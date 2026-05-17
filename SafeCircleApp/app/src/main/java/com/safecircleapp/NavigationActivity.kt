package com.safecircleapp

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.safecircleapp.databinding.ActivityMapsBinding
import com.safecircleapp.databinding.ActivityNavigationBinding
import de.hdodenhof.circleimageview.CircleImageView
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.overlay.Marker
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay



class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var userReference: DatabaseReference
    private lateinit var circleReference: DatabaseReference

    private lateinit var imageView: CircleImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationBinding
    private val markersMap: MutableMap<String, Marker> = mutableMapOf()
    private lateinit var mapView: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Load OSM configuration
        Configuration.getInstance().load(
            applicationContext,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigation.toolbar)

// Initialize OSM MapView
        mapView = findViewById(R.id.map) as MapView
        mapView.setTileSource(TileSourceFactory.MAPNIK) // Set tile source to OSM's MAPNIK
        mapView.setBuiltInZoomControls(true) // Enable zoom controls
        mapView.setMultiTouchControls(true)  // Enable multi-touch gestures

// Set up location overlay
        locationOverlay = MyLocationNewOverlay(mapView)
        locationOverlay.enableMyLocation()  // Enable user location tracking
        mapView.overlays.add(locationOverlay) // Add overlay to map

        // Center map on user location with a zoom level
        mapView.controller.setZoom(15.0)
        locationOverlay.runOnFirstFix {
            val userLocation = locationOverlay.myLocation
            if (userLocation != null) {
                mapView.controller.setCenter(
                    GeoPoint(
                        userLocation.latitude,
                        userLocation.longitude
                    )
                )
            }

            userReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(auth.currentUser?.uid ?: "")
            circleReference = FirebaseDatabase.getInstance().getReference("CircleMembers")

            // Find Views
            val navView: NavigationView = binding.navView
            imageView = navView.getHeaderView(0).findViewById(R.id.imageView)
            textViewName = navView.getHeaderView(0).findViewById(R.id.textViewName)
            textViewEmail = navView.getHeaderView(0).findViewById(R.id.textViewEmail)
            val fab: FloatingActionButton = findViewById(R.id.fab)
            fab.setOnClickListener {
                showCircleMembersList()
            }

            // Load user data
            loadUserData()

            val drawerLayout: DrawerLayout = binding.drawerLayout
            val navController = findNavController(R.id.nav_host_fragment_content_navigation)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home, R.id.nav_signOut,
                    R.id.nav_joinCircle, R.id.nav_myCircle,
                    R.id.nav_inviteMembers, R.id.nav_sendSOS, R.id.nav_ShareLoc, R.id.nav_settings
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            navView.setNavigationItemSelectedListener(this)

            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            initializeLocationServices()
        }
    }

    private fun initializeLocationServices() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Start listening to circle members' locations
            listenForCircleMemberLocations()
        }
    }

    private fun listenForCircleMemberLocations() {
        // Get the current user's user ID
        userReference.child("userId").get().addOnSuccessListener { snapshot ->
            val currentUserId = snapshot.value?.toString() ?: return@addOnSuccessListener

            // Reference to the circle members under the current user's node
            val circleMembersReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUserId)  // Main user's userId
                .child("CircleMembers")

            circleMembersReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (circleMemberSnapshot in snapshot.children) {
                        val circleMemberId = circleMemberSnapshot.key ?: continue

                        // Fetch the live location of each circle member from their own user node
                        val memberRef = FirebaseDatabase.getInstance().getReference("Users").child(circleMemberId)
                        memberRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(memberSnapshot: DataSnapshot) {
                                val latitude = memberSnapshot.child("latitude").getValue(Double::class.java)
                                val longitude = memberSnapshot.child("longitude").getValue(Double::class.java)
                                val name = memberSnapshot.child("name").getValue(String::class.java) ?: "Circle Member"
                                val isSharing = memberSnapshot.child("sharing").getValue(String::class.java)

                                if (latitude != null && longitude != null && isSharing == "true") {
                                    val latLng = GeoPoint(latitude, longitude)

                                    // Add or update marker on OSM
                                    val marker = markersMap[circleMemberId] ?: Marker(mapView).apply {
                                        mapView.overlays.add(this)
                                        markersMap[circleMemberId] = this
                                    }

                                    marker.position = latLng
                                    marker.title = name
                                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                                    mapView.invalidate() // Refresh the map
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@NavigationActivity, "Error fetching location", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@NavigationActivity, "Error fetching members", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showCircleMembersList() {
        userReference.child("CircleMembers").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val memberNames = mutableListOf<String>()
                val memberIds = mutableListOf<String>()

                for (circleMemberSnapshot in snapshot.children) {
                    val name = circleMemberSnapshot.child("name").getValue(String::class.java) ?: "You"
                    val userId = circleMemberSnapshot.key ?: continue

                    memberNames.add(name)
                    memberIds.add(userId)
                }

                val builder = AlertDialog.Builder(this@NavigationActivity)
                builder.setTitle("Select Circle Member")

                builder.setItems(memberNames.toTypedArray()) { _, which ->
                    val selectedMemberId = memberIds[which]
                    handleCircleMemberClick(selectedMemberId)
                }

                builder.show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NavigationActivity, "Error fetching members", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleCircleMemberClick(userId: String) {
        val memberRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        memberRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val latitude = snapshot.child("latitude").getValue(Double::class.java)
                val longitude = snapshot.child("longitude").getValue(Double::class.java)
                val name = snapshot.child("name").getValue(String::class.java) ?: "Circle Member"
                val isSharing = snapshot.child("sharing").getValue(String::class.java)

                if (isSharing == "false") {
                    Toast.makeText(this@NavigationActivity, "$name is not sharing their location at this time.", Toast.LENGTH_SHORT).show()
                } else if (latitude != null && longitude != null) {
                    // Snap to the member's location on OSM
                    val memberLocation = GeoPoint(latitude, longitude)
                    mapView.controller.animateTo(memberLocation)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NavigationActivity, "Error fetching location", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_signOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_joinCircle -> {
                val intent = Intent(this, JoinCircleActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_myCircle -> {
                val intent = Intent(this, MyCircleActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_inviteMembers -> {
                shareCurrentUserCircleCode()
                return true
            }
            R.id.nav_sendSOS -> {
                val intent = Intent(this, EmergencySosActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_ShareLoc -> {
                shareCurrentLocation()
                return true
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initializeLocationServices()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun loadUserData() {
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java) ?: ""
                val profileImageUrl = snapshot.child("imageUrl").getValue(String::class.java) ?: ""

                textViewName.text = name
                textViewEmail.text = email

                // Load profile image with Glide
                Glide.with(this@NavigationActivity)
                    .load(profileImageUrl)
                    .into(imageView)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NavigationActivity, "Error loading user data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MunicipalServicesApplication
{
    public partial class LocalEvents : Form
    {

        private SortedDictionary<DateTime, Event> eventsDictionary; // SortedDictionary
        private HashSet<string> categories;
        private PriorityQueue<Event, DateTime> priorityEvents; // PriorityQueue for prioritizing events
        private List<UserSearch> userSearchHistory = new List<UserSearch>();
        private Dictionary<string, int> searchHistory = new Dictionary<string, int>();

        private Dictionary<string, int> keywordFrequency = new Dictionary<string, int>();
        private Dictionary<string, int> categoryFrequency = new Dictionary<string, int>();
        private Dictionary<DateTime, int> dateFrequency = new Dictionary<DateTime, int>();


        private void UpdateSearchFrequency(string category, DateTime date)
        {
            // Update category frequency
            if (categoryFrequency.ContainsKey(category))
            {
                categoryFrequency[category]++;
            }
            else
            {
                categoryFrequency[category] = 1;
            }
            Console.WriteLine($"Updated category frequency: {category} = {categoryFrequency[category]}");

            // Update date frequency
            if (dateFrequency.ContainsKey(date.Date))
            {
                dateFrequency[date.Date]++;
            }
            else
            {
                dateFrequency[date.Date] = 1;
            }
            Console.WriteLine($"Updated date frequency: {date.ToShortDateString()} = {dateFrequency[date.Date]}");
        }

        private List<string> GetTopRecommendations()
        {
            var topKeywords = keywordFrequency.OrderByDescending(kvp => kvp.Value).Take(5).Select(kvp => kvp.Key);
            var topCategories = categoryFrequency.OrderByDescending(kvp => kvp.Value).Take(5).Select(kvp => kvp.Key);
            var topDates = dateFrequency.OrderByDescending(kvp => kvp.Value).Take(5).Select(kvp => kvp.Key);

            return topKeywords.ToList();
        }


        private void DisplayRecommendations()
        {
            flowLayoutPanelRecommendations.Controls.Clear();

            // Get the top 5 categories based on frequency
            var topCategories = categoryFrequency.OrderByDescending(c => c.Value)
                                                 .Take(5)
                                                 .Select(c => c.Key)
                                                 .ToList();

            // Display logs
            Console.WriteLine("Top Categories: " + string.Join(", ", topCategories));

            // Use a list to gather recommended events
            var recommendedEvents = new List<KeyValuePair<DateTime, Event>>();

            // Find events that match the top categories
            foreach (var category in topCategories)
            {
                // Find events for each top category
                var matchingEvents = eventsDictionary
                    .Where(evt => evt.Value.Category == category)
                    .ToList();

                // Add matching events to the recommended events list
                if (matchingEvents.Any())
                {
                    Console.WriteLine($"Found {matchingEvents.Count} matching events for Category: {category}");
                    recommendedEvents.AddRange(matchingEvents);
                }
            }

            // Limit to top 5 events
            var topRecommendedEvents = recommendedEvents
                .GroupBy(evt => evt.Key)
                .SelectMany(g => g.OrderByDescending(evt => evt.Value.Date)
                                  .Take(5))
                .Distinct() 
                .Take(5) 
                .ToList();

            // Display recommended events
            if (!topRecommendedEvents.Any())
            {
                MessageBox.Show("No recommendations available based on your search.", "No Recommendations");
                return;
            }

            foreach (var evt in topRecommendedEvents)
            {
                Panel recommendationPanel = CreateEventCard(evt.Key, evt.Value.Description, evt.Value.Category, evt.Value);
                flowLayoutPanelRecommendations.Controls.Add(recommendationPanel);
            }


        }

        private void RecordUserSearch(string category, string keyword, DateTime? date)
        {
            userSearchHistory.Add(new UserSearch { Category = category, Keyword = keyword, Date = date });
        }

        public LocalEvents()
        {
            InitializeComponent();
            InitializeEventsData();
            DisplayEventCards();
        }

        private void DisplayEventCards()
        {
            Random random = new Random();

            // Sort the events so that favourites come first
            var sortedEvents = eventsDictionary.OrderByDescending(e => e.Value.IsFavourite);

            // Clear the FlowLayoutPanel before adding new cards
            flowLayoutPanel1.Controls.Clear();

            foreach (var eventItem in sortedEvents)
            {
                // Create a Panel
                Panel card = new Panel
                {
                    Size = new Size(200, 270),
                    BorderStyle = BorderStyle.FixedSingle,
                    Padding = new Padding(10),
                    Margin = new Padding(10)
                };

                // Create a FlowLayoutPanel
                FlowLayoutPanel cardContent = new FlowLayoutPanel
                {
                    FlowDirection = FlowDirection.TopDown, // Arrange vertically
                    Dock = DockStyle.Fill,
                    AutoSize = true,
                    WrapContents = false
                };

                // Generate a random number between 1 and 5 for random images
                int randomIndex = random.Next(1, 6);
                Image randomImage = null;

                switch (randomIndex)
                {
                    case 1:
                        randomImage = Properties.Resources.Image1;
                        break;
                    case 2:
                        randomImage = Properties.Resources.Image2;
                        break;
                    case 3:
                        randomImage = Properties.Resources.Image3;
                        break;
                    case 4:
                        randomImage = Properties.Resources.Image4;
                        break;
                    case 5:
                        randomImage = Properties.Resources.Image5;
                        break;
                }

                // Create PictureBox for the event image
                PictureBox eventImage = new PictureBox
                {
                    SizeMode = PictureBoxSizeMode.StretchImage,
                    Size = new Size(180, 120),
                    Image = randomImage // Use the random image from Resources
                };

                // Create Label for the description
                Label descriptionLabel = new Label
                {
                    Text = eventItem.Value.Description,
                    AutoSize = true,
                    Font = new Font("Arial", 10),
                    Margin = new Padding(0, 10, 0, 0)
                };

                // Create Label for the date
                Label dateLabel = new Label
                {
                    Text = "Date: " + eventItem.Value.Date.ToString("MMMM dd, yyyy"),
                    AutoSize = true,
                    Font = new Font("Arial", 9, FontStyle.Italic),
                    ForeColor = Color.Gray,
                    Margin = new Padding(0, 5, 0, 0)
                };

                // Create Label for the category
                Label categoryLabel = new Label
                {
                    Text = "Category: " + eventItem.Value.Category,
                    AutoSize = true,
                    Font = new Font("Arial", 9, FontStyle.Bold),
                    ForeColor = Color.DarkBlue,
                    Margin = new Padding(0, 5, 0, 0)
                };

                // Create PictureBox for the star icon
                PictureBox starIcon = new PictureBox
                {
                    SizeMode = PictureBoxSizeMode.Zoom,
                    Size = new Size(24, 24),
                    Image = eventItem.Value.IsFavourite ? Properties.Resources.staryellow : Properties.Resources.stargrey,
                    Cursor = Cursors.Hand,
                    Margin = new Padding(0, 10, 0, 0)
                };

                starIcon.Click += (sender, e) =>
                {
                    eventItem.Value.IsFavourite = !eventItem.Value.IsFavourite;
                    starIcon.Image = eventItem.Value.IsFavourite ? Properties.Resources.staryellow : Properties.Resources.stargrey; // Update the star icon
                };

                // Add controls to the FlowLayoutPanel
                cardContent.Controls.Add(eventImage);
                cardContent.Controls.Add(descriptionLabel);
                cardContent.Controls.Add(dateLabel);
                cardContent.Controls.Add(categoryLabel);

                // Add the star icon to the card content
                cardContent.Controls.Add(starIcon);

                // Add FlowLayoutPanel to the card Panel
                card.Controls.Add(cardContent);

                // Add card to FlowLayoutPanel on the form
                flowLayoutPanel1.Controls.Add(card);
            }
        }



        private void InitializeEventsData()
        {
            // SortedDictionary to store event data
            eventsDictionary = new SortedDictionary<DateTime, Event>
    {
        { new DateTime(2024, 10, 15), new Event { Description = "Community Cleanup", Category = "Community", Date = new DateTime(2024, 10, 15) } },
        { new DateTime(2024, 11, 01), new Event { Description = "Town Hall Meeting", Category = "Government", Date = new DateTime(2024, 11, 01) } },
        { new DateTime(2024, 12, 05), new Event { Description = "Holiday Parade", Category = "Holiday", Date = new DateTime(2024, 12, 05) } },
        { new DateTime(2024, 12, 20), new Event { Description = "Winter Festival", Category = "Holiday", Date = new DateTime(2024, 12, 20) } },
        { new DateTime(2024, 10, 20), new Event { Description = "Charity Run", Category = "Community", Date = new DateTime(2024, 10, 20) } },
        { new DateTime(2024, 11, 15), new Event { Description = "Election Day", Category = "Government", Date = new DateTime(2024, 11, 15) } },
        { new DateTime(2024, 11, 20), new Event { Description = "Park Clean-Up", Category = "Community", Date = new DateTime(2024, 11, 20) } },
        { new DateTime(2024, 12, 01), new Event { Description = "Holiday Tree Lighting", Category = "Holiday", Date = new DateTime(2024, 12, 01) } },
        { new DateTime(2024, 12, 10), new Event { Description = "School Board Meeting", Category = "Government", Date = new DateTime(2024, 12, 10) } },
        { new DateTime(2025, 01, 10), new Event { Description = "New Year’s Celebration", Category = "Holiday", Date = new DateTime(2025, 01, 10) } },
        { new DateTime(2025, 02, 14), new Event { Description = "Valentine’s Day Party", Category = "Holiday", Date = new DateTime(2025, 02, 14) } },
        { new DateTime(2025, 03, 15), new Event { Description = "City Fair", Category = "Community", Date = new DateTime(2025, 03, 15) } },
        { new DateTime(2025, 04, 05), new Event { Description = "Environmental Awareness Day", Category = "Community", Date = new DateTime(2025, 04, 05) } },
        { new DateTime(2025, 05, 01), new Event { Description = "Labor Day Parade", Category = "Holiday", Date = new DateTime(2025, 05, 01) } },
        { new DateTime(2025, 06, 21), new Event { Description = "Summer Solstice Festival", Category = "Holiday", Date = new DateTime(2025, 06, 21) } },
        { new DateTime(2025, 07, 04), new Event { Description = "Independence Day Fireworks", Category = "Holiday", Date = new DateTime(2025, 07, 04) } },
        { new DateTime(2025, 08, 15), new Event { Description = "Farmers Market", Category = "Community", Date = new DateTime(2025, 08, 15) } },
        { new DateTime(2025, 09, 10), new Event { Description = "City Council Meeting", Category = "Government", Date = new DateTime(2025, 09, 10) } },
        { new DateTime(2025, 10, 31), new Event { Description = "Halloween Costume Party", Category = "Holiday", Date = new DateTime(2025, 10, 31) } },
        { new DateTime(2025, 12, 25), new Event { Description = "Christmas Day Parade", Category = "Holiday", Date = new DateTime(2025, 12, 25) } }
    };

            // HashSet to store unique categories
            categories = new HashSet<string>
    {
        "Community",
        "Government",
        "Holiday"
    };

            // Initialize the priority queue
            priorityEvents = new PriorityQueue<Event, DateTime>();

            // Add all events to the priority queue for processing
            foreach (var evt in eventsDictionary)
            {
                priorityEvents.Enqueue(evt.Value, evt.Key);
            }

            // Add categories to comboBox
            comboBoxCategory1.Items.Clear(); // Clear any previous items
            comboBoxCategory1.Items.Add("All"); // Add an "All" option
            foreach (var category in categories)
            {
                comboBoxCategory1.Items.Add(category);
            }
            comboBoxCategory1.SelectedIndex = 0;
        }


        private void LocalEvents_Load(object sender, EventArgs e)
        {

        }

        private Panel CreateEventCard(DateTime eventDate, string description, string category, Event eventItem)
        {
            Random random = new Random();

            // Create a Panel
            Panel card = new Panel
            {
                Size = new Size(200, 270),
                BorderStyle = BorderStyle.FixedSingle,
                Padding = new Padding(10),
                Margin = new Padding(10),
                BackColor = Color.White
            };

            // Create a FlowLayoutPanel to arrange the image and text vertically
            FlowLayoutPanel cardContent = new FlowLayoutPanel
            {
                FlowDirection = FlowDirection.TopDown,
                Dock = DockStyle.Fill,
                AutoSize = true,
                WrapContents = false
            };

            // Generate a random number between 1 and 5 for random images
            int randomIndex = random.Next(1, 6);
            Image randomImage = null;

            switch (randomIndex)
            {
                case 1:
                    randomImage = Properties.Resources.Image1;
                    break;
                case 2:
                    randomImage = Properties.Resources.Image2;
                    break;
                case 3:
                    randomImage = Properties.Resources.Image3;
                    break;
                case 4:
                    randomImage = Properties.Resources.Image4;
                    break;
                case 5:
                    randomImage = Properties.Resources.Image5;
                    break;
            }

            // Create PictureBox for the event image
            PictureBox eventImage = new PictureBox
            {
                SizeMode = PictureBoxSizeMode.StretchImage,
                Size = new Size(180, 120),
                Image = randomImage // Use the random image from Resources
            };

            // Create Label for the description
            Label descriptionLabel = new Label
            {
                Text = description,
                AutoSize = true,
                Font = new Font("Arial", 10),
                Margin = new Padding(0, 10, 0, 0)
            };

            // Create Label for the date
            Label dateLabel = new Label
            {
                Text = "Date: " + eventDate.ToString("MMMM dd, yyyy"),
                AutoSize = true,
                Font = new Font("Arial", 9, FontStyle.Italic),
                ForeColor = Color.Gray,
                Margin = new Padding(0, 5, 0, 0)
            };

            // Create Label for the category
            Label categoryLabel = new Label
            {
                Text = "Category: " + category,
                AutoSize = true,
                Font = new Font("Arial", 9, FontStyle.Bold),
                ForeColor = Color.DarkBlue,
                Margin = new Padding(0, 5, 0, 0)
            };

            // Create PictureBox for the star icon
            PictureBox starIcon = new PictureBox
            {
                SizeMode = PictureBoxSizeMode.Zoom,
                Size = new Size(24, 24),
                Image = eventItem.IsFavourite ? Properties.Resources.staryellow : Properties.Resources.stargrey,
                Cursor = Cursors.Hand,
                Margin = new Padding(0, 10, 0, 0)
            };

            starIcon.Click += (sender, e) =>
            {
                eventItem.IsFavourite = !eventItem.IsFavourite;
                starIcon.Image = eventItem.IsFavourite ? Properties.Resources.staryellow : Properties.Resources.stargrey;
            };

            // Add controls to the FlowLayoutPanel
            cardContent.Controls.Add(eventImage);
            cardContent.Controls.Add(descriptionLabel);
            cardContent.Controls.Add(dateLabel);
            cardContent.Controls.Add(categoryLabel);

            // Add the star icon to the card
            cardContent.Controls.Add(starIcon);

            // Add FlowLayoutPanel to the card
            card.Controls.Add(cardContent);

            return card;
        }



        private Dictionary<string, int> AnalyzeSearchPatterns()
        {
            Dictionary<string, int> frequency = new Dictionary<string, int>();

            foreach (var search in userSearchHistory)
            {
                if (!string.IsNullOrEmpty(search.Category))
                {
                    if (!frequency.ContainsKey(search.Category))
                        frequency[search.Category] = 0;
                    frequency[search.Category]++;
                }

                if (!string.IsNullOrEmpty(search.Keyword))
                {
                    if (!frequency.ContainsKey(search.Keyword))
                        frequency[search.Keyword] = 0;
                    frequency[search.Keyword]++;
                }
            }

            return frequency.OrderByDescending(kv => kv.Value).Take(5).ToDictionary(kv => kv.Key, kv => kv.Value); // Return top 5
        }


        // Recommend events based on user search history
        private List<Event> RecommendEvents(Dictionary<string, int> searchPreferences)
        {
            List<Event> recommendedEvents = new List<Event>();

            foreach (var preference in searchPreferences)
            {
                foreach (var evt in eventsDictionary.Values)
                {
                    if (evt.Category == preference.Key || evt.Description.Contains(preference.Key))
                    {
                        recommendedEvents.Add(evt);
                    }
                }
            }

            return recommendedEvents;
        }



        private void btnSearchCategoryKeyword_Click_1(object sender, EventArgs e)
        {
            // Get the selected category
            string selectedCategory = comboBoxCategory1.SelectedItem?.ToString();
            // Get the keyword entered by the user
            string keyword = txtKeyword.Text.Trim().ToLower();

            // Filter events based on category and keyword
            var filteredEvents = eventsDictionary
                .Where(evt =>
                    // Check if category matches
                    (selectedCategory == "All" || evt.Value.Category == selectedCategory) &&
                    // Check if the keyword matches any part of the event description
                    (string.IsNullOrEmpty(keyword) || evt.Value.Description.ToLower().Contains(keyword))
                )
                .ToList();

            // Sort filtered events, placing favorited events first
            var sortedEvents = filteredEvents
                .OrderByDescending(evt => evt.Value.IsFavourite)
                .ToList();

            flowLayoutPanel1.Controls.Clear();

            // Display the sorted events
            foreach (var evt in sortedEvents)
            {
                Panel card = CreateEventCard(evt.Key, evt.Value.Description, evt.Value.Category, evt.Value);
                if (card != null) 
                {
                    flowLayoutPanel1.Controls.Add(card);
                }
            }


            // Notify the user if no events match the filters
            if (!sortedEvents.Any())
            {
                MessageBox.Show("No events match the selected category or keyword.", "No Results");
            }

            UpdateSearchFrequency(selectedCategory, DateTime.Now);
            DisplayRecommendations();
        }


        private List<Event> FilterEvents(string category, string keyword, DateTime? selectedDate)
        {
            IEnumerable<Event> filteredEvents = eventsDictionary.Values;

            // Filter by category
            if (!string.IsNullOrEmpty(category) && category != "All")
            {
                filteredEvents = filteredEvents.Where(evt => evt.Category.Equals(category, StringComparison.OrdinalIgnoreCase));
            }

            // Filter by keyword
            if (!string.IsNullOrEmpty(keyword))
            {
                filteredEvents = filteredEvents.Where(evt => evt.Description.IndexOf(keyword, StringComparison.OrdinalIgnoreCase) >= 0);
            }

            // Filter by date
            if (selectedDate.HasValue)
            {
                filteredEvents = filteredEvents.Where(evt => evt.Date.Date == selectedDate.Value.Date);
            }

            return filteredEvents.ToList();
        }


        private void DisplayEventCards(string category = null, string keyword = null, DateTime? selectedDate = null)
        {
            flowLayoutPanel1.Controls.Clear();

            // Filter events based on the category, keyword, and date
            var filteredEvents = FilterEvents(category, keyword, selectedDate);

            Random random = new Random();
            string basePath = @"C:\Users\sahen\source\repos\MunicipalServicesApplication\MunicipalServicesApplication\Images\";

            foreach (var eventItem in filteredEvents)
            {
                Panel card = CreateEventCard(eventItem.Date, eventItem.Description, eventItem.Category, eventItem);
                if (card != null)
                {
                    flowLayoutPanel1.Controls.Add(card);
                }
            }

        }


        private void btnSearchDate_Click_1(object sender, EventArgs e)
        {
            string selectedCategory = comboBoxCategory1.SelectedItem.ToString();
            string keyword = txtKeyword.Text;
            DateTime selectedDate = dateTimePicker.Value;

            DisplayEventCards(selectedCategory, keyword, selectedDate);
            UpdateSearchFrequency(selectedCategory, selectedDate);
            DisplayRecommendations();
        }

        private void UpdateSearchHistory(string keyword)
        {
            if (searchHistory.ContainsKey(keyword))
            {
                searchHistory[keyword]++;
            }
            else
            {
                searchHistory[keyword] = 1;
            }
        }

        private List<Event> GetRecommendedEvents()
        {
            var recommendedEvents = new List<Event>();

            // Prioritize events containing frequently searched keywords
            foreach (var keyword in searchHistory.OrderByDescending(k => k.Value).Select(k => k.Key))
            {
                var matchingEvents = eventsDictionary.Values
                    .Where(evt => evt.Description.IndexOf(keyword, StringComparison.OrdinalIgnoreCase) >= 0)
                    .ToList();
                recommendedEvents.AddRange(matchingEvents);
            }

            // Remove duplicates from the recommended events list
            return recommendedEvents.Distinct().ToList();
        }

        private void DisplayRecommendedEvents()
        {
            flowLayoutPanelRecommendations.Controls.Clear();
            var recommendedEvents = GetRecommendedEvents();

            foreach (var evt in recommendedEvents)
            {
                Label eventLabel = new Label();
                eventLabel.Text = evt.Description;
                eventLabel.AutoSize = true;
                eventLabel.Padding = new Padding(5);
                eventLabel.Margin = new Padding(10);

                // Add the event label to the flowLayoutPanel
                flowLayoutPanelRecommendations.Controls.Add(eventLabel);
            }
        }

        private void OnSearch(string keyword)
        {
            UpdateSearchHistory(keyword);
            DisplayRecommendedEvents();
        }

        private void btnBack_Click(object sender, EventArgs e)
        {
            Form1 form1 = new Form1();
            form1.Show();
            this.Close();
        }
    }


    // Event class to store event details
    public class Event
    {
        public string Description { get; set; }
        public string Category { get; set; }
        public DateTime Date { get; set; }
        public bool IsFavourite { get; set; }
    }




}

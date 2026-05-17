using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MunicipalServicesApplication
{
    public partial class Form1 : Form
    {

        public Form1()
        {
            InitializeComponent();
            InitializeMenu();

            panel2.BackColor = Color.FromArgb(0, Color.Black);
        }

        private void InitializeMenu()
        {
            btnReportIssues.Enabled = true;
            btnLocalEvents.Enabled = true;  
            btnServiceStatus.Enabled = false; 

            btnReportIssues.Click += BtnReportIssues_Click;
        }

        private void BtnReportIssues_Click(object sender, EventArgs e)
        {
            // Open the Report Issues form
            ReportIssuesForm reportForm = new ReportIssuesForm();
            reportForm.ShowDialog();
        }

        private void btnLocalEvents_Click(object sender, EventArgs e)
        {
            MessageBox.Show("This feature is under development.");
        }

        private void btnServiceStatus_Click(object sender, EventArgs e)
        {
            MessageBox.Show("This feature is under development.");
        }

        private void btnLocalEvents_Click_1(object sender, EventArgs e)
        {
            LocalEvents localevents = new LocalEvents();
            localevents.ShowDialog();
            this.Hide();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            
        }

        private void panel2_Paint(object sender, PaintEventArgs e)
        {

        }

        private void btnServiceStatus_Click_1(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void btnReportIssues_Click_1(object sender, EventArgs e)
        {
            this.Hide();
        }

        private void label3_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            MessageBox.Show("Feature Coming Soon!!!");
        }

        private void btnSanitation_Click(object sender, EventArgs e)
        {
            SanitationChat sanitationChat = new SanitationChat();
            sanitationChat.Show();
            this.Hide();
        }

        private void btnRoads_Click(object sender, EventArgs e)
        {
            RoadsChat roadsChat = new RoadsChat();
            roadsChat.Show();
            this.Hide();
        }

        private void btnUtilities_Click(object sender, EventArgs e)
        {
            UtilitiesChat utilitiesChat = new UtilitiesChat();
            utilitiesChat.Show();
            this.Hide();
        }

        private void btnOther_Click(object sender, EventArgs e)
        {
            OtherChat otherChat = new OtherChat();
            otherChat.Show();
            this.Hide();
        }
    }
}
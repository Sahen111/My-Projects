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
    public partial class ReportIssuesForm : Form
    {
        private LinkedList<Issue> reportedIssues;

        public ReportIssuesForm()
        {
            InitializeComponent();
            InitializeForm();
            reportedIssues = new LinkedList<Issue>();
        }

        private void InitializeForm()
        {
            // Initialize the ComboBox with categories
            cmbCategory.Items.AddRange(new string[] { "Sanitation", "Roads", "Utilities", "Other" });
            cmbCategory.SelectedIndex = 0;
        }

        private void btnAttachFile_Click(object sender, EventArgs e)
        {
            try
            {
                // Create and configure the OpenFileDialog
                OpenFileDialog openFileDialog = new OpenFileDialog();
                openFileDialog.Filter = "Image Files|*.jpg;*.jpeg;*.png|Document Files|*.doc;*.docx;*.pdf|All Files|*.*";
                openFileDialog.Title = "Select a file to attach";

                // Show the dialog and check if the user selected a file
                if (openFileDialog.ShowDialog() == DialogResult.OK)
                {
                    // Get the selected file path
                    string selectedFilePath = openFileDialog.FileName;

                    lblNoFile.Text = $"Attached: {System.IO.Path.GetFileName(selectedFilePath)}";

                    // Check if it's an image file
                    if (selectedFilePath.EndsWith(".jpg") || selectedFilePath.EndsWith(".jpeg") || selectedFilePath.EndsWith(".png"))
                    {
                        // Display the selected image in the PictureBox
                        pboxReportIssues.Image = Image.FromFile(selectedFilePath);
                    }
                    else
                    {
                        // If it's not an image, clear the PictureBox
                        pboxReportIssues.Image = null;
                    }

                    MessageBox.Show($"File attached successfully: {selectedFilePath}", "File Attached", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"An error occurred while attaching the file: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }



        private void btnSubmit_Click(object sender, EventArgs e)
        {
            string location = txtLocation.Text;
            string category = cmbCategory.SelectedItem.ToString();
            string description = rtbDescription.Text;

            // Check if the required fields are filled
            if (string.IsNullOrEmpty(location) || string.IsNullOrEmpty(description))
            {
                MessageBox.Show("Please fill in all required fields.");
                return;
            }

            // Check if either an image or a file is attached
            if (pboxReportIssues.Image == null && string.IsNullOrEmpty(lblNoFile.Text))
            {
                // Ask the user if they want to submit without an image or file
                DialogResult noAttachmentResult = MessageBox.Show("You haven't attached an image or file. Do you want to submit without attaching an image or file?", "No Attachment", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

                if (noAttachmentResult == DialogResult.No)
                {
                    // Allow them to attach a file or image
                    AttachImage();
                }
                else
                {
                    // If the user clicks 'Yes', show the confirmation message
                    ShowConfirmationMessage(location, category, description);
                    return;
                }
            }
            else
            {
                // If an image or file is attached, directly show the confirmation message
                ShowConfirmationMessage(location, category, description);
            }
        }


        private void ShowConfirmationMessage(string location, string category, string description)
        {
            // Create the confirmation message with the user's input
            string confirmationMessage = $"Location: {location}\n" +
                                         $"Category: {category}\n" +
                                         $"Description: {description}\n\n" +
                                         "Are you sure you want to submit this issue?";

            // Show the confirmation message box
            DialogResult dialogResult = MessageBox.Show(confirmationMessage, "Confirm Submission", MessageBoxButtons.YesNo, MessageBoxIcon.Question);

            // If the user clicks 'Yes', submit the issue
            if (dialogResult == DialogResult.Yes)
            {
                SubmitIssue(location, category, description);
            }
            else
            {
                // If the user clicks 'No', Cancel the submission
                MessageBox.Show("Issue submission canceled.");
            }
        }

        private void SubmitIssue(string location, string category, string description)
        {
            // Create a new issue object
            Issue newIssue = new Issue
            {
                Location = location,
                Category = category,
                Description = description,
                DateReported = DateTime.Now
            };

            // Store the issue in the linked list
            reportedIssues.AddLast(newIssue);

            MessageBox.Show("Issue reported successfully!");

            Form1 form1 = new Form1();
            form1.Show();
            this.Close();
        }

        private void AttachImage()
        {
            try
            {
                // Create and configure the OpenFileDialog
                OpenFileDialog openFileDialog = new OpenFileDialog();
                openFileDialog.Filter = "Image Files|*.jpg;*.jpeg;*.png|Document Files|*.doc;*.docx;*.pdf|All Files|*.*";
                openFileDialog.Title = "Select a file to attach";

                // Show the dialog and check if the user selected a file
                if (openFileDialog.ShowDialog() == DialogResult.OK)
                {
                    // Get the selected file path
                    string selectedFilePath = openFileDialog.FileName;

                    // Check if it's an image file
                    if (selectedFilePath.EndsWith(".jpg") || selectedFilePath.EndsWith(".jpeg") || selectedFilePath.EndsWith(".png"))
                    {
                        // Display the selected image in the PictureBox
                        pboxReportIssues.Image = Image.FromFile(selectedFilePath);
                    }

                    MessageBox.Show($"File attached successfully: {selectedFilePath}", "File Attached", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"An error occurred while attaching the file: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }



        private void btnBack_Click(object sender, EventArgs e)
        {
            Form1 form1 = new Form1();
            form1.Show();
            this.Close();
        }

        private void lblNoFile_Click(object sender, EventArgs e)
        {

        }
    }

    public class Issue
    {
        public string Location { get; set; }
        public string Category { get; set; }
        public string Description { get; set; }
        public DateTime DateReported { get; set; }
    }
}
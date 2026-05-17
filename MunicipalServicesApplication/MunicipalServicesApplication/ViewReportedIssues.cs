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
    public partial class ViewReportedIssues : Form
    {

        private LinkedList<Issue> reportedIssues;

        public ViewReportedIssues(LinkedList<Issue> issues)
        {
            InitializeComponent();
            reportedIssues = issues;
            PopulateIssues();
        }

        private void PopulateIssues()
        {
            foreach (var issue in reportedIssues)
            {
                datagridIssues.Rows.Add(issue.Location, issue.Category, issue.Description, issue.DateReported);
            }
        }

        private void datagridIssues_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }
    }
}

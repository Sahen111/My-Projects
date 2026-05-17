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
    public partial class LoadingPage : Form
    {
        public LoadingPage()
        {
            InitializeComponent();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            progressBar1.Increment(3);

            if (progressBar1.Value == progressBar1.Maximum)
            {
                timer1.Stop();

                this.Hide();

                Form1 form1 = new Form1();
                form1.Show();
            }
        }

        private void LoadingPage_Load(object sender, EventArgs e)
        {
            timer1.Start();
        }
    }
}

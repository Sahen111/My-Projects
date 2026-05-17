using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ReflectionsForm
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        //global variable for assembly
        private string assemblyPath;

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            //oprn file dialog --> browse for the file
            OpenFileDialog ofg = new OpenFileDialog();
            ofg.Filter = "DLL File(*.dll)|*.dll";
            ofg.Title = "Select a .dll file";

            if (ofg.ShowDialog() == DialogResult.OK)
            {
                //stores the info in a class level variable
                assemblyPath = ofg.FileName;
                //place it into the textbox
                textBox1.Text = assemblyPath;
            }
            else
            {
                MessageBox.Show("Error - loading file");
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            //check if the file path is valid
            if(!string.IsNullOrEmpty(assemblyPath))
            {
                Assembly assembly = Assembly.LoadFrom(assemblyPath);
                //Clear the listboxes before you populate them
                listBox1.Items.Clear();
                listBox2.Items.Clear();
                listBox3.Items.Clear();

                //Now fetch the meta data
                foreach (Type type in assembly.GetTypes())
                {
                    //pull the method info
                    foreach (MethodInfo methodInfo in type.GetMethods())
                    {
                        listBox1.Items.Add(methodInfo.ReturnType.Name
                            + " " + methodInfo.Name);
                    }
                    //properties
                    foreach (PropertyInfo property in type.GetProperties())
                    {
                        listBox2.Items.Add(property.PropertyType.Name
                            + " " + property.Name);
                    }
                    //constructors
                    foreach (ConstructorInfo constructor in type.GetConstructors())
                    {
                        listBox3.Items.Add(constructor.ToString());
                    }
                }//outer foreach ends
            } //if ends
            else
            {
                MessageBox.Show("Select a valid .dll file");
            }
        }
    }
}

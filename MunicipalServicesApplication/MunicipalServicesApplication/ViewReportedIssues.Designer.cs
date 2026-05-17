namespace MunicipalServicesApplication
{
    partial class ViewReportedIssues
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.datagridIssues = new System.Windows.Forms.DataGridView();
            ((System.ComponentModel.ISupportInitialize)(this.datagridIssues)).BeginInit();
            this.SuspendLayout();
            // 
            // datagridIssues
            // 
            this.datagridIssues.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.datagridIssues.Location = new System.Drawing.Point(160, 94);
            this.datagridIssues.Name = "datagridIssues";
            this.datagridIssues.RowHeadersWidth = 51;
            this.datagridIssues.RowTemplate.Height = 24;
            this.datagridIssues.Size = new System.Drawing.Size(440, 278);
            this.datagridIssues.TabIndex = 0;
            this.datagridIssues.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.datagridIssues_CellContentClick);
            // 
            // ViewReportedIssues
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.datagridIssues);
            this.Name = "ViewReportedIssues";
            this.Text = "ViewReportedIssues";
            ((System.ComponentModel.ISupportInitialize)(this.datagridIssues)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView datagridIssues;
    }
}
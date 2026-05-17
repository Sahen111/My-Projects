namespace MunicipalServicesApplication
{
    partial class LocalEvents
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
            this.dateTimePicker = new System.Windows.Forms.DateTimePicker();
            this.flowLayoutPanel1 = new System.Windows.Forms.FlowLayoutPanel();
            this.btnSearchCategoryKeyword = new MaterialSkin.Controls.MaterialButton();
            this.comboBoxCategory1 = new MaterialSkin.Controls.MaterialComboBox();
            this.txtKeyword = new MaterialSkin.Controls.MaterialTextBox();
            this.btnSearchDate = new MaterialSkin.Controls.MaterialButton();
            this.label1 = new System.Windows.Forms.Label();
            this.panel1 = new System.Windows.Forms.Panel();
            this.flowLayoutPanelRecommendations = new System.Windows.Forms.FlowLayoutPanel();
            this.label2 = new System.Windows.Forms.Label();
            this.btnBack = new System.Windows.Forms.Button();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // dateTimePicker
            // 
            this.dateTimePicker.Location = new System.Drawing.Point(28, 165);
            this.dateTimePicker.Name = "dateTimePicker";
            this.dateTimePicker.Size = new System.Drawing.Size(244, 22);
            this.dateTimePicker.TabIndex = 0;
            // 
            // flowLayoutPanel1
            // 
            this.flowLayoutPanel1.AutoScroll = true;
            this.flowLayoutPanel1.Location = new System.Drawing.Point(28, 212);
            this.flowLayoutPanel1.Name = "flowLayoutPanel1";
            this.flowLayoutPanel1.Size = new System.Drawing.Size(943, 561);
            this.flowLayoutPanel1.TabIndex = 9;
            // 
            // btnSearchCategoryKeyword
            // 
            this.btnSearchCategoryKeyword.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.btnSearchCategoryKeyword.BackColor = System.Drawing.SystemColors.ActiveCaption;
            this.btnSearchCategoryKeyword.Density = MaterialSkin.Controls.MaterialButton.MaterialButtonDensity.Default;
            this.btnSearchCategoryKeyword.Depth = 0;
            this.btnSearchCategoryKeyword.ForeColor = System.Drawing.SystemColors.ActiveCaption;
            this.btnSearchCategoryKeyword.HighEmphasis = true;
            this.btnSearchCategoryKeyword.Icon = null;
            this.btnSearchCategoryKeyword.Location = new System.Drawing.Point(721, 93);
            this.btnSearchCategoryKeyword.Margin = new System.Windows.Forms.Padding(4, 6, 4, 6);
            this.btnSearchCategoryKeyword.MouseState = MaterialSkin.MouseState.HOVER;
            this.btnSearchCategoryKeyword.Name = "btnSearchCategoryKeyword";
            this.btnSearchCategoryKeyword.NoAccentTextColor = System.Drawing.Color.Empty;
            this.btnSearchCategoryKeyword.Size = new System.Drawing.Size(78, 36);
            this.btnSearchCategoryKeyword.TabIndex = 10;
            this.btnSearchCategoryKeyword.Text = "Search";
            this.btnSearchCategoryKeyword.Type = MaterialSkin.Controls.MaterialButton.MaterialButtonType.Contained;
            this.btnSearchCategoryKeyword.UseAccentColor = false;
            this.btnSearchCategoryKeyword.UseVisualStyleBackColor = false;
            this.btnSearchCategoryKeyword.Click += new System.EventHandler(this.btnSearchCategoryKeyword_Click_1);
            // 
            // comboBoxCategory1
            // 
            this.comboBoxCategory1.AutoResize = false;
            this.comboBoxCategory1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(255)))), ((int)(((byte)(255)))));
            this.comboBoxCategory1.Depth = 0;
            this.comboBoxCategory1.DrawMode = System.Windows.Forms.DrawMode.OwnerDrawVariable;
            this.comboBoxCategory1.DropDownHeight = 174;
            this.comboBoxCategory1.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBoxCategory1.DropDownWidth = 121;
            this.comboBoxCategory1.Font = new System.Drawing.Font("Microsoft Sans Serif", 14F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Pixel);
            this.comboBoxCategory1.ForeColor = System.Drawing.Color.CornflowerBlue;
            this.comboBoxCategory1.FormattingEnabled = true;
            this.comboBoxCategory1.Hint = "Enter a Category";
            this.comboBoxCategory1.IntegralHeight = false;
            this.comboBoxCategory1.ItemHeight = 43;
            this.comboBoxCategory1.Location = new System.Drawing.Point(431, 78);
            this.comboBoxCategory1.MaxDropDownItems = 4;
            this.comboBoxCategory1.MouseState = MaterialSkin.MouseState.OUT;
            this.comboBoxCategory1.Name = "comboBoxCategory1";
            this.comboBoxCategory1.Size = new System.Drawing.Size(251, 49);
            this.comboBoxCategory1.StartIndex = 0;
            this.comboBoxCategory1.TabIndex = 11;
            // 
            // txtKeyword
            // 
            this.txtKeyword.AnimateReadOnly = false;
            this.txtKeyword.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtKeyword.Depth = 0;
            this.txtKeyword.Font = new System.Drawing.Font("Roboto", 16F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Pixel);
            this.txtKeyword.ForeColor = System.Drawing.Color.CornflowerBlue;
            this.txtKeyword.Hint = "Enter a Keyword";
            this.txtKeyword.LeadingIcon = null;
            this.txtKeyword.Location = new System.Drawing.Point(28, 80);
            this.txtKeyword.MaxLength = 50;
            this.txtKeyword.MouseState = MaterialSkin.MouseState.OUT;
            this.txtKeyword.Multiline = false;
            this.txtKeyword.Name = "txtKeyword";
            this.txtKeyword.Size = new System.Drawing.Size(378, 50);
            this.txtKeyword.TabIndex = 12;
            this.txtKeyword.Text = "";
            this.txtKeyword.TrailingIcon = null;
            // 
            // btnSearchDate
            // 
            this.btnSearchDate.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.btnSearchDate.Density = MaterialSkin.Controls.MaterialButton.MaterialButtonDensity.Default;
            this.btnSearchDate.Depth = 0;
            this.btnSearchDate.HighEmphasis = true;
            this.btnSearchDate.Icon = null;
            this.btnSearchDate.Location = new System.Drawing.Point(296, 160);
            this.btnSearchDate.Margin = new System.Windows.Forms.Padding(4, 6, 4, 6);
            this.btnSearchDate.MouseState = MaterialSkin.MouseState.HOVER;
            this.btnSearchDate.Name = "btnSearchDate";
            this.btnSearchDate.NoAccentTextColor = System.Drawing.Color.Empty;
            this.btnSearchDate.Size = new System.Drawing.Size(130, 36);
            this.btnSearchDate.TabIndex = 13;
            this.btnSearchDate.Text = "Filter by Date";
            this.btnSearchDate.Type = MaterialSkin.Controls.MaterialButton.MaterialButtonType.Contained;
            this.btnSearchDate.UseAccentColor = false;
            this.btnSearchDate.UseVisualStyleBackColor = true;
            this.btnSearchDate.Click += new System.EventHandler(this.btnSearchDate_Click_1);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Segoe UI", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.ForeColor = System.Drawing.SystemColors.Control;
            this.label1.Location = new System.Drawing.Point(457, 8);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(547, 38);
            this.label1.TabIndex = 14;
            this.label1.Text = "LOCAL EVENTS AND ANNOUNCEMENTS";
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.CornflowerBlue;
            this.panel1.Controls.Add(this.btnBack);
            this.panel1.Controls.Add(this.label1);
            this.panel1.Location = new System.Drawing.Point(-15, 1);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(1413, 51);
            this.panel1.TabIndex = 15;
            // 
            // flowLayoutPanelRecommendations
            // 
            this.flowLayoutPanelRecommendations.AutoScroll = true;
            this.flowLayoutPanelRecommendations.Location = new System.Drawing.Point(998, 212);
            this.flowLayoutPanelRecommendations.Name = "flowLayoutPanelRecommendations";
            this.flowLayoutPanelRecommendations.Size = new System.Drawing.Size(361, 561);
            this.flowLayoutPanelRecommendations.TabIndex = 16;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(1078, 174);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(210, 22);
            this.label2.TabIndex = 18;
            this.label2.Text = "RECOMMENDATIONS";
            // 
            // btnBack
            // 
            this.btnBack.BackColor = System.Drawing.Color.Red;
            this.btnBack.Location = new System.Drawing.Point(27, 11);
            this.btnBack.Name = "btnBack";
            this.btnBack.Size = new System.Drawing.Size(86, 32);
            this.btnBack.TabIndex = 15;
            this.btnBack.Text = "Back";
            this.btnBack.UseVisualStyleBackColor = false;
            this.btnBack.Click += new System.EventHandler(this.btnBack_Click);
            // 
            // LocalEvents
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoScroll = true;
            this.ClientSize = new System.Drawing.Size(1378, 797);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.flowLayoutPanelRecommendations);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.btnSearchDate);
            this.Controls.Add(this.txtKeyword);
            this.Controls.Add(this.comboBoxCategory1);
            this.Controls.Add(this.btnSearchCategoryKeyword);
            this.Controls.Add(this.flowLayoutPanel1);
            this.Controls.Add(this.dateTimePicker);
            this.Name = "LocalEvents";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "LocalEvents";
            this.Load += new System.EventHandler(this.LocalEvents_Load);
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DateTimePicker dateTimePicker;
        private System.Windows.Forms.FlowLayoutPanel flowLayoutPanel1;
        private MaterialSkin.Controls.MaterialButton btnSearchCategoryKeyword;
        private MaterialSkin.Controls.MaterialComboBox comboBoxCategory1;
        private MaterialSkin.Controls.MaterialTextBox txtKeyword;
        private MaterialSkin.Controls.MaterialButton btnSearchDate;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.FlowLayoutPanel flowLayoutPanelRecommendations;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnBack;
    }
}
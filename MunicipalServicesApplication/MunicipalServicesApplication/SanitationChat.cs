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
    public partial class SanitationChat : Form
    {
        private MessageLinkedList messageList;
        private int colorIndex = 0;

        public SanitationChat()
        {
            InitializeComponent();
            messageList = ChatManager.Instance.SanitationMessages;
            pnlChatWindow.AutoScroll = true;
            UpdateMessageDisplay();
        }

        private void btnSendMessage_Click(object sender, EventArgs e)
        {
            string newMessage = txtMessageInput.Text;
            if (!string.IsNullOrWhiteSpace(newMessage))
            {
                messageList.AddMessage(newMessage);
                UpdateMessageDisplay();
                txtMessageInput.Clear();
            }
        }

        private void UpdateMessageDisplay()
        {
            pnlChatWindow.Controls.Clear();
            List<Message> allMessages = messageList.GetAllMessages();
            int yOffset = 10;

            foreach (Message message in allMessages)
            {
                Label lblMessage = new Label
                {
                    Text = $"{message.UserName}: {message.Content}\n{message.Timestamp.ToShortTimeString()}",
                    AutoSize = true,
                    Padding = new Padding(10),
                    MaximumSize = new Size(pnlChatWindow.Width - 50, 0),
                    BorderStyle = BorderStyle.FixedSingle,
                    TextAlign = ContentAlignment.MiddleLeft,
                    BackColor = GetNextColor(),
                    Location = new Point(10, yOffset)
                };

                pnlChatWindow.Controls.Add(lblMessage);
                yOffset += lblMessage.Height + 10;
            }

            pnlChatWindow.VerticalScroll.Value = pnlChatWindow.VerticalScroll.Maximum;
        }

        private Color GetNextColor()
        {
            Color[] colors = { Color.LightBlue, Color.LightGreen, Color.LightCoral, Color.LightYellow, Color.MediumPurple };
            Color color = colors[colorIndex];
            colorIndex = (colorIndex + 1) % colors.Length;
            return color;
        }

        private void btnClose_Click(object sender, EventArgs e)
        {
            Form1 form1 = new Form1();
            form1.Show();
            this.Close();
        }
    }
}
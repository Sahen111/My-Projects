using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MunicipalServicesApplication
{
    public class Message
    {
        public string UserName { get; set; }
        public string Content { get; set; }
        public DateTime Timestamp { get; set; }
        public Message Next { get; set; }

        public Message(string content)
        {
            UserName = "Anonymous";
            Content = content;
            Timestamp = DateTime.Now;
            Next = null;
        }
    }
}

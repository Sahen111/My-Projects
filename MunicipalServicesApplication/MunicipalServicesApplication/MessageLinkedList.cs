using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MunicipalServicesApplication
{
    public class MessageLinkedList
    {
        private Message head;

        public MessageLinkedList()
        {
            head = null;
        }

        public void AddMessage(string content)
        {
            Message newMessage = new Message(content);
            if (head == null)
            {
                head = newMessage;
            }
            else
            {
                Message current = head;
                while (current.Next != null)
                {
                    current = current.Next;
                }
                current.Next = newMessage;
            }
        }

        public List<Message> GetAllMessages()
        {
            List<Message> messages = new List<Message>();
            Message current = head;
            while (current != null)
            {
                messages.Add(current);
                current = current.Next;
            }
            return messages;
        }
    }
}

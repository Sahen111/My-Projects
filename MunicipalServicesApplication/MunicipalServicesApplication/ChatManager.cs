using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MunicipalServicesApplication
{
    public class ChatManager
    {
        // Singleton instance
        private static ChatManager _instance;
        public static ChatManager Instance => _instance ?? (_instance = new ChatManager());

        // Message lists for each category
        public MessageLinkedList SanitationMessages { get; private set; }
        public MessageLinkedList RoadsMessages { get; private set; }
        public MessageLinkedList UtilitiesMessages { get; private set; }
        public MessageLinkedList OtherMessages { get; private set; }

        private ChatManager()
        {
            SanitationMessages = new MessageLinkedList();
            RoadsMessages = new MessageLinkedList();
            UtilitiesMessages = new MessageLinkedList();
            OtherMessages = new MessageLinkedList();
        }
    }
}

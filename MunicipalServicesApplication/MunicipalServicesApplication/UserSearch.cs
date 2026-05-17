using System;

namespace MunicipalServicesApplication
{
    public class UserSearch
    {
        public string Category { get; set; } // Category of the event
        public string Keyword { get; set; }  // Search keyword
        public DateTime? Date { get; set; }  // Date of the search (optional)
    }

}
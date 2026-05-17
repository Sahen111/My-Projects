using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HashTableAdv
{
    internal class customNode
    {
        public customNode(int key, string value)
        {
            this.key = key;
            this.value = value;
        }

        public int key {  get; set; }
        public string value { get; set; }

        //output
        public override string ToString()
        {
            return "(" + key + " - " + value + ")";
        }

    }
}

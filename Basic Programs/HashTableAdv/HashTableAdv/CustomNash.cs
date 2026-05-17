using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HashTableAdv
{
    internal class CustomNash
    {
        // class that brings in the nodes
        // hash function
        // computations

        // 1 gethash method
        // 2 add method
        // 3 remove method

        customNode[] hashdata = new customNode[9];

        //1 gethash method
        public int GetHashdata(int key)
        {
            return key % hashdata.Length;
        }

        // 2 add method --> NB key, value
        public void Add(int key, string value)
        {
            //we don't know WHERE
            hashdata[GetHashdata(key)] = new customNode(key, value);
        }

        // 3 remove method
        public void Remove (int key)
        {
            hashdata[GetHashdata(key)] = null;
        }

        //fetch the data
        public string GetHashtable()
        {
            string finalOutput = "";

            for (int i = 0; i < hashdata.Length; i++)
            {
                if(hashdata[i] != null)
                {
                    finalOutput += hashdata[i].ToString() + "\t";
                }
                else
                {
                    //if there's an empty value --> *****
                    finalOutput += "*\t";
                }
            }
            return finalOutput;
        }
    }
}

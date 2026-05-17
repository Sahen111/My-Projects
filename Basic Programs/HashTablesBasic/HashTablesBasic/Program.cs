using System.Collections;

namespace HashTablesBasic
{
    internal class Program
    {
        static void Main(string[] args)
        {
            //create an obj of HT
            Hashtable ht = new Hashtable();
            //addng elements onto it --> .add method
            // {key ; value}
            ht.Add("1", "Jake");
            ht.Add("2", "Sally");
            ht.Add("3", "Henry");

            //print out --> Interface IEnumerables / Icollection / Dictionary

            ICollection keys = ht.Keys;

            foreach (String k in keys)
            {
                Console.WriteLine(k + " " + ht[k]);
            }

            Console.ReadLine();
        }
    }
}

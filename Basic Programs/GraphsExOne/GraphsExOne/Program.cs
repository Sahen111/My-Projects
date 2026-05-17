using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GraphsExOne
{
    internal class Program
    {
        static void Main(string[] args)
        {
            //pull the graph class
            Graph gp = new Graph(4);
            //use the addedges method
            gp.AddEdge(0, 1);
            gp.AddEdge(0, 2);
            gp.AddEdge(0, 3);
            gp.AddEdge(1, 2);

            Console.WriteLine("Adj list -- graph eg 1");
            gp.Display();

            Console.ReadLine();
        }
    }
}

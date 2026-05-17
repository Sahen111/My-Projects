using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GraphsExOne
{
    public class Graph
    {

        //linked list
        LinkedList<int>[] adjList;

        //ctor
        public Graph(int v)
        {
            adjList = new LinkedList<int>[v];

            for (int i = 0; i < v; i++)
            {
                adjList[i] = new LinkedList<int>();
            }
        }

        //method to handle bidirectional nodes on the graph

        public void AddEdge(int u, int v, bool isBidirectional = true)
        {
            adjList[u].AddLast(v);
            if (isBidirectional)
            {
                adjList[v].AddLast(u);
            }
        }

        //method to display
        public void Display()
        {
            for (int i = 0;i < adjList.Length;i++)
            {
                Console.WriteLine($"{i}");
                foreach (var neighbor in adjList[i])
                {
                    Console.WriteLine($"--{neighbor}");
                }
            }
        }

    }
}

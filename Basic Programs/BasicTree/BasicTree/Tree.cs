using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BasicTree
{
    class Tree<T>
    {
        //set the root
        public TreeNode<T> Root { get; set; }

        //handle the output -- optional
        public void PrintTree(TreeNode<T> node, string indent = "",
            string branch = "")
        {
            if (node == null) return;

            //print out the node
            Console.WriteLine($"{indent}{branch}{node.Data}");

            //iteration
            if(node.Children != null)
            {
                for (int i = 0; i < node.Children.Count; i++)
                {
                    //NB edges --> N -1
                    // print in a hierarchical manner
                    string childrenBranch = (i == node.Children.Count - 1) ? "|_": "|--";
                    PrintTree(node.Children[i], indent + (branch == "" ? "" : "  "), childrenBranch);
                }
            }
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BasicTreesTwo
{
    public class Tree <T>
    {
        public TreeNode<T> Root { get; set; }

        public void PrintTree(TreeNode<T> node, string indent = "", bool isLast = true)
        {
            if (node == null)
                return;

            //Print the current node with apprpriate indentation
            Console.WriteLine(indent + (isLast ? "|___ " : "|---" + node.Data));

            //Adjust the indentationfor children nodes
            indent = isLast ? "   " : "|  ";

            //print each child node
            for (int i = 0; i < node.Children.Count; i++)
            {
                PrintTree(node.Children[i], indent, i == node.Children.Count - 1);
            }
        }

    }
}

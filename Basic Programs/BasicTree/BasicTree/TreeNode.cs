using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BasicTree
{
    class TreeNode <T>

        //<T> --> means --> Generic data type
    {
        // data //parent //children

        public T Data { get; set; }
        public TreeNode<T> Parent { get; set; }
        //hold a list of child nodes to the tree
        public List<TreeNode<T>> Children { get; set; }

        //set the base height and epth
        public int GetHeight()
        {
            int height = 1;
            TreeNode<T> current = this;

            while (current.Parent != null)
            {
                height++;
                current = current.Parent;
            }
            return height;
        }
    }
}

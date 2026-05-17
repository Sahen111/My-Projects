using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BasicTreesTwo
{
    public class TreeNode<T>
    {
        public T Data { get; set; }
        public TreeNode<T> Parent { get; set; }
        public List<TreeNode<T>> Children { get; set; }

        //set the height and depth of the tree
        public int GetHeight()
        {
            int height = 1;
            TreeNode<T> current = this;

            while (current != null)
            {
                height++;
                current = current.Parent;
            }

            return height;
        }
    }
}

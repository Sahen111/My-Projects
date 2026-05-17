namespace BasicTree
{
    class Program
    {
        static void Main(string[] args)
        {
            //now pull it all together

            Tree<int> tree = new Tree<int>();
            //give the root a value
            tree.Root = new TreeNode<int>() { Data = 100 };
            //now the child nodes 50 1 150
            tree.Root.Children = new List<TreeNode<int>>
            {
                new TreeNode<int>() { Data = 50, Parent = tree.Root },
                new TreeNode<int>() {Data = 1, Parent = tree.Root },
                new TreeNode<int>() {Data = 150, Parent = tree.Root }
            };

            //testing
            //now the subtree
            tree.Root.Children[2].Children = new List<TreeNode<int>>()
            {
                new TreeNode<int>() {Data =30, Parent = tree.Root.Children[2]},
                new TreeNode<int>() {Data =8, Parent = tree.Root.Children[2]}
            };

            tree.PrintTree(tree.Root);


            Console.ReadLine();
        }
    }
}

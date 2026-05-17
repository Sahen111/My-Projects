namespace BasicTreesTwo
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Tree<String> company = new Tree<string>
            {
                Root = new TreeNode<string> { Data = "CEO" }
            };

            //Now init the child nodes
            company.Root.Children = new List<TreeNode<string>>
            {
                new TreeNode<string> {Data = "Manager A", Parent = company.Root },
                new TreeNode<string> {Data = "Manager B", Parent = company.Root }
            };

            //Add the subnodes
            TreeNode<string> managerA = company.Root.Children.Find(node => node.Data == "Manager A");

            if (managerA != null)
            {
                managerA.Children = new List<TreeNode<string>>
                {
                    new TreeNode<string> { Data = "Supervisor A1", Parent = managerA },
                    new TreeNode<string> { Data = "Supervisor A2", Parent = managerA }
                };
            }
            //Sub nodes to supervisor A
            TreeNode<string> supervisorA1 = managerA.Children.Find(node => node.Data == "Supervisor A1");
                if (supervisorA1 != null)
            {
                supervisorA1.Children = new List<TreeNode<string>>
                {
                    new TreeNode<string> { Data = "Liner Manager A1-1", Parent = supervisorA1 },
                    new TreeNode<string> { Data = "Liner Manager A1-2", Parent = supervisorA1 }
                };

                //sub nodes to Line manager a1-1
                TreeNode<string> linemanagerA1_1 = supervisorA1.Children.Find(node => node.Data == "Line Manager A1");
                if (supervisorA1 != null)
                {
                    supervisorA1.Children = new List<TreeNode<string>>
                {
                    new TreeNode<string> { Data = "Coordinator A1-1-1", Parent = linemanagerA1_1 },
                    new TreeNode<string> { Data = "Coordinator A1-1-2", Parent = linemanagerA1_1 }
                };
                }
                company.PrintTree(company.Root);
            }
                Console.ReadLine();
        }
    }
}

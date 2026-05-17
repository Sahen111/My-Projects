using System.Reflection;

namespace ReflectionsOne
{
    internal class Program
    {
        static string path = "C:\\Users\\sahen\\Desktop\\dll\\CarLibrary.dll";
        static void Main(string[] args)
        {
            /* Prog to fetch metadata from an assembly
                1 - load the file
                2 - all built in types
                2 - Analyze the meta data
                    - assembly -- get types
            */

            Assembly assembly = Assembly.LoadFile(path);
            Type[] types = assembly.GetTypes();
            
            foreach (var type in types)
            {
                Console.WriteLine("TYPE : " + type.Name);
                //method info
                MethodInfo[] methods = type.GetMethods();
                foreach (var method in methods)
                {
                    Console.WriteLine("-----Method : " + method.Name);
                    //parameters
                    ParameterInfo[] parameters = method.GetParameters();

                    foreach (var param in parameters)
                    {
                        Console.WriteLine("----Parameters : " + param.Name +
                            ", Type " + param.ParameterType);
                    }
                }
            }
            Console.ReadLine();
        }
    }
}

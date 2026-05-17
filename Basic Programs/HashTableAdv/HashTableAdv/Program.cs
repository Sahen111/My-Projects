namespace HashTableAdv
{
    internal class Program
    {
        static void Main(string[] args)
        {

            CustomNash ch = new CustomNash();
            //add values
            ch.Add(56581, "Nashy");
            ch.Add(56484, "Xavier");
            ch.Add(56393, "Mikey");

            Console.WriteLine(ch.GetHashtable());

            Console.ReadLine();
        }
    }
}

namespace RomanToInteger
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // Initialize the dictionary to map Roman numerals to integer values
            Dictionary<char, int> romanMap = new Dictionary<char, int>
            {
                {'I', 1},
                {'V', 5},
                {'X', 10},
                {'L', 50},
                {'C', 100},
                {'D', 500},
                {'M', 1000}
            };

            int Ans = 0;

            Console.WriteLine("Please Enter Roman Numerals to Convert to an Integer");
            string s = Console.ReadLine().ToUpper(); // Convert input to uppercase to handle case sensitivity

            for (int i = 0; i < s.Length; i++)
            {
                // Check if the current character has a smaller value than the next character
                if (i + 1 < s.Length && romanMap[s[i]] < romanMap[s[i + 1]])
                {
                    Ans -= romanMap[s[i]]; // Subtract the current character's value
                }
                else
                {
                    Ans += romanMap[s[i]]; // Add the current character's value
                }
            }

            Console.WriteLine($"The integer value of the Roman numeral {s} is: {Ans}");
        }
    }

}

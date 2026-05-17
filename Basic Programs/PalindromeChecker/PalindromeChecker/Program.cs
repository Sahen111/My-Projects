namespace PalindromeChecker
{
    internal class Program
    {
        static void Main(string[] args)
        {
            int revNum = 0;

            Console.WriteLine("Please Enter a number");
            int Number = int.Parse(Console.ReadLine());
            int originalNum = Number;
            while (Number != 0)
            {
                {
                    revNum = revNum * 10 + Number % 10;
                    Number /= 10;
                }
            }

                if (originalNum == revNum)
                {
                    Console.WriteLine($"{originalNum} is a palindrome");
                }
                else
                {
                    Console.WriteLine($"{originalNum} is not a palindrome");
                }
            }
        }
    }

namespace TripletChecker
{
    internal class Program
    {
        static void Main(string[] args)
        {
            int[] nums;
            while (true)
            {
                Console.WriteLine("Please enter either 3, 6, or 9 numbers separated by spaces:");
                string input = Console.ReadLine();

                string[] inputs = input.Split(' ');
                if (inputs.Length == 3 || inputs.Length == 6 || inputs.Length == 9)
                {
                    nums = new int[inputs.Length];
                    for (int i = 0; i < inputs.Length; i++)
                    {
                        nums[i] = int.Parse(inputs[i]);
                    }
                    break;
                }
                else
                {
                    Console.WriteLine("Invalid input. You must enter exactly 3, 6, or 9 numbers.");
                }
            }

            var result = ThreeSum(nums);

            Console.WriteLine("Output:");
            foreach (var triplet in result)
            {
                Console.WriteLine($"[{string.Join(",", triplet)}]");
            }
        }

        static IList<IList<int>> ThreeSum(int[] nums)
        {
            IList<IList<int>> result = new List<IList<int>>();

            // Sort the array to use two-pointer technique and to avoid duplicates
            Array.Sort(nums);

            for (int i = 0; i < nums.Length - 2; i++)
            {
                // Skip duplicate elements
                if (i > 0 && nums[i] == nums[i - 1])
                {
                    continue;
                }

                int left = i + 1;
                int right = nums.Length - 1;

                while (left < right)
                {
                    int sum = nums[i] + nums[left] + nums[right];

                    if (sum == 0)
                    {
                        // Found a valid triplet
                        result.Add(new List<int> { nums[i], nums[left], nums[right] });

                        // Skip duplicate elements for left and right pointers
                        while (left < right && nums[left] == nums[left + 1])
                        {
                            left++;
                        }
                        while (left < right && nums[right] == nums[right - 1])
                        {
                            right--;
                        }

                        left++;
                        right--;
                    }
                    else if (sum < 0)
                    {
                        // Increase the sum by moving the left pointer to the right
                        left++;
                    }
                    else
                    {
                        // Decrease the sum by moving the right pointer to the left
                        right--;
                    }
                }
            }

            return result;
        }
    }
}
using System.Collections.Generic;
using System;

namespace MunicipalServicesApplication
{
    public class PriorityQueue<TElement, TPriority> where TPriority : IComparable<TPriority>
    {
        private SortedDictionary<TPriority, Queue<TElement>> storage = new SortedDictionary<TPriority, Queue<TElement>>();

        public void Enqueue(TElement element, TPriority priority)
        {
            if (!storage.ContainsKey(priority))
            {
                storage[priority] = new Queue<TElement>();
            }
            storage[priority].Enqueue(element);
        }

        public TElement Dequeue()
        {
            if (storage.Count == 0)
            {
                throw new InvalidOperationException("The queue is empty");
            }

            var firstKey = FirstKey();
            var queue = storage[firstKey];

            var item = queue.Dequeue();
            if (queue.Count == 0)
            {
                storage.Remove(firstKey);
            }

            return item;
        }

        public int Count
        {
            get
            {
                int count = 0;
                foreach (var queue in storage.Values)
                {
                    count += queue.Count;
                }
                return count;
            }
        }

        private TPriority FirstKey()
        {
            foreach (var key in storage.Keys)
            {
                return key;
            }
            throw new InvalidOperationException("No elements in the queue");
        }
    }
}
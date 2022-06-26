/* *****************************************************************************
 *  Name: QiuYuxuan
 *  Date: 2022/06/04
 *  Description: Implements a randomized queue using arrays
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;

    private int capacity;

    private int queueSize;

    // construct an empty randomized queue
    public RandomizedQueue() {
        int originalSize = 8;
        items = (Item[]) new Object[originalSize];
        capacity = originalSize;
        queueSize = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return queueSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queueSize;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        int enlargeRatio = 2;
        if (queueSize == capacity) {
            resize(enlargeRatio * capacity);
        }
        items[queueSize++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (queueSize == 0) {
            throw new NoSuchElementException();
        }
        double halveThreshold = 0.25;
        int index = StdRandom.uniform(queueSize);
        Item value = items[index];
        items[index] = items[queueSize - 1];
        items[queueSize - 1] = null;
        --queueSize;
        if (queueSize > 0 && queueSize * 1.0 / capacity <= halveThreshold) {
            resize(capacity / 2);
        }
        return value;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (queueSize == 0) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(queueSize)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // resize the array
    private void resize(int size) {
        Item[] newItems = (Item[]) new Object[size];
        for (int i = 0; i < queueSize; ++i) {
            newItems[i] = items[i];
        }
        items = newItems;
        capacity = size;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final Item[] randomItems;

        private int index;

        public RandomizedQueueIterator() {
            randomItems = (Item[]) new Object[queueSize];
            for (int i = 0; i < queueSize; ++i) {
                randomItems[i] = items[i];
            }
            StdRandom.shuffle(randomItems);
            index = 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return index != queueSize;
        }

        @Override
        public Item next() {
            if (index == queueSize) {
                throw new NoSuchElementException();
            }
            return randomItems[index++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        System.out.println("Is queue emtpy? " + queue.isEmpty());
        System.out.println("Queue size: " + queue.size());
        System.out.println("-----------");
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        queue.enqueue("G");
        queue.enqueue("H");
        Iterator<String> queueIterator = queue.iterator();
        while (queueIterator.hasNext()) {
            System.out.println(queueIterator.next());
        }
        System.out.println("Is queue emtpy? " + queue.isEmpty());
        System.out.println("Queue size: " + queue.size());
        queueIterator = queue.iterator();
        while (queueIterator.hasNext()) {
            System.out.println(queueIterator.next());
        }
        System.out.println("sample: " + queue.sample());
        System.out.println("-----------");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        System.out.println("Is queue emtpy? " + queue.isEmpty());
        System.out.println("Queue size: " + queue.size());
        queueIterator = queue.iterator();
        while (queueIterator.hasNext()) {
            System.out.println(queueIterator.next());
        }
        System.out.println("sample: " + queue.sample());
    }
}

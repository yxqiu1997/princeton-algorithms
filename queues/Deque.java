/* *****************************************************************************
 *  Name: QiuYuxuan
 *  Date: 2022/06/04
 *  Description: Implements a deque using linked lists
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>  {

    private int size;

    private final Node headSentinel;

    private final Node tailSentinel;

    // construct an empty deque
    public Deque() {
        headSentinel = new Node(null, null, null);
        tailSentinel = new Node(null, null, null);
        headSentinel.next = tailSentinel;
        tailSentinel.prev = headSentinel;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newFirst = new Node(item, headSentinel, headSentinel.next);
        headSentinel.next.prev = newFirst;
        headSentinel.next = newFirst;
        ++size;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newLast = new Node(item, tailSentinel.prev, tailSentinel);
        tailSentinel.prev.next = newLast;
        tailSentinel.prev = newLast;
        ++size;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item value = headSentinel.next.value;
        headSentinel.next.next.prev = headSentinel;
        headSentinel.next = headSentinel.next.next;
        --size;
        return value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item value = tailSentinel.prev.value;
        tailSentinel.prev.prev.next = tailSentinel;
        tailSentinel.prev = tailSentinel.prev.prev;
        --size;
        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeItereator();
    }

    private class Node {
        private final Item value;
        private Node prev;
        private Node next;

        public Node(Item value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private class DequeItereator implements Iterator<Item> {

        private Node current;

        public DequeItereator() {
            current = headSentinel.next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return current != tailSentinel;
        }

        @Override
        public Item next() {
            if (current == tailSentinel) {
                throw new NoSuchElementException();
            }
            Item value = current.value;
            current = current.next;
            return value;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        System.out.println("Is deque emtpy? " + deque.isEmpty());
        System.out.println("Deque size: " + deque.size);
        System.out.println("-----------");
        deque.addFirst("C");
        deque.addFirst("B");
        deque.addFirst("A");
        Iterator<String> dequeIterator = deque.iterator();
        while (dequeIterator.hasNext()) {
            System.out.println(dequeIterator.next());
        }
        System.out.println("Is deque emtpy? " + deque.isEmpty());
        System.out.println("Deque size: " + deque.size);
        System.out.println("-----------");
        deque.addLast("D");
        deque.addLast("E");
        deque.addLast("F");
        dequeIterator = deque.iterator();
        while (dequeIterator.hasNext()) {
            System.out.println(dequeIterator.next());
        }
        System.out.println("Is deque emtpy? " + deque.isEmpty());
        System.out.println("Deque size: " + deque.size);
        System.out.println("-----------");
        deque.removeFirst();
        deque.removeLast();
        dequeIterator = deque.iterator();
        while (dequeIterator.hasNext()) {
            System.out.println(dequeIterator.next());
        }
        System.out.println("Is deque emtpy? " + deque.isEmpty());
        System.out.println("Deque size: " + deque.size);
    }

}

/* *****************************************************************************
 *  Name: Qiu Yuxuan
 *  Date: 2022/06/04
 *  Description: A client program
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid arguments!");
        }
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        int k = Integer.parseInt(args[0]);
        for (int i = 0; i < k; ++i) {
            System.out.println(queue.dequeue());
        }
    }
}

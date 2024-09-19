package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private Node sent;
    private int size;
    private static class Node<T> {
        T i;
        Node pre;
        Node next;
        Node(Node pre, T i, Node next) {
            this.i = i;
            this.pre = pre;
            this.next = next;
        }
    }
    public LinkedListDeque() {
        sent = new Node(null, null, null);
        sent.pre = sent;
        sent.next = sent;
        size = 0;
    }
    @Override
    public void addFirst(T x) { // @Override
        Node xnode = new Node(sent, x, sent.next);
        sent.next.pre = xnode;
        sent.next = xnode;
        size++;
    }
    @Override
    public void addLast(T y) { //@Override
        Node ynode = new Node(sent.pre, y, sent);
        sent.pre.next = ynode;
        sent.pre = ynode;
        size++;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() { // @Override
        Node test = sent;
        while (size > 0) {
            System.out.print(sent.next.i + " ");
            test = test.next;
            size--;
        }
    }
    @Override
    public T removeFirst() { // @Override
        if (isEmpty()) {
            return null;
        }
        T x = (T) sent.next.i;
        Node newNode = sent.next.next;
        newNode.pre = sent;
        sent.next = newNode;
        size--;
        return x;
    }
    @Override
    public T removeLast() { // @Override
        if (isEmpty()) {
            return null;
        }
        T x = (T) sent.pre.i;
        Node newNode = sent.pre.pre;
        newNode.next = sent;
        sent.pre = newNode;
        size--;
        return x;
    }
    @Override
    public T get(int index) { // @Override
        Node test = sent.next;
        if (index < 0 || index > size) {
            return null;
        }
        while (index > 0) {
            test = test.next;
            index--;
        }
        return (T) test.i;
    }
    private Node recursiveGet(Node n, int index) {
        if (index == 0) {
            return n.next;
        }
        return recursiveGet(n.next, index - 1);
    }
    public T getRecursive(int index) {
        if (index > size - 1) {
            return null;
        }
        return (T) recursiveGet(sent, index).i;
    }
    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }
    private class LLDequeIterator implements Iterator<T> {
        private int pos;
        private Node n = sent;
        LLDequeIterator() {
            pos = 0;
        }
        public boolean hasNext() {
            return pos < size;
        }
        public T next() {
            T result = (T) n.next.i;
            pos += 1;
            n = n.next;
            return result;
        }
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> n = (Deque<T>) o;
        if (n.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (!n.get(i).equals(this.get(i))) {
                return false;
            }
        }
        return true;
    }
}

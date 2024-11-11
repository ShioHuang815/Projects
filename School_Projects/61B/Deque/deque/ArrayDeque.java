package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] T;
    private int size;
    private int nextLast;
    private int nextFirst;

    public ArrayDeque() {
        T = (T[]) new Object[8];
        size = 0;
        nextLast = 5;
        nextFirst = 4;
    }
    private int plus(int i) {
        if (i + 1 > T.length - 1) {
            return 0;
        }
        return i + 1;
    }
    private int minus(int i) {
        if (i - 1 < 0) {
            return T.length - 1;
        }
        return i - 1;
    }
    private void resize(int newSize) {
        T[] newI = (T[]) new Object[newSize];
        int cur = plus(nextFirst);
        for (int count = 0; count < size; count++) {
            newI[count] = T[cur];
            cur = plus(cur);
        }
        T = newI;
        nextFirst = T.length - 1;
        nextLast = size;
    }
    @Override
    public void addFirst(T n) { // @Override
        if (size == T.length) {
            resize(2 * T.length);
        }
        T[nextFirst] = n;
        nextFirst = minus(nextFirst);
        size++;
    }
    @Override
    public void addLast(T n) { // @Override
        if (size == T.length) {
            resize(2 * T.length);
        }
        T[nextLast] = n;
        nextLast = plus(nextLast);
        size++;
    }
    @Override
    public int size() { // @Override
        return size;
    }
    @Override
    public void printDeque() { // @Override
        int temp = nextFirst + 1;
        while (temp != nextLast) {
            System.out.print(T[temp] + " ");
            temp = plus(temp);
        }
    }

    private boolean checker() {
        double rate = (double) size / T.length;
        return T.length >= 16 && rate < 0.25;
    }

    @Override
    public T removeFirst() { // @Override
        if (size == 0) {
            return null;
        }
        int cur = plus(nextFirst);
        T remove = T[cur];
        T[cur] = null;
        nextFirst = cur;
        size--;
        if (checker()) {
            resize(T.length / 2);
        }
        return remove;
    }
    @Override
    public T removeLast() { // @Override
        if (size == 0) {
            return null;
        }
        int cur = minus(nextLast);
        T remove = T[cur];
        T[cur] = null;
        nextLast = cur;
        size--;
        if (checker()) {
            resize(T.length / 2);
        }
        return remove;
    }
    @Override
    public T get(int index) { // @Override
        if (index >= size || index < 0) {
            return null;
        }
        int cur = nextFirst;
        while (index > -1) {
            cur = plus(cur);
            index--;
        }
        return T[cur];
    }
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;
        int i = size;

        ArrayDequeIterator() {
            pos = plus(nextFirst);
        }
        public boolean hasNext() {
            return i > 0;
        }
        public T next() {
            T result = T[pos];
            pos = plus(pos);
            i--;
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

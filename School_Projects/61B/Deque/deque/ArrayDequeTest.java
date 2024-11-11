package deque;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;;
public class ArrayDequeTest {
    public static void main(String[] args) {
        Testadd();
     }
    public static void Testadd(){
        ArrayDeque a = new ArrayDeque();
        a.addLast(0);
        a.addLast(1);
        Iterator n = a.iterator();
        System.out.print(a.get(1));
        }
}

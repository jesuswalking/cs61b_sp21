package deque;

import org.junit.Test;
import static org.junit.Assert.*;

/** Performs some basic AList tests. */
public class ArrayDequeTest {
	
	@Test
	public void addFirstTest() {
		ArrayDeque<Integer> ad = new ArrayDeque<>();

		ad.addFirst(0);
		
		assertEquals((Integer)0, ad.get(0));

		ad.addFirst(1);

		assertEquals((Integer)1, ad.get(0));
		
	}

	@Test
	public void printTest() {
		ArrayDeque<Integer> pr = new ArrayDeque<>();

		for (int i = 0; i < 10; i ++) {
			pr.addLast(i);
		}

		pr.printDeque();
	}

	@Test
	public void finalTest() {
		ArrayDeque<Integer> fl = new ArrayDeque<>();

		for (int i = 0; i < 99; i ++) {
			fl.addLast(i);
		}

		for (int i = 0; i < 99; i ++) {
			fl.addFirst(i);
		}

		fl.printDeque();

		for (int i = 0; i < 198; i ++) {
			fl.removeLast();
		}

		fl.printDeque();
		System.out.println(fl.size());
	}
}
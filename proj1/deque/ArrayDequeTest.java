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
}
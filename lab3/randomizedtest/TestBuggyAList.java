package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

  @Test
  public void testThreeAddThreeRemove() {
    AListNoResizing<Integer> ok = new AListNoResizing<>();
    BuggyAList<Integer> suspect = new BuggyAList<>();

    ok.addLast(1);
    ok.addLast(2);
    ok.addLast(3);
    suspect.addLast(1);
    suspect.addLast(2);
    suspect.addLast(3);

    assertEquals(ok.removeLast(), suspect.removeLast());
    assertEquals(ok.removeLast(), suspect.removeLast());
    assertEquals(ok.removeLast(), suspect.removeLast());
  }
}

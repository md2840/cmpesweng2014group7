import static org.junit.Assert.*;

import org.junit.Test;


public class MainTest {

	Main tested = new Main();
	
	@Test
	public void testDivision(){
		assertEquals("10 over 5 must be 2",2,tested.divide(10, 5));
	}
	

}

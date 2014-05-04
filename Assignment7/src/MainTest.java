import static org.junit.Assert.*;

import org.junit.Test;


public class MainTest {

	Main tested = new Main();
	
	
	/**Writer: Setenay Ronael*/
	@Test
	public void testDivision(){
		assertEquals("10 over 5 must be 2",2,tested.divide(10, 5));
	}
	
	/**Writer: Admir Nurkovic*/
	@Test
	public void testSubtraction(){
		assertEquals("20 - 15 must be 5",5,tested.subtract(20, 15));
	}
	

}

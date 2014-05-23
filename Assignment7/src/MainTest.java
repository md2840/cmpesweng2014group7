import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class which tests whether methods return expected correct value or not.
*/
public class MainTest {

	Main tested = new Main();
	
	
	/**
	* @author Setenay Ronael
	* @see divide( a, b )
	* @return expected value and value which is returned by divide method
	*/
	@Test
	public void testDivision(){
		assertEquals("10 over 5 must be 2",2,tested.divide(10, 5));
	}
	
	/**
	* @author Admir Nurkovic
	* @see subtract( a, b )
	* @return expected value and value which is returned by subtract method
	*/
	@Test
	public void testSubtraction(){
		assertEquals("20 - 15 must be 5",5,tested.subtract(20, 15));
	}
	
	/**
	* @author Mehmet Yasin AKPINAR
	* @see sum( a, b )
	* @return expected value and value which is returned by sum method
	*/
	@Test
	public void testSummation(){
		assertEquals("10 plus 5 must be 15",15,tested.sum(10, 5));
	}
	
	/**
	* @author Mustafa Demirel
	* @see multiply( a, b )
	* @return expected value and value which is returned by multiply method
	*/
	@Test
	public void testMultiply(){
		assertEquals("5 times 2 must be 10",10,tested.multiply(5, 2));
	}
	
	/**
	* @author Gokce Yesiltas
	* @see power( a, b )
	* @return expected value and value which is returned by power method
	*/
	@Test
	public void testPower(){
		assertEquals("2 to 5 must be 32",32,tested.power(2, 5));
	}
	
	/**
	* @author Cafer Tayyar Yoruk
	* @see remainder( a, b )
	* @return expected value and value which is returned by remainder method
	*/
	@Test
	public void testRemainder(){
		assertEquals("3 over 2 must be 1",1,tested.remainder(3, 2));
	}
	
	/**
	* @author Mehmet Yasin AKPINAR
	* @see logarithm( a )
	* @return expected value and value which is returned by logarithm method
	*/
	@Test
	public void testLogarithm(){
		assertEquals("Natural logarithm of 1 must be 0",0.0,tested.logarithm(1),0.01);
	}
}

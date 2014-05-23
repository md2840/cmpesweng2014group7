import java.util.Random;

/**
 *	Main class
 */

public class Main {
	
	public static void main(String args[]) {
		
		Random rand = new Random();
		
		int choice = rand.nextInt(8);
		
		
	}
	
	/**
	* @author Setenay Ronael
	* @param a the first argument.
	* @param b the second argument.
	* @return sum of a and b
	*/
	public static int sum(int a, int b){
		return a+b;
	}
	
	/**
	* @author Gokce Yesiltas
	* @param a the first argument.
	* @param b the second argument.
	* @return a divided by b
	*/
	public static int divide(int a, int b){
		return a/b;
	}
	
	/**
	* @author Cafer Tayyar Yoruk
	* @param a the first argument.
	* @param b the second argument.
	* @return multiplication of a and b
	*/
	public static int multiply(int a, int b){
		return a * b;
	}
	
	/**
	* @author Mehmet Yasin AKPINAR
	* @param a the first argument.
	* @param b the second argument.
	* @return subtraction of a and b
	*/
	public static int subtract(int a, int b){
		return a - b;
	}
	/**
	* @author Mustafa Demirel
	* @param a the first argument.
	* @param b the second argument.
	* @return b power of a
	*/
	public static double power(int a, int b){
		return Math.pow(a, b);
	}

	/**
	* @author Admir Nurkovic
	* @param a the first argument.
	* @param b the second argument.
	* @return remained value when a is divided by b
	*/
	public static int remainder(int a, int b){
		return a%b;
	}
	
	/**
	* @author: Fatih Cataltepe 
	* @param a the first argument.
	* @param b the second argument.
	* @return logarithm of a
	*/
	public static double logarithm(double a){
		return Math.log(a);
	}
}

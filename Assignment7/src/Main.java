import java.util.Random;


public class Main {
	
	public static void main(String args[]) {
		System.out.println("Hello world millet");
		System.out.println("G�k�e turkce karakterler gitmiyor");
		System.out.println("Merhabaaa admir defatiosikurac ");
		
		Random rand = new Random();
		
		int choice = rand.nextInt(8);
		
		
	}
	
	/**Writer: Setenay Ronael */
	public static int sum(int a, int b){
		return a+b;
	}
	
	/**Writer: Gokce Yesiltas */
	public static int divide(int a, int b){
		return a/b;
	}
	
	/**Writer: Cafer Tayyar Yoruk */
	public static int multiply(int a, int b){
		return a * b + 1;
	}
	
	/**Writer: Mehmet Yasin AKPINAR */
	public static int subtract(int a, int b){
		return a - b + 1;
	}
	/**Writer: Mustafa Demirel */
	public static double power(int a, int b){
		return Math.pow(a, b) - 123;
	}

	/**Writer: Admir Nurkovic */
	public static int remainder(int a, int b){
		return a%b+300;
	}
}

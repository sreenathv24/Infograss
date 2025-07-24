package sdde;
import java.util.*;

public class Array {

	public static void main(String[] args) {
		ArrayList<String> car = new ArrayList<>();
		car.add("BMW");
		car.add("VOLVO");
		car.add("FERARI");
		
		System.out.println(car);
		
		Thread t1 = new Thread(()->{
			car.set(1,"Mercedes");
		});
		;
		Thread t2 = new Thread(()->{
			car.set(1, "AUDI");
		});
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		   
		}
		catch(Exception e) {
			System.out.println(e);
			
		}
		System.out.println(car);	

	}

}

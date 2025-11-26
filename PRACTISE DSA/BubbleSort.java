package javacode;

public class BubbleSort {

	public void bubbleSort(int[] a) {
		for(int i=0;i<(a.length);i++) {
			for(int j=0;j<(a.length-i-1);j++) {
				if(a[j]>a[j+1]) {
					
					int temp=a[j];
					a[j]=a[j+1];
					a[j+1]=temp;
				}
			}
		}
		for(int array:a) {
			System.out.print(array+" ");
		}
	}
	public static void main(String[] args) {
		int[] arr= {1,10, 18, 22, 3, 78,45, 65};
		BubbleSort b1=new BubbleSort();
		
		b1.bubbleSort(arr);
	}
}

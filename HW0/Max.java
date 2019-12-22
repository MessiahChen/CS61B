
public class Max {
	private static int[] a = {127, 170, 169, 285, 61};
	public static void main(String[] args) {
		System.out.println(max(a));
	}
	private static int max(int[] a) {
		int maxNum = a[0];
		for(int i = 0; i<a.length; i++) {
			if(a[i]>=maxNum)	maxNum = a[i];
		}
		return maxNum;
	}
}


public class ThreeSum {
	private static int[] a = {1,2,3,4,5,61,188,285,189,-11};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(threeSum(a));
	}
	/*Time complexity is too large in this way*/
	private static boolean threeSum(int[] a) {
		for(int i = 0; i<a.length; i++) {
			for(int j = 0; j<a.length; j++) {
				for(int k = 0; k<a.length; k++) {
					if(a[k] == 0)	return true;
					if(a[i]+a[j]+a[k] == 0)	return true;
				}
			}
		}
		return false;
	}
}

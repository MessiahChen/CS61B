import java.util.*;

public class ThreeSumDis {
	private static int[] a = {1,2,3,4,5,61,188,285,189,-9};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(threeSumDis(a));
	}
	private static boolean threeSumDis(int[] a) {
		int x,y,z,start,end;
		Arrays.sort(a);
		for(int i = 0; i<a.length-2; i++) {
			x = a[i];
			start = i+1;
			end = a.length-1;
			while(start<end) {
				y = a[start];
				z = a[end];
				if(x+y+z == 0)	return true;
				else if(x+y+z > 0)	end-=1;
				else	start+=1;
			}
		}
		return false;
	}
}

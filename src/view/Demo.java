package view;

import java.util.Arrays;

public class Demo {

	public static void main(String[] args) {
		
		String[][] schedule = new String[5][5];
		for (String[] str:schedule) {
			Arrays.fill(str, "");
		}
		System.out.println(Arrays.deepToString(schedule));

	}

}

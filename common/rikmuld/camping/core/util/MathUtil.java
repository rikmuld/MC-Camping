package rikmuld.camping.core.util;

public class MathUtil {

	public static int[] inverse(int[] numbers)
	{
		int[] returnNumbers = new int[numbers.length];
		
		for(int i = 0; i<numbers.length; i++)
		{
			returnNumbers[i] = -numbers[i];
		}
		
		return returnNumbers;
	}
}

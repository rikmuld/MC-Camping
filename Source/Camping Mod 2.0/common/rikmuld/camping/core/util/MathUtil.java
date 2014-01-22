package rikmuld.camping.core.util;

public class MathUtil {

	public static float getScaledNumber(int currNumber, int maxNumber, int scaledNumber)
	{
		return ((float)currNumber / (float)maxNumber) * scaledNumber;
	}

	public static int[] inverse(int[] numbers)
	{
		int[] returnNumbers = new int[numbers.length];

		for(int i = 0; i < numbers.length; i++)
		{
			returnNumbers[i] = -numbers[i];
		}

		return returnNumbers;
	}
}

package edu.uic.ids.database;

import java.text.NumberFormat;

public class MathUtil {
	private static NumberFormat number = NumberFormat.getNumberInstance();
	//private static NumberFormat percent = NumberFormat.getPercentInstance();
	
	public static String numberFormat(double value) {
		number.setMaximumFractionDigits(4);
		return number.format(value);
	}
	
	public static String numberFormat(float value) {
		number.setMaximumFractionDigits(4);
		return number.format(value);
	}
	
	public static double round(double value, double precision) {
		return Math.round(value * precision)/precision;
		}
}

package com.hcl.atf.taf.controller.utilities;

import java.util.regex.Pattern;

/*
 * Source URL : https://forums.oracle.com/thread/2046138
 * TODO : Provide credit to the source.
 */
public class CronValidate {
	enum Day {
		MON, TUE, WED, THU, FRI, SAT, SUN
	}
	enum Month {
		JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
	}
	public static String validQuartzCron(String cronExpression) {

		StringBuffer errorMessage =  new StringBuffer();
		
		if (cronExpression == null || cronExpression.trim().equals("")) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " Cron Expression is empty. Provide a valid Cron Expression.");
			return errorMessage.toString();
		}
		String[] parts = cronExpression.split(" ");
		if (parts.length != 6 && parts.length != 7) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression is not complete. Provide a valid Cron Expression.");
			return errorMessage.toString();
		}
		// seconds
		if (!CronValidate.validateField(parts[0], CronValidate.zero59(), false)) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression seconds section(#1) is not valid.");
			//return false;
		}
		// minutes
		if (!CronValidate.validateField(parts[1], CronValidate.zero59(), false)) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression minutes section(#2) is not valid.");
		}
		// hours
		if (!CronValidate.validateField(parts[2], CronValidate.zero23(), false)) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression hours section(#3) is not valid.");
		}
		// Day of Month
		if ((parts[3].contains("L") || parts[3].contains("W"))) {
			if (!CronValidate.validateLWMonth(parts[3]))
				errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression day of month section(#4) is not valid.");
		}
		else if (!CronValidate.validateField(parts[3], CronValidate.one31(), true)) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression day of month section(#4) is not valid.");
		}

		// Month
		if (!CronValidate.validateField(CronValidate.convertToAllNumbers(parts[4]), CronValidate.one12(), false)) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression month section(#5) is not valid.");
		}
		// Day of Week
		if ((parts[5].contains("L") || parts[5].contains("#"))) {
			if (!CronValidate.validateLHashDayOfWeek(parts[5]))
				errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression day of week section(#6) is not valid.");
		}
		else if (!CronValidate.validateField(CronValidate.convertToAllNumbers(parts[5]), CronValidate.one7(), true)) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression day of week section(#6) is not valid.");
		}

		// year
		if (parts.length == 7 && !CronValidate.validYear(parts[6])) {
			errorMessage = ValidationUtility.appendErrorMessage(errorMessage, " The Cron Expression year section(#7) is not valid.");
		}
		
		if (errorMessage.length() == 0)// if you got this far you are ok.
			return null;
		else
			return errorMessage.toString();
	}

	public static boolean validateField(String cronExpression, String basicFieldRegex, boolean allowQMark) {
		if (cronExpression.toString().equals("")) {
			return false;
		}
		if (cronExpression.equals("*")) {
			return true;
		}
		if (allowQMark && cronExpression.equals("?")) {
			return true;
		}
		if (CronValidate.validateRegex(basicFieldRegex, cronExpression)) {
			return true;
		}
		if (!cronExpression.contains(",") && !cronExpression.contains("/") && !cronExpression.contains("-")) {
			return CronValidate.validateRegex(basicFieldRegex, cronExpression);
		}
		if (cronExpression.contains(",")) {
			String[] parts = cronExpression.split(",");
			for (String part : parts) {
				if (part.contains("-")) {
					String[] pieces = part.split("\\-");
					for (String piece : pieces) {
						if (!CronValidate.validateRegex(basicFieldRegex, piece)) {
							return false;
						}
					}

				} else if (part.contains("/")) {
					String[] pieces = part.split("/");
					if (pieces.length != 2) {
						return false;
					}
					if (!CronValidate.validateRegex(basicFieldRegex, pieces[0]) || !CronValidate.validateRegex(basicFieldRegex, pieces[1])) {
						return false;
					}
				}

				else if (!CronValidate.validateRegex(basicFieldRegex, part)) {
					return false;

				}
			}
			return true;
		}
		if (cronExpression.contains("/")) {
			String[] parts = cronExpression.split("/");
			if (parts.length != 2) {
				return false;
			}
			if (!CronValidate.validateRegex(basicFieldRegex, parts[0]) || !CronValidate.validateRegex(basicFieldRegex, parts[1])) {
				return false;
			}
		}
		if (cronExpression.contains("-")) {
			String[] pieces = cronExpression.split("\\-");
			for (String piece : pieces) {
				if (!CronValidate.validateRegex(basicFieldRegex, piece)) {
					return false;
				}
			}
		}

		return true;

	}

	// exceptionals
	private static boolean validateLHashDayOfWeek(String dayOfWeekCron) {
		if (dayOfWeekCron.equals("L") || dayOfWeekCron.matches("^([1-7])L$") || dayOfWeekCron.matches("^([1-7]#[1-7])$")) {
			return true;
		}
		return false;
	}

	private static boolean validateLWMonth(String dayOfMonthCron) {
		if (dayOfMonthCron.equals("L") || dayOfMonthCron.equals("LW") || dayOfMonthCron.equals("W") || dayOfMonthCron.matches("^([1-9]|1[0-9]|2[0-9]|3[0-1])W$")) {
			return true;
		}
		return false;
	}
	private static boolean validYear(String yearCron) {
		if (yearCron.equals("*")) {
			return true;
		}
		if (yearCron.matches(CronValidate.yearRange())) {
			return true;
		}
		if (yearCron.matches(CronValidate.year())) {
			return true;
		}
		if(yearCron.contains(",")) {
			String[] parts = yearCron.split(",");
			// each part must be a year range or a simple year
			for(String part : parts) {
				if (part.matches(CronValidate.yearRange()) || part.matches(CronValidate.year())) {
					return true;
				}
			}
		}
		return false;
	}

	// utilities
	public static String convertToAllNumbers(String cron) {
		for (Day day : Day.values()) {
			if (cron.contains(day.name())) {
				cron = cron.replaceAll(day.name(), "" + (day.ordinal() + 1));
			}
		}
		for (Month month : Month.values()) {
			if (cron.contains(month.name())) {
				cron = cron.replaceAll(month.name(), "" + (month.ordinal() + 1));
			}
		}
		return cron;
	}

	public static boolean validateRegex(String regex, String input) {
		return Pattern.matches(regex, input);
	}
	// Regexes
	private static String zero59() {
		return "^([0-5]?[0-9])$";
	}

	private static String zero23() {
		return "^([0-9]|1[0-9]|2[0-3])$";
	}

	private static String one31() {
		return "^([1-9]|1[0-9]|2[0-9]|3[0-1])$";
	}

	private static String one12() {
		return "^([1-9]|1[0-2])$";
	}
	private static String one7() {
		return "^([1-7])$";
	}
	private static String yearRange() {
		return "^[0-9]{1,4}-[0-9]{1,4}$";
	}
	private static String year() {
		return "[0-9]{1,4}";
	}
}
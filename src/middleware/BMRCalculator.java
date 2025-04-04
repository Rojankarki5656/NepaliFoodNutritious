package middleware;

import Models.BMR;

public class BMRCalculator {
    public static BMR calculateKcal(String sex, double weight, double height, int age, int activityFactor) {
        double bmr = 0;
        String error = "";
        System.out.println(age);
        
        if (weight <= 0 || height <= 0 || age <=0 ) {
        	return new BMR(bmr, bmr, "Age or weight or height cannot be less than zero.");
        }
        if(activityFactor <=0 || activityFactor>4)
        {
        	return new BMR(bmr,bmr,"activity factor should be between 0 and 4 only");
        }
        // Calculate BMR based on sex
        if (sex.equalsIgnoreCase("male")) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else if (sex.equalsIgnoreCase("female")) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        } else {
            error = "Invalid sex input! Must be male or female";
        }

        // Determine activity factor multiplier
        double multiplier;
        switch (activityFactor) {
            case 1 -> multiplier = 1.2;
            case 2 -> multiplier = 1.375;
            case 3 -> multiplier = 1.55;
            case 4 -> multiplier = 1.725;
            default -> throw new IllegalArgumentException("Invalid activity factor! Must be between 1 and 4.");
        }

        // Calculate total daily kcal requirement
        double totalKcal = bmr * multiplier;

        // Return the BMR object
        return new BMR(bmr, totalKcal, error);
    }
}

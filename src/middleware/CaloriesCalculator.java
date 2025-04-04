package middleware;

import java.util.Map;

import backend.FoodService;

public class CaloriesCalculator {
	
	public static String CalculateCalories(String foodName, String quantityStr) {		
            try {
                int quantity = Integer.parseInt(quantityStr);
                float caloriesPerUnit = FoodService.foodCalories(foodName);
                if(quantity < 0) {
                	return "Quantity cannot be less than zero";
                }
                if (caloriesPerUnit > 0) {
                    float totalCalories = quantity * caloriesPerUnit;
                    return "Total Calories: " + totalCalories;
                } else {
                	return"Food item not found.";
                }
            } catch (NumberFormatException ex) {
            	return "Enter a valid quantity.";
            }
	}
	
	public static float totalCalories(int id) {
        Map<String, Integer> foodData = FoodService.updateCharts(id);
        float total = foodData.entrySet().stream()
                .map(entry -> CalculateCalories(entry.getKey(), String.valueOf(entry.getValue())))
                .filter(result -> result.startsWith("Total Calories: ")) // Only process valid results
                .map(result -> Float.parseFloat(result.replace("Total Calories: ", "")))
                .reduce(0f, Float::sum); // Sum up the calories

            return total;
	}
}

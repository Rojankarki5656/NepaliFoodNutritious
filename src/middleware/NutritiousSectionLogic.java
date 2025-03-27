package middleware;

import Models.Target;
import backend.FoodService;
import backend.TargetService;

public class NutritiousSectionLogic {
		
	public static boolean checkLogic(int id) {
            Target targetCal = TargetService.checkTarget(id);
            if (targetCal != null && targetCal.getStatus().equals("Active")) {
                return true;
            }
        
		return false;

	}
	public static String addLogic(int id, String foodName, int quantity, Float calories) {
        if (!foodName.isEmpty()) {
        	String result = FoodService.addFood(id, foodName, quantity, calories);
        	return result;
		
	}
        return "I guess food name is empty!!!!";
}
}
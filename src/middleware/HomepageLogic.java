package middleware;

import Models.Target;
import backend.TargetService;

public class HomepageLogic {
	private static int targetCalories;
	private static Float cumulativeConsumedCalories;
	
	public static boolean switchInHomePage(int id) {
        Target targetCal = TargetService.checkTarget(id);
        if (targetCal != null && targetCal.getStatus().equals("Active")) {
            targetCalories = targetCal.getTarget();
        }
        cumulativeConsumedCalories = (float) TargetService.updateCalories(id);
        if (targetCalories > 0) {
            return true;
        }
        
     return false;

	}
    public String setTargetCalories(int newTarget, int id) {
        try {
            if (newTarget <= 0) {
            	return "Error: Target must be greater than 0!";
                
            }
            targetCalories = newTarget;
            TargetService.saveTargetToDatabase(targetCalories, id);
            return "Yay!";
        } catch (NumberFormatException ex) {
            return "Invalid input! Please enter a number.";
        }
    }
    public String addFood(int consumedCalories, int id) {
        try {
            if (cumulativeConsumedCalories + consumedCalories > targetCalories) {
                return "Error: Consumed calories exceed target!";
            }

            cumulativeConsumedCalories += consumedCalories;
            System.out.println(consumedCalories);
            System.out.println(cumulativeConsumedCalories);
            System.out.println(targetCalories);
            TargetService.saveConsumedCaloriesToDatabase(cumulativeConsumedCalories, targetCalories);
            TargetService.updateRemaining(id);

            if (cumulativeConsumedCalories == targetCalories) {
                return "Completed! You have reached your target.";
            } else {
                return "";
            }
        } catch (NumberFormatException ex) {
            return "Invalid input! Please enter a number.";
        }
    }

    public void resetTargetCalories(int id) {
        String status = "Reset";
        TargetService.resetTargetToDatabase(status, id);
        switchInHomePage(id);
    }
}

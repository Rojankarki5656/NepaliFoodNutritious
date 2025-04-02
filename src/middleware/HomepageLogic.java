package middleware;

import java.time.LocalDate;

import Models.User_Target;
import backend.TargetService;

public class HomepageLogic {
	private static double targetCalories;
	private static double cumulativeConsumedCalories;
	
	public static boolean switchInHomePage(int id) {
        User_Target targetCal = TargetService.checkTarget(id);
        if (targetCal != null && targetCal.getStatus().equals("Active")) {
            targetCalories = targetCal.getTargetCalories();
            cumulativeConsumedCalories = targetCal.getCaloriesTaken();

        }
        if (targetCalories > 0) {
            return true;
        }
        
     return false;

	}
    public String setTargetCalories(int newTarget, int id, LocalDate date ) {
        try {
            if (newTarget <= 0) {
            	return "Error: Target must be greater than 0!";
                
            }
            targetCalories = newTarget;
            TargetService.saveTargetToDatabase(targetCalories, id, date);
            return "Yay!";
        } catch (NumberFormatException ex) {
            return "Invalid input! Please enter a number.";
        }
    }
    
    public String addFood(int consumedCalories, int id, double totalcal) {
        try {
            User_Target targetCal = TargetService.checkTarget(id);
            LocalDate currentDate = LocalDate.now();
            if (totalcal + consumedCalories >= targetCalories) {
            	resetTargetCalories(id, "Completed");
                return "Error: Consumed calories exceed target!";
            }
            
            if(targetCal.getEndDate().isAfter(currentDate)) {
            	return "You have reached the date";
            }

            totalcal += consumedCalories;
            System.out.println(targetCal.getEndDate());
            System.out.println(currentDate);

            TargetService.saveConsumedCaloriesToDatabase(totalcal, targetCalories);
            TargetService.updateRemaining(id, totalcal);
            
            return "Sucess";

        } catch (NumberFormatException ex) {
            return "Invalid input! Please enter a number.";
        }
    }

    public void resetTargetCalories(int id, String status) {
        TargetService.resetTargetToDatabase(status, id);
        switchInHomePage(id);
    }
}

package Models;

import java.time.LocalDate;

public class User_Target{
    private int userId;
    private double targetCalories;
    private String status; // "In Progress", "Completed", "Not Completed", "Reset"
    private double calories_taken;
    private double targetRemaining;
    private LocalDate startDate;
    private LocalDate endDate;

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(double targetCalories) {
        this.targetCalories = targetCalories;
    }

    public double getTargetRemaining() {
        return targetRemaining;
    }

    public void setTargetRemaining(double targetRemaining) {
        this.targetRemaining = targetRemaining;
    }
    
    public double getCaloriesTaken() {
        return calories_taken;
    }

    public void setCaloriesTaken(double calories_taken) {
        this.calories_taken = calories_taken;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
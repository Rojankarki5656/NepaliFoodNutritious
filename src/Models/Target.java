package Models;

public class Target {
	
	private int target;
	private String status;
	private Float total_calories;
	private Float remaining;
	
    // Constructor
    public Target(int target, String status, Float total_calories, Float remaining) {
        this.target = target;
        this.status = status;
        this.total_calories = total_calories;
        this.remaining = remaining;
    }
    public int getTarget() {
        return target;
    }

    public String getStatus() {
        return status;
    }

    public Float getTotalCalories() {
        return total_calories;
    }
    
    public Float getRemaining() {
    	return remaining;
    }
}

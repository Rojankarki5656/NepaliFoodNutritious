package Models;

public class FoodItems {
	private String name;
    private Float calories;
    private Float protein;
    private Float carbohydrates;
    private Float fats;

    // Constructor
    public FoodItems(String name,Float calories, Float protein, Float carbohydrates, Float fats) {
    	this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
    }
    public String getName() {
    	return name; 
    	}

    // Getter methods
    public Float getCalories() {
        return calories;
    }

    public Float getProtein() {
        return protein;
    }

    public Float getCarbohydrates() {
        return carbohydrates;
    }

    public Float getFats() {
        return fats;
    }

    // Method to get all values as an array
    public Float[] getAllNutrients() {
        return new Float[]{calories, protein, carbohydrates, fats};
    }

    // Method to get all values as a formatted string
    public String getAllNutrientsAsString() {
        return "Calories: " + calories + " kcal, Protein: " + protein + " g, Carbs: " + carbohydrates + " g, Fats: " + fats + " g";
    }
}

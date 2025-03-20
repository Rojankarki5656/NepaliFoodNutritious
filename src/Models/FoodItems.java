package Models;

public class FoodItems {
    private Float calories;
    private Float protein;
    private Float carbohydrates;
    private Float fats;

    // Constructor
    public FoodItems(Float calories, Float protein, Float carbohydrates, Float fats) {
        this.calories = calories;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
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

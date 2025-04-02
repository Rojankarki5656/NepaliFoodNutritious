package Models;

import java.sql.Array;
import java.sql.Date;

public class Food_log {
	private Array food_name;
	private Array quantity;
	private Array total_calories;
	private Array date;
	
	public Food_log(Array food_name, Array quantity, Array total_calories, Array date) {
		this.food_name = food_name;
		this.quantity = quantity;
		this.total_calories = total_calories;
		this.date = date;
	}

	public Array getFoodName() {
		return food_name;
	}
	public Array getQuantity() {
		return quantity;
	}
	public Array getTotalCalories() {
		return total_calories;
	}
	public Array getDate() {
		return date;
	}

}

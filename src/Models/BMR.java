package Models;

public class BMR {
    private double bmr;
    private double totalKcal;
    private double proteinKcal;
    private double fatKcal;
    private double carbsKcal;
    private String error;

    // Constructor
    public BMR(double bmr, double totalKcal, String error) {
        this.bmr = bmr;
        this.totalKcal = totalKcal;
        this.proteinKcal = totalKcal * 0.3;
        this.fatKcal = totalKcal * 0.3;
        this.carbsKcal = totalKcal * 0.4;
        this.error = error;
    }

    // Getter methods for all fields
    public double getProteinKcal() {
        return proteinKcal;
    }

    public double getFatKcal() {
        return fatKcal;
    }

    public double getCarbsKcal() {
        return carbsKcal;
    }

    public double getBmr() {
        return bmr;
    }

    public double getTotalKcal() {
        return totalKcal;
    }
    public String getError() {
        return error;
    }
}

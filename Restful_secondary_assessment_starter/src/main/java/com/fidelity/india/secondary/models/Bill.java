package com.fidelity.india.secondary.models;

import java.util.Objects;


	

public class Bill {
    private String marina;
    private String country;
    private String vesselName;
    private double dailyRate;
    private int billableNights;
    private String currentBalance;

    public Bill() {
        // Default constructor
    }

    public Bill(String marina, String country, String vesselName, double dailyRate, int billableNights, String currentBalance) {
        this.marina = marina;
        this.country = country;
        this.vesselName = vesselName;
        this.dailyRate = dailyRate;
        this.billableNights = billableNights;
        this.currentBalance = currentBalance;
    }

    // Getters and setters for all the fields
    public String getMarina() {
        return marina;
    }

    public void setMarina(String marina) {
        this.marina = marina;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public int getBillableNights() {
        return billableNights;
    }

    public void setBillableNights(int billableNights) {
        this.billableNights = billableNights;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
	public int hashCode() {
		return Objects.hash(billableNights, country, currentBalance, dailyRate, marina, vesselName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bill other = (Bill) obj;
		return billableNights == other.billableNights && Objects.equals(country, other.country)
				&& Objects.equals(currentBalance, other.currentBalance)
				&& Double.doubleToLongBits(dailyRate) == Double.doubleToLongBits(other.dailyRate)
				&& Objects.equals(marina, other.marina) && Objects.equals(vesselName, other.vesselName);
	}

	@Override
    public String toString() {
        return "Bill{" +
                "marina='" + marina + '\'' +
                ", country='" + country + '\'' +
                ", vesselName='" + vesselName + '\'' +
                ", dailyRate=" + dailyRate +
                ", billableNights=" + billableNights +
                ", currentBalance='" + currentBalance + '\'' +
                '}';
    }
}


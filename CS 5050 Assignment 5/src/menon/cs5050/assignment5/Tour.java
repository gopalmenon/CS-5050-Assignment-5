package menon.cs5050.assignment5;

import java.util.ArrayList;
import java.util.List;

public class Tour {

	private List<City> citiesInTour;
	private double tourLength;
	private int numberOfStatesExpanded;
	
	public Tour(List<City> citiesInTour, double tourLength) {
		this.citiesInTour = citiesInTour;
		this.tourLength = tourLength;
		this.numberOfStatesExpanded = 0;
	}
	
	public Tour(Tour tourToClone) {
		
		this.citiesInTour = new ArrayList<City>(tourToClone.getCitiesInTour().size());
		for (City city : tourToClone.getCitiesInTour()) {
			this.citiesInTour.add(city);		
		}
		
		this.tourLength = tourToClone.getTourLength();
	}
	
	public List<City> getCitiesInTour() {
		return citiesInTour;
	}
	public void setCitiesInTour(List<City> citiesInTour) {
		this.citiesInTour = citiesInTour;
	}
	public double getTourLength() {
		return tourLength;
	}
	public void setTourLength(double tourLength) {
		this.tourLength = tourLength;
	}
	
	public int getNumberOfStatesExpanded() {
		return numberOfStatesExpanded;
	}

	public void setNumberOfStatesExpanded(int numberOfStatesExpanded) {
		this.numberOfStatesExpanded = numberOfStatesExpanded;
	}

	City getLastCity() {
		if (this.citiesInTour.size() == 0) {
			return null;
		} else {
			return this.citiesInTour.get(this.citiesInTour.size() - 1);
		}
	}
	
	/**
	 * Add a city to the tour by appending the cities array with the new city and incrementing the tour length by the distance from
	 * the last city to the new city
	 * @param cityToAdd
	 */
	public void addCity(City cityToAdd) {
		
		City lastCity = getLastCity();
		this.citiesInTour.add(cityToAdd);
		if (lastCity != null) {
			this.tourLength += lastCity.distanceTo(cityToAdd);
		}
		
	}
	
	/**
	 * Add a city to the tour by appending the cities array with the new city and incrementing the tour length by the distance 
	 * passed in as a parameter
	 * @param cityToAdd
	 * @param distanceToCity
	 */
	void addCity(City cityToAdd, double distanceToCity) {

		this.citiesInTour.add(cityToAdd);
		this.tourLength += distanceToCity;

	}
	
	@Override
	public String toString() {
		
		StringBuffer returnValue = new StringBuffer();
		
		returnValue.append("[");
		
		boolean firstTime = true;
		for (City city : this.citiesInTour) {
			
			if (firstTime) {
				firstTime = false;
			} else {
				returnValue.append(", ");
			}
			
			returnValue.append(city.toString());
			
		}
		
		returnValue.append("]");
		
		return returnValue.toString();
		
	}
	
	/**
	 * @param city
	 * @return true if the tour contain the city
	 */
	public boolean contains(City city) {
		
		if (city == null) {
			return false;
		}
		
		for (City cityinTour : this.citiesInTour) {
			if (city.equals(cityinTour)) {
				return true;
			}
		}
		
		return false;
		
	}
	
}

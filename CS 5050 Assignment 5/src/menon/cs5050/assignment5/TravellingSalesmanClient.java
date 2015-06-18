package menon.cs5050.assignment5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TravellingSalesmanClient {
	
	public static final int DEFAULT_NUMBER_OF_CITIES = 10;
	
	private Random randomDoubleGenerator;
	private List<City> citiesToTour;
	
	
	/**
	 * Constructor
	 * @param numberOfCitiesToTour
	 */
	public TravellingSalesmanClient(int numberOfCitiesToTour) {
		this.randomDoubleGenerator = new Random(2038905);
		try {
			this.citiesToTour = new ArrayList<City>(numberOfCitiesToTour);
		} catch (IllegalArgumentException e) {
			System.err.println(numberOfCitiesToTour + " is not a valid parameter for number of cities.");
			System.exit(0);
		}
	}
	
	/**
	 * Create a list of unique cities to tour
	 * @param numberOfCitiesToTour
	 */
	private void generateCitiesToTour(int numberOfCitiesToTour) {
		
		City nextCity = null;
		for (int cityCounter = 0; cityCounter < numberOfCitiesToTour; ) {
			
			nextCity = getNextRandomCity();
			if (this.citiesToTour.contains(nextCity)) {
				continue;
			} else {
				this.citiesToTour.add(nextCity);
				++cityCounter;
			}
			
		}

	}
	
	/**
	 * @return a City located at randomly generated coordinates
	 */
	private City getNextRandomCity() {
		
		return new City(this.randomDoubleGenerator.nextDouble() * 640, this.randomDoubleGenerator.nextDouble() * 480);
		
	}
	
	/**
	 * @return the shortest tour
	 */
	private Tour getShortestTour() {
		
		try {
			TravellingSalesman travellingSalesman = new TravellingSalesman(getNextRandomCity(), this.citiesToTour, TravellingSalesman.EXHAUSTIVE_SOLUTION);
			return travellingSalesman.getShortestTour();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {
		
		int numberOfCitiesToTour = 0;
		try {
			if (args != null && args.length > 0) {
				numberOfCitiesToTour = Integer.parseInt(args[0].trim());
			} else {
				numberOfCitiesToTour = DEFAULT_NUMBER_OF_CITIES;
			}
		} catch (NumberFormatException e) {
			numberOfCitiesToTour = DEFAULT_NUMBER_OF_CITIES;
			System.err.println(args[0].trim() + " is not a valid parameter for number of cities.");
		}
		
		TravellingSalesmanClient travellingSalesmanClient = new TravellingSalesmanClient(numberOfCitiesToTour);
		travellingSalesmanClient.generateCitiesToTour(numberOfCitiesToTour);
		System.out.println("Shortest tour is: " + travellingSalesmanClient.getShortestTour());
		
		
	}	

}

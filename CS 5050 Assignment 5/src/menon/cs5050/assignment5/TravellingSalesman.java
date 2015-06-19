package menon.cs5050.assignment5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravellingSalesman {
	
	public static final String EXHAUSTIVE_SOLUTION = "E";
	public static final String GLOBAL_UPPER_BOUND_SOLUTION = "G";
	public static final String LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS = "LC";
	public static final String LOCAL_LOWER_BOUND_SOLUTION_NEW = "LN";
	public static final String INITIAL_GREEDY_SOLUTION_WITH_LB = "IG";
	public static final String INITIAL_GREEDY_SOLUTION_WITH_LB_NEW = "IGN";
	
	private City startCity;
	private List<City> citiesToVisit;
	private String solutionToUse;
	private double bestRouteDistanceSoFar;
	private int numberOfStatesExpanded;
	private Map<CityPair, Double> distanceTable;
	private Tour greedySolutionTour;
	
	public TravellingSalesman(City startCity, List<City> citiesToVisit, String solutionToUse) throws Exception {
		
		if (!EXHAUSTIVE_SOLUTION.equalsIgnoreCase(solutionToUse.trim()) && !GLOBAL_UPPER_BOUND_SOLUTION.equalsIgnoreCase(solutionToUse.trim()) &&
			!LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS.equalsIgnoreCase(solutionToUse.trim()) && !LOCAL_LOWER_BOUND_SOLUTION_NEW.equalsIgnoreCase(solutionToUse.trim()) &&
			!INITIAL_GREEDY_SOLUTION_WITH_LB.equalsIgnoreCase(solutionToUse.trim()) && !INITIAL_GREEDY_SOLUTION_WITH_LB_NEW.equalsIgnoreCase(solutionToUse.trim())) {
			throw new Exception("Invalid solution to use " + solutionToUse);
		}
		
		this.startCity = startCity;
		this.citiesToVisit = citiesToVisit;
		this.solutionToUse = solutionToUse.trim();
		this.bestRouteDistanceSoFar = Double.MAX_VALUE;
		this.numberOfStatesExpanded = 0;
		this.distanceTable = new HashMap<CityPair, Double>();
		fillDistanceTable();
		
		if (INITIAL_GREEDY_SOLUTION_WITH_LB.equalsIgnoreCase(solutionToUse.trim()) || INITIAL_GREEDY_SOLUTION_WITH_LB_NEW.equalsIgnoreCase(solutionToUse.trim())) {
			this.greedySolutionTour = new Tour(new ArrayList<City>(), 0);
			findGreedySolutionTour();
		} else {
			this.greedySolutionTour = null;
		}
		
	}
	
	/**
	 * Fill up table for distance between cities. The table will be used by the greedy solution.
	 */
	private void fillDistanceTable() {
		
		//Loop through all cities in the list all fill the table
		int numberOfCities = this.citiesToVisit.size();
		double distancebetweenCities = 0.0;
		for (int fromCityIndex = 0; fromCityIndex < numberOfCities - 1; ++fromCityIndex) {
			
			distancebetweenCities = this.citiesToVisit.get(fromCityIndex).distanceTo(this.startCity);
			distanceTable.put(new CityPair(this.citiesToVisit.get(fromCityIndex), this.startCity), Double.valueOf(distancebetweenCities));
			distanceTable.put(new CityPair(this.startCity, this.citiesToVisit.get(fromCityIndex)), Double.valueOf(distancebetweenCities));
			
			for (int toCityIndex = fromCityIndex + 1; toCityIndex < numberOfCities; ++toCityIndex) {
				
				distancebetweenCities = this.citiesToVisit.get(fromCityIndex).distanceTo(this.citiesToVisit.get(toCityIndex));
				distanceTable.put(new CityPair(this.citiesToVisit.get(fromCityIndex), this.citiesToVisit.get(toCityIndex)), Double.valueOf(distancebetweenCities));
				distanceTable.put(new CityPair(this.citiesToVisit.get(toCityIndex), this.citiesToVisit.get(fromCityIndex)), Double.valueOf(distancebetweenCities));
		
			}
		}

		distancebetweenCities = this.citiesToVisit.get(numberOfCities - 1).distanceTo(this.startCity);
		distanceTable.put(new CityPair(this.citiesToVisit.get(numberOfCities - 1), this.startCity), Double.valueOf(distancebetweenCities));
		distanceTable.put(new CityPair(this.startCity, this.citiesToVisit.get(numberOfCities - 1)), Double.valueOf(distancebetweenCities));
		
	}
	
	/**
	 * Find the greedy solution tour from the starting city by selecting the closest next city
	 */
	private void findGreedySolutionTour() {
		
		//Start with the city of origin
		City fromCity = this.startCity, closestCity = null;
		this.greedySolutionTour.addCity(fromCity, 0.0);
		double closestCityDistance = Double.MAX_VALUE;
		boolean moreCitiesToVisit = true;
		
		while (moreCitiesToVisit) {
			
			closestCityDistance = Double.MAX_VALUE;
			closestCity = null;
			
			for (City city : this.citiesToVisit) {
				if (!city.equals(fromCity) && !this.greedySolutionTour.contains(city)) {
				
					if (this.distanceTable.get(new CityPair(fromCity, city)).doubleValue() < closestCityDistance) {
						closestCityDistance = this.distanceTable.get(new CityPair(fromCity, city)).doubleValue();
						closestCity = city;
					}
				
				}
			}
			
			if (closestCity == null) {
				moreCitiesToVisit = false;
			} else {
				this.greedySolutionTour.addCity(closestCity, closestCityDistance);
				fromCity = closestCity;
			}

		}
		
		this.bestRouteDistanceSoFar = this.greedySolutionTour.getTourLength();
		
	}
	
	/**
	 * @return the shortest tour based on the option specified
	 */
	public Tour getShortestTour() {
		
		Tour shortestTour = null;
		Tour tour = new Tour(new ArrayList<City>(), 0);
		tour.addCity(startCity, 0.0);
		if (EXHAUSTIVE_SOLUTION.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getExhaustiveSearchTour(tour, this.citiesToVisit);
		} else if (GLOBAL_UPPER_BOUND_SOLUTION.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getGlobalUpperBoundSearchTour(tour, this.citiesToVisit);
		} else if (LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getGlobalUpperBoundWithLocalLowerBoundSearchTour(tour, this.citiesToVisit);
		} else if (LOCAL_LOWER_BOUND_SOLUTION_NEW.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getGlobalUpperBoundWithNewLocalLowerBoundSearchTour(tour, this.citiesToVisit);
		} else if (INITIAL_GREEDY_SOLUTION_WITH_LB.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getGlobalUpperBoundWithLocalLowerBoundSearchTour(tour, this.citiesToVisit);
			if (shortestTour == null) {
				shortestTour = this.greedySolutionTour;
			}
		} else if (INITIAL_GREEDY_SOLUTION_WITH_LB_NEW.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour =  getGlobalUpperBoundWithNewLocalLowerBoundSearchTour(tour, this.citiesToVisit);;
			if (shortestTour == null) {
				shortestTour = this.greedySolutionTour;
			}
		}
		
		if (shortestTour != null) {
			shortestTour.setNumberOfStatesExpanded(this.numberOfStatesExpanded);
		}

		return shortestTour;
		
	}
	
	/**
	 * @param tour
	 * @param remainingCities
	 * @return the shortest tour after doing an exhaustive search
	 */
	private Tour getExhaustiveSearchTour(Tour tour, List<City> remainingCities) {
		
		//Cover the base case for when there are no cities remaining
		if (remainingCities.size() == 0) {
			return tour;
		}
		
		int numberOfRemainingCities = remainingCities.size();
		Tour bestTour = null, modifiedTour = null, currentTour = null;
		City cityToAdd = null;
		List<City> modifiedMemainingCities = null;

		//Loop through remaining cities that need to be included in the tour
		for (int cityIndex = 0; cityIndex < numberOfRemainingCities; ++cityIndex) {
			
			++this.numberOfStatesExpanded;
			
			cityToAdd = remainingCities.get(cityIndex);
			modifiedTour = new Tour(tour);
			modifiedTour.addCity(cityToAdd, this.distanceTable.get(new CityPair(cityToAdd, modifiedTour.getLastCity())).doubleValue());
			modifiedMemainingCities = new ArrayList<City>(remainingCities);
			modifiedMemainingCities.remove(cityToAdd);
			
			currentTour = getExhaustiveSearchTour(modifiedTour, modifiedMemainingCities);
			if (bestTour == null) {
				bestTour = currentTour;
			} else {
				if (currentTour.getTourLength() < bestTour.getTourLength()) {
					bestTour = currentTour;
				}
			}
			
		}
		
		return bestTour;

	}
	
	/**
	 * @param tour
	 * @param remainingCities
	 * @return the shortest tour after doing an exhaustive search that disregards any tours that are longer than current best
	 */
	private Tour getGlobalUpperBoundSearchTour(Tour tour, List<City> remainingCities) {
		
		//Don't search further if the incomplete tour is already longer than the current best
		if (tour.getTourLength() > this.bestRouteDistanceSoFar) {
			return null;
		}
				
		//Cover the base case for when there are no cities remaining
		if (remainingCities.size() == 0) {
			if (tour.getTourLength() < this.bestRouteDistanceSoFar) {
				this.bestRouteDistanceSoFar = tour.getTourLength();
			}
			return tour;
		}

		int numberOfRemainingCities = remainingCities.size();
		Tour bestTour = null, modifiedTour = null, currentTour = null;
		City cityToAdd = null;
		List<City> modifiedMemainingCities = null;

		//Loop through remaining cities that need to be included in the tour
		for (int cityIndex = 0; cityIndex < numberOfRemainingCities; ++cityIndex) {
			
			++this.numberOfStatesExpanded;
			
			cityToAdd = remainingCities.get(cityIndex);
			modifiedTour = new Tour(tour);
			modifiedTour.addCity(cityToAdd, this.distanceTable.get(new CityPair(cityToAdd, modifiedTour.getLastCity())).doubleValue());
			modifiedMemainingCities = new ArrayList<City>(remainingCities);
			modifiedMemainingCities.remove(cityToAdd);
			
			currentTour = getGlobalUpperBoundSearchTour(modifiedTour, modifiedMemainingCities);
			if (currentTour != null) {
				if (bestTour == null) {
					bestTour = currentTour;
				} else {
					if (currentTour.getTourLength() < bestTour.getTourLength()) {
						bestTour = currentTour;
					}
				}
			}
			
		}
		
		return bestTour;

	}
	
	/**
	 * @param tour
	 * @param remainingCities
	 * @return the shortest tour after doing an exhaustive search that disregards any tours that are longer than current best plus the
	 *         lower bound of the remaining distance. The lower bound is computed using the straight line distance from the current last
	 *         city in the tour to the starting city.  
	 */
	private Tour getGlobalUpperBoundWithLocalLowerBoundSearchTour(Tour tour, List<City> remainingCities) {
		
		//Don't search further if the incomplete tour plus lower bound is already longer than the current best
		double tourLengthSoFar = tour.getTourLength();
		if (tour.getCitiesInTour().size() > 0) {
			tourLengthSoFar += tour.getTourLength() + tour.getCitiesInTour().get(tour.getCitiesInTour().size() -1).distanceTo(this.startCity);
		}
		
		if (tourLengthSoFar > this.bestRouteDistanceSoFar) {
			return null;
		}
				
		//Cover the base case for when there are no cities remaining
		if (remainingCities.size() == 0) {
			if (tour.getTourLength() < this.bestRouteDistanceSoFar) {
				this.bestRouteDistanceSoFar = tour.getTourLength();
			}
			return tour;
		}
		
		int numberOfRemainingCities = remainingCities.size();
		Tour bestTour = null, modifiedTour = null, currentTour = null;
		City cityToAdd = null;
		List<City> modifiedMemainingCities = null;

		//Loop through remaining cities that need to be included in the tour
		for (int cityIndex = 0; cityIndex < numberOfRemainingCities; ++cityIndex) {
			
			++this.numberOfStatesExpanded;
			
			cityToAdd = remainingCities.get(cityIndex);
			modifiedTour = new Tour(tour);
			modifiedTour.addCity(cityToAdd, this.distanceTable.get(new CityPair(cityToAdd, modifiedTour.getLastCity())).doubleValue());
			modifiedMemainingCities = new ArrayList<City>(remainingCities);
			modifiedMemainingCities.remove(cityToAdd);
			
			currentTour = getGlobalUpperBoundWithLocalLowerBoundSearchTour(modifiedTour, modifiedMemainingCities);
			if (currentTour != null) {
				if (bestTour == null) {
					bestTour = currentTour;
				} else {
					if (currentTour.getTourLength() < bestTour.getTourLength()) {
						bestTour = currentTour;
					}
				}
			}
			
		}
		
		return bestTour;
		
	}
	
	/**
	 * @param tour
	 * @param remainingCities
	 * @return the shortest tour after doing an exhaustive search that disregards any tours that are longer than current best plus the
	 *         lower bound of the remaining distance. The lower bound is computed using the straight line distance from the current last
	 *         city in the tour, to another city and back to the starting city.  
	 */
	private Tour getGlobalUpperBoundWithNewLocalLowerBoundSearchTour(Tour tour, List<City> remainingCities) {
		
		//Don't search further if the incomplete tour plus lower bound is already longer than the current best
		double tourLengthSoFar = tour.getTourLength();
		if (tour.getCitiesInTour().size() > 0 && remainingCities.size() > 0) {
			tourLengthSoFar += tour.getTourLength() + 
					           tour.getCitiesInTour().get(tour.getCitiesInTour().size() -1).distanceTo(remainingCities.get(0)) + 
					           remainingCities.get(0).distanceTo(this.startCity);
		}

		if (tourLengthSoFar > this.bestRouteDistanceSoFar) {
			return null;
		}
				
		//Cover the base case for when there are no cities remaining
		if (remainingCities.size() == 0) {
			if (tour.getTourLength() < this.bestRouteDistanceSoFar) {
				this.bestRouteDistanceSoFar = tour.getTourLength();
			}
			return tour;
		}

		int numberOfRemainingCities = remainingCities.size();
		Tour bestTour = null, modifiedTour = null, currentTour = null;
		City cityToAdd = null;
		List<City> modifiedMemainingCities = null;

		//Loop through remaining cities that need to be included in the tour
		for (int cityIndex = 0; cityIndex < numberOfRemainingCities; ++cityIndex) {
			
			++this.numberOfStatesExpanded;
			
			cityToAdd = remainingCities.get(cityIndex);
			modifiedTour = new Tour(tour);
			modifiedTour.addCity(cityToAdd, this.distanceTable.get(new CityPair(cityToAdd, modifiedTour.getLastCity())).doubleValue());
			modifiedMemainingCities = new ArrayList<City>(remainingCities);
			modifiedMemainingCities.remove(cityToAdd);
			
			currentTour = getGlobalUpperBoundWithNewLocalLowerBoundSearchTour(modifiedTour, modifiedMemainingCities);
			if (currentTour != null) {
				if (bestTour == null) {
					bestTour = currentTour;
				} else {
					if (currentTour.getTourLength() < bestTour.getTourLength()) {
						bestTour = currentTour;
					}
				}
			}
			
		}
		
		return bestTour;
		
	}

}

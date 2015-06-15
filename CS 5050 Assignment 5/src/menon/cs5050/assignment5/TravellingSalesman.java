package menon.cs5050.assignment5;

import java.util.ArrayList;
import java.util.List;

public class TravellingSalesman {
	
	public static final String EXHAUSTIVE_SOLUTION = "E";
	public static final String GLOBAL_UPPER_BOUND_SOLUTION = "G";
	public static final String LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS = "LC";
	public static final String LOCAL_LOWER_BOUND_SOLUTION_NEW = "LN";
	public static final String INITIAL_GREEDY_SOLUTION_WITH_LB = "IG";
	public static final String INITIAL_GREEDY_SOLUTION_WITH_LB_NEW = "IGN";
	
	private List<City> citiesToVisit;
	private String solutionToUse;
	private double bestRouteDistanceSoFar;
	private int numberOfStatesExpanded;
	
	public TravellingSalesman(List<City> citiesToVisit, String solutionToUse) throws Exception {
		
		if (!EXHAUSTIVE_SOLUTION.equalsIgnoreCase(solutionToUse.trim()) && !GLOBAL_UPPER_BOUND_SOLUTION.equalsIgnoreCase(solutionToUse.trim()) &&
			!LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS.equalsIgnoreCase(solutionToUse.trim()) && !LOCAL_LOWER_BOUND_SOLUTION_NEW.equalsIgnoreCase(solutionToUse.trim()) &&
			!INITIAL_GREEDY_SOLUTION_WITH_LB.equalsIgnoreCase(solutionToUse.trim()) && !INITIAL_GREEDY_SOLUTION_WITH_LB_NEW.equalsIgnoreCase(solutionToUse.trim())) {
			throw new Exception("Invalid solution to use " + solutionToUse);
		}
		
		this.citiesToVisit = citiesToVisit;
		this.solutionToUse = solutionToUse.trim();
		this.bestRouteDistanceSoFar = Double.MAX_VALUE;
		this.numberOfStatesExpanded = 0;
		
	}
	
	/**
	 * @return the shortest tour based on the option specified
	 */
	public Tour getShortestTour() {
		
		Tour shortestTour = null;
		if (EXHAUSTIVE_SOLUTION.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getExhaustiveSearchTour(new Tour(new ArrayList<City>(), 0), this.citiesToVisit);
		} else if (GLOBAL_UPPER_BOUND_SOLUTION.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getGlobalUpperBoundSearchTour(new Tour(new ArrayList<City>(), 0), this.citiesToVisit);
		} else if (LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS.equalsIgnoreCase(this.solutionToUse)) {
			shortestTour = getGlobalUpperBoundWithLocalLowerBoundSearchTour(new Tour(new ArrayList<City>(), 0), this.citiesToVisit);
		} else if (LOCAL_LOWER_BOUND_SOLUTION_NEW.equalsIgnoreCase(this.solutionToUse)) {
			return null;
		} else if (INITIAL_GREEDY_SOLUTION_WITH_LB.equalsIgnoreCase(this.solutionToUse)) {
			return null;
		} else if (INITIAL_GREEDY_SOLUTION_WITH_LB_NEW.equalsIgnoreCase(this.solutionToUse)) {
			return null;
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
			modifiedTour.addCity(cityToAdd);
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
			modifiedTour.addCity(cityToAdd);
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
				
		//Cover the base case for when there are no cities remaining
		if (remainingCities.size() == 0) {
			if (tour.getTourLength() < this.bestRouteDistanceSoFar) {
				this.bestRouteDistanceSoFar = tour.getTourLength();
			}
			return tour;
		}
		
		//Don't search further if the incomplete tour plus lower bound is already longer than the current best
		if (tour.getCitiesInTour().size() > 0) {
			double tourLengthSoFar = tour.getTourLength() + tour.getCitiesInTour().get(tour.getCitiesInTour().size() -1).distanceTo(tour.getCitiesInTour().get(0));
			if (tourLengthSoFar > this.bestRouteDistanceSoFar) {
				return null;
			}
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
			modifiedTour.addCity(cityToAdd);
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

}

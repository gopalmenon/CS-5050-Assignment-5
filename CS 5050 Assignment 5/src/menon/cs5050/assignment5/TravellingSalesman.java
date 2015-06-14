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
	
	public TravellingSalesman(List<City> citiesToVisit, String solutionToUse) throws Exception {
		
		if (!EXHAUSTIVE_SOLUTION.equalsIgnoreCase(solutionToUse.trim()) && !GLOBAL_UPPER_BOUND_SOLUTION.equalsIgnoreCase(solutionToUse.trim()) &&
			!LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS.equalsIgnoreCase(solutionToUse.trim()) && !LOCAL_LOWER_BOUND_SOLUTION_NEW.equalsIgnoreCase(solutionToUse.trim()) &&
			!INITIAL_GREEDY_SOLUTION_WITH_LB.equalsIgnoreCase(solutionToUse.trim()) && !INITIAL_GREEDY_SOLUTION_WITH_LB_NEW.equalsIgnoreCase(solutionToUse.trim())) {
			throw new Exception("Invalid solution to use " + solutionToUse);
		}
		
		this.citiesToVisit = citiesToVisit;
		this.solutionToUse = solutionToUse.trim();
		
	}
	
	/**
	 * @return the shortest tour based on the option specified
	 */
	public Tour getShortestTour() {
		
		if (EXHAUSTIVE_SOLUTION.equalsIgnoreCase(this.solutionToUse)) {
			return getExhaustiveSearchTour(new Tour(new ArrayList<City>(), 0), this.citiesToVisit);
		} else if (GLOBAL_UPPER_BOUND_SOLUTION.equalsIgnoreCase(this.solutionToUse)) {
			return null;
		} else if (LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS.equalsIgnoreCase(this.solutionToUse)) {
			return null;
		} else if (LOCAL_LOWER_BOUND_SOLUTION_NEW.equalsIgnoreCase(this.solutionToUse)) {
			return null;
		} else if (INITIAL_GREEDY_SOLUTION_WITH_LB.equalsIgnoreCase(this.solutionToUse)) {
			return null;
		} else if (INITIAL_GREEDY_SOLUTION_WITH_LB_NEW.equalsIgnoreCase(this.solutionToUse)) {
			return null;
		} else {
			return null;
		}
		
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

}

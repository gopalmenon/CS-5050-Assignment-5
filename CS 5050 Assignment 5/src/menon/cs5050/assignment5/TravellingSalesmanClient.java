package menon.cs5050.assignment5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TravellingSalesmanClient {
	
	public static final int MINIMUM_NUMBER_OF_CITIES = 2;
	public static final int MAXIMUM_NUMBER_OF_CITIES = 13;
	public static final String OUTPUT_FILE_NAME = "output.txt";
	public static final String SCRIPT_GLOBAL_CODE = "\nlog.a.exhaustive = log2(a.exhaustive)" +
													"\nlog.b.global.upper = log2(b.global.upper)" +
													"\nlog.c.lowerbound.class = log2(c.lowerbound.class)" +
													"\nlog.d.lowerbound.new = log2(d.lowerbound.new)" +
													"\nlog.e.greedy.lb = log2(e.greedy.lb)" +
													"\nlog.f.greedy.lb.new = log2(f.greedy.lb.new)" +
													"\nplot(problem.size, log.a.exhaustive, type='l', col='red')" +
													"\nlines(problem.size, log.b.global.upper, col='green')" +
													"\nlines(problem.size, log.c.lowerbound.class, col='blue')" +
													"\nlines(problem.size, log.d.lowerbound.new, col='purple')" +
													"\nlines(problem.size, log.e.greedy.lb, col='pink')" +
													"\nlines(problem.size, log.f.greedy.lb.new, col='brown')";
	
	private Random randomDoubleGenerator;
	
	private List<Integer> problemSize;
	private List<Integer> evaluatedStatesAlorithmA;
	private List<Integer> evaluatedStatesAlorithmB;
	private List<Integer> evaluatedStatesAlorithmC;
	private List<Integer> evaluatedStatesAlorithmD;
	private List<Integer> evaluatedStatesAlorithmE;
	private List<Integer> evaluatedStatesAlorithmF;
	
	private PrintWriter out;
	
	/**
	 * Constructor
	 */
	public TravellingSalesmanClient() {
		
		this.randomDoubleGenerator = new Random(2038905);
		
		int numberOfTests = MAXIMUM_NUMBER_OF_CITIES - MINIMUM_NUMBER_OF_CITIES + 1;
		this.problemSize = new ArrayList<Integer>(numberOfTests);
		this.evaluatedStatesAlorithmA = new ArrayList<Integer>(numberOfTests);
		this.evaluatedStatesAlorithmB = new ArrayList<Integer>(numberOfTests);
		this.evaluatedStatesAlorithmC = new ArrayList<Integer>(numberOfTests);
		this.evaluatedStatesAlorithmD = new ArrayList<Integer>(numberOfTests);
		this.evaluatedStatesAlorithmE = new ArrayList<Integer>(numberOfTests);
		this.evaluatedStatesAlorithmF = new ArrayList<Integer>(numberOfTests);

		try {
			this.out = new PrintWriter(new FileWriter(OUTPUT_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Create a list of unique cities to tour
	 * @param numberOfCitiesToTour
	 * @return a list of cities to tour
	 */
	private List<City> getCitiesToTour(int numberOfCitiesToTour) {
		
		List<City> citiesToTour = new ArrayList<City>(numberOfCitiesToTour);
		City nextCity = null;
		for (int cityCounter = 0; cityCounter < numberOfCitiesToTour; ) {
			
			nextCity = getNextRandomCity();
			if (citiesToTour.contains(nextCity)) {
				continue;
			} else {
				citiesToTour.add(nextCity);
				++cityCounter;
			}
			
		}
		
		return citiesToTour;

	}
	
	/**
	 * @return a City located at randomly generated coordinates
	 */
	private City getNextRandomCity() {
		
		return new City(this.randomDoubleGenerator.nextDouble() * 640, this.randomDoubleGenerator.nextDouble() * 480);
		
	}


	private void runAllTours(int numberOfCities) {
		
		City startCity = getNextRandomCity();
		List<City> citiesToTour = getCitiesToTour(numberOfCities);
		Tour tour = null;
		try {
			
			//Add 1 to take into account the starting city
			this.problemSize.add(Integer.valueOf(numberOfCities + 1));
			
			TravellingSalesman travellingSalesmanA = new TravellingSalesman(startCity, citiesToTour, TravellingSalesman.EXHAUSTIVE_SOLUTION);
			tour = travellingSalesmanA.getShortestTour();
			this.evaluatedStatesAlorithmA.add(Integer.valueOf(tour.getNumberOfStatesExpanded()));
			
			TravellingSalesman travellingSalesmanB = new TravellingSalesman(startCity, citiesToTour, TravellingSalesman.GLOBAL_UPPER_BOUND_SOLUTION);
			tour = travellingSalesmanB.getShortestTour();
			this.evaluatedStatesAlorithmB.add(Integer.valueOf(tour.getNumberOfStatesExpanded()));

			TravellingSalesman travellingSalesmanC = new TravellingSalesman(startCity, citiesToTour, TravellingSalesman.LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS);
			tour = travellingSalesmanC.getShortestTour();
			this.evaluatedStatesAlorithmC.add(Integer.valueOf(tour.getNumberOfStatesExpanded()));

			TravellingSalesman travellingSalesmanD = new TravellingSalesman(startCity, citiesToTour, TravellingSalesman.LOCAL_LOWER_BOUND_SOLUTION_NEW);
			tour = travellingSalesmanD.getShortestTour();
			this.evaluatedStatesAlorithmD.add(Integer.valueOf(tour.getNumberOfStatesExpanded()));

			TravellingSalesman travellingSalesmanE = new TravellingSalesman(startCity, citiesToTour, TravellingSalesman.INITIAL_GREEDY_SOLUTION_WITH_LB);
			tour = travellingSalesmanE.getShortestTour();
			this.evaluatedStatesAlorithmE.add(Integer.valueOf(tour.getNumberOfStatesExpanded()));

			TravellingSalesman travellingSalesmanF = new TravellingSalesman(startCity, citiesToTour, TravellingSalesman.INITIAL_GREEDY_SOLUTION_WITH_LB_NEW);
			tour = travellingSalesmanF.getShortestTour();
			this.evaluatedStatesAlorithmF.add(Integer.valueOf(tour.getNumberOfStatesExpanded()));

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
				
	}

	private void printRunStatistics() {

		this.out.println(getRScript("problem.size", this.problemSize));
		this.out.println(getRScript("a.exhaustive", this.evaluatedStatesAlorithmA));
		this.out.println(getRScript("b.global.upper", this.evaluatedStatesAlorithmB));
		this.out.println(getRScript("c.lowerbound.class", this.evaluatedStatesAlorithmC));
		this.out.println(getRScript("d.lowerbound.new", this.evaluatedStatesAlorithmD));
		this.out.println(getRScript("e.greedy.lb", this.evaluatedStatesAlorithmE));
		this.out.println(getRScript("f.greedy.lb.new", this.evaluatedStatesAlorithmF));
		
		this.out.println(SCRIPT_GLOBAL_CODE);
		
		out.close();
	}
	
	/**
	 * @param listName
	 * @param numbers
	 * @return a string containing the R script for showing the list as a vector
	 */
	private String getRScript(String listName, List<Integer> numbers) {
		
		StringBuffer returnValue = new StringBuffer();
		
		returnValue.append(listName);
		returnValue.append(" = c(");
		
		boolean firstTime = true;
		for (Integer number : numbers) {
			if (firstTime) {
				firstTime = false;
			} else {
				returnValue.append(", ");
			}
			returnValue.append(number.intValue());
		}
		returnValue.append(")");
		
		return returnValue.toString();
		
	}

	public static void main(String[] args) {
		
		TravellingSalesmanClient travellingSalesmanClient = new TravellingSalesmanClient();

		//Run a loop starting from a low count of cities to the maximum
		for (int cityCounter = MINIMUM_NUMBER_OF_CITIES; cityCounter < MAXIMUM_NUMBER_OF_CITIES; ++cityCounter) {
			
			travellingSalesmanClient.runAllTours(cityCounter);
		
		}
		
		travellingSalesmanClient.printRunStatistics();
	}

}

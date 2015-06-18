package menon.cs5050.assignment5;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class TravellingSalesmanUI {

	private static final int ADD_CITIES_MODE = 0;
	private static final int ADD_START_MODE = 1;
	private static final int START_END_DIMENSION = 10;
	private static final int TERRAIN_WIDTH = 640;
	private static final int TERRAIN_HEIGHT = 480;
	
	private static int selectedMode = 0;
	private static String selectedTourMode = "";

	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Traveling Salesman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Create the menu bar.  Make it have a green background.
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);        
        menuBar.setPreferredSize(new Dimension(TERRAIN_WIDTH, 20));
        
        //Create user menus
        JMenu fileMenu, citiesMenu, tourTypeMenu, tourMenu;
        JMenuItem exitMenuItem, addCitiesMenuItem, addStartCityMenuItem, findTourMenuItem, showMetricsMenuItem;
        JRadioButtonMenuItem exhaustiveMenuItem, globalUpperBoundMenuItem, localLowerBoundMenuItem, localLowerBoundNewMenuItem, initialGreedyWithLbMenuItem, initialGreedyWithNewLbMenuItem;
        
        //File menu
        fileMenu = new JMenu("File");
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {System.exit(0);} });
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        
        //Cities menu
        citiesMenu = new JMenu("Cities");
        addCitiesMenuItem = new JMenuItem("Add Cities");
        addCitiesMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedMode = ADD_CITIES_MODE;} });
        addStartCityMenuItem = new JMenuItem("Add Start City");
        addStartCityMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedMode = ADD_START_MODE;} });
        citiesMenu.add(addCitiesMenuItem);
        citiesMenu.add(addStartCityMenuItem);
        menuBar.add(citiesMenu);
        
        //Tour Type menu
        tourTypeMenu = new JMenu("Tour Type");
        ButtonGroup tourTypeGroup = new ButtonGroup();
        
        exhaustiveMenuItem = new JRadioButtonMenuItem("a) Exhaustive");
        exhaustiveMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedTourMode = TravellingSalesman.EXHAUSTIVE_SOLUTION;} });
        exhaustiveMenuItem.setSelected(true);
        selectedTourMode = TravellingSalesman.EXHAUSTIVE_SOLUTION;
        tourTypeGroup.add(exhaustiveMenuItem);
        tourTypeMenu.add(exhaustiveMenuItem);
        
        globalUpperBoundMenuItem = new JRadioButtonMenuItem("b) Global Upper Bound");
        globalUpperBoundMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedTourMode = TravellingSalesman.GLOBAL_UPPER_BOUND_SOLUTION;} });
        tourTypeGroup.add(globalUpperBoundMenuItem);
        tourTypeMenu.add(globalUpperBoundMenuItem);
        
        localLowerBoundMenuItem = new JRadioButtonMenuItem("c) b + Local Lower Bound from class");
        localLowerBoundMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedTourMode = TravellingSalesman.LOCAL_LOWER_BOUND_SOLUTION_FROM_CLASS;} });
        tourTypeGroup.add(localLowerBoundMenuItem);
        tourTypeMenu.add(localLowerBoundMenuItem);        
        
        localLowerBoundNewMenuItem = new JRadioButtonMenuItem("d) b + New Local Lower Bound");
        localLowerBoundNewMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedTourMode = TravellingSalesman.LOCAL_LOWER_BOUND_SOLUTION_NEW;} });
        tourTypeGroup.add(localLowerBoundNewMenuItem);
        tourTypeMenu.add(localLowerBoundNewMenuItem);
        
        initialGreedyWithLbMenuItem = new JRadioButtonMenuItem("e) c + Initial Greedy");
        initialGreedyWithLbMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedTourMode = TravellingSalesman.INITIAL_GREEDY_SOLUTION_WITH_LB;} });
        tourTypeGroup.add(initialGreedyWithLbMenuItem);
        tourTypeMenu.add(initialGreedyWithLbMenuItem);
        
        initialGreedyWithNewLbMenuItem = new JRadioButtonMenuItem("f) d + Initial Greedy");
        initialGreedyWithNewLbMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {selectedTourMode = TravellingSalesman.INITIAL_GREEDY_SOLUTION_WITH_LB_NEW;} });
        tourTypeGroup.add(initialGreedyWithNewLbMenuItem);
        tourTypeMenu.add(initialGreedyWithNewLbMenuItem);      
        menuBar.add(tourTypeMenu);
		
		//Create the terrain for the salesman
		final Terrain terrain = new Terrain();
		terrain.setPreferredSize(new Dimension(TERRAIN_WIDTH, TERRAIN_HEIGHT));
        
        //Tour menu
        tourMenu = new JMenu("Tour");
        findTourMenuItem = new JMenuItem("Find Tour");
        findTourMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {terrain.findTour();} });
        showMetricsMenuItem = new JMenuItem("Show Metrics");
        showMetricsMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {JOptionPane.showMessageDialog(terrain, terrain.getMetricsMessage());} });

        tourMenu.add(findTourMenuItem);
        tourMenu.add(showMetricsMenuItem);
        menuBar.add(tourMenu);
        
        //Set the menu bar and add the label to the content pane.
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(terrain, BorderLayout.CENTER);
        terrain.addMouseListener(terrain);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    //Define the terrain that will contain the obstacles, start an d end points
    @SuppressWarnings("serial")
	static class Terrain extends JPanel implements MouseListener {
    	
    	private static ArrayList<City> destinations = new ArrayList<City>();
    	private static City startPoint = null;
    	private static Tour tour = null;
    	private static boolean routingAttempted = false;
    	private static boolean routefound = false;
    	    	
    	public void paintComponent(Graphics g) {
        	
            super.paintComponent(g);

            //Draw cities
            g.setColor(Color.BLACK);
            for (City city : destinations) {
            	if (city != null) {
            		g.fillOval(Double.valueOf(city.getxCoordinate()).intValue() - START_END_DIMENSION / 2, Double.valueOf(city.getyCoordinate()).intValue() - START_END_DIMENSION / 2, START_END_DIMENSION, START_END_DIMENSION);
            	}
            }
            
            //Draw start point
            g.setColor(Color.GREEN);
            if (startPoint != null) {
            	g.fillOval(Double.valueOf(startPoint.getxCoordinate()).intValue() - START_END_DIMENSION / 2, Double.valueOf(startPoint.getyCoordinate()).intValue() - START_END_DIMENSION / 2, START_END_DIMENSION, START_END_DIMENSION);
            }
                        
        	if (tour != null) {
        		g.setColor(Color.ORANGE);
        		drawTour(g, tour);
        	}
    }

		@Override
		public void mouseClicked(MouseEvent e) {
			
			switch (selectedMode) {
			
			case ADD_CITIES_MODE:
				if (!routingAttempted) {
					destinations.add(new City(e.getX(), e.getY()));
				}
				break;
								
			case ADD_START_MODE:
				if (!routingAttempted) {
					startPoint = new City(e.getX(), e.getY());
				}
				break;
											
			}
			
			repaint();
		}
		
		public void findTour() {
			if (!routingAttempted && startPoint != null) {
				try {
					TravellingSalesman travellingSalesman = new TravellingSalesman(startPoint, destinations, selectedTourMode);
					tour = travellingSalesman.getShortestTour();
					if (tour != null) {
						routefound = true;
					}
					routingAttempted = true;
					repaint();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}

	    
	    public void showMetrics() {
	    	
	    }

	    public String getMetricsMessage() {
			
			if (routingAttempted) {
				StringBuffer metricsMessage = new StringBuffer();
				if (routefound) {
					
					metricsMessage.append(tour.getNumberOfStatesExpanded()).append(" states were expanded to find the shortest tour.");
					return metricsMessage.toString();
				} else {
					metricsMessage.append(tour.getNumberOfStatesExpanded()).append(" states were expanded, but the shortest tour was not found.");
					return metricsMessage.toString();
				}
			} else {
				return "The tour has not been found yet.";
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}    	

    }
    
    private static void drawTour(Graphics g, Tour tour) {
    	
    	City previousCity = null;
    	List<City> citiesInTour = tour.getCitiesInTour();
    	for (City city : citiesInTour) {
    		
    		if(previousCity == null) {
    			previousCity = city;
    			continue;
    		}
    		
			g.drawLine(Double.valueOf(previousCity.getxCoordinate()).intValue(), Double.valueOf(previousCity.getyCoordinate()).intValue(), Double.valueOf(city.getxCoordinate()).intValue(), Double.valueOf(city.getyCoordinate()).intValue());
			previousCity = city;
			
    	}
    	
    	//Show return to start city
    	g.setColor(Color.RED);
    	g.drawLine(Double.valueOf(previousCity.getxCoordinate()).intValue(), Double.valueOf(previousCity.getyCoordinate()).intValue(), Double.valueOf(tour.getCitiesInTour().get(0).getxCoordinate()).intValue(), Double.valueOf(tour.getCitiesInTour().get(0).getyCoordinate()).intValue());
    	
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TravellingSalesmanUI().createAndShowGUI();
            }
        });
    }
    
    
}

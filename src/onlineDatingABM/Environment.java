package onlineDatingABM;
import java.util.Random;

import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.distribution.Normal;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
	public static int id = 0;
	
	//the search radius for all agents, sweeped in script 
	public int searchRadius = 5;
	//parameters to decide population size (sweeped) 
	public int gridHeight = 60;
	public int gridWidth = 60;
	public int numAgents = gridHeight * gridWidth;
	
	//this needs to be sweeped to change gender ratio across trials 
	public double _maleP = 0.5; 
	public int numMales = (int)(numAgents * _maleP); 
	public int numFemales = numAgents - numMales; 
	
	public double sd = 0.1; 
	public double likeSD = 0.1; 
	public double meanAttractiveness = 0.5;
	public double meanMessagingProb = 0.5; 
	
	public boolean charts = false; 
	public boolean doneMatching = false; 
	
	//a bag to store all agents 
	public Bag allAgents; 

//	public Environment(long seed) {
//		super(seed);
//		// TODO Auto-generated constructor stub
//	}

	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}
//
//	public Environment(long seed, Class observer, String runTimeFileName) {
//		super(seed, observer, runTimeFileName);
//		// TODO Auto-generated constructor stub
//	}
	
	public void makeAgents() {
		allAgents = new Bag();
		Normal normal = new Normal(0.5, sd, random); // To do: decide what the mean value should be 
		for (int i = 0; i < numAgents; i++) {
			
			/*Generating agent gender in accordance to desired population sex ratio */
	
			int gender = 0; //by default assign male 
			if (i>= numMales) {
				//start creating female agents once the desired number of male agents was generated 
				gender = 1; 
			}
	
			double attractiveness = normal.nextDouble(meanAttractiveness, sd); 
			double messaging = normal.nextDouble(meanMessagingProb, sd); 
			double likeThreshold = normal.nextDouble(attractiveness, likeSD); 
			int agentID = id++;
			
			// randomly generate a number between 0 and 1, round it to the nearest integer to randomly generate preferences 
			Random random = new Random();
			double randomNumber = random.nextDouble();
			int preference = Math.round((float) randomNumber);
			
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			// a Bag is a class in MASON that works similarly to a dynamic array
            //the method gets the objects at the location x,y
            Bag b = sparseSpace.getObjectsAtLocation(x, y);
            while(b != null) {
            	x = random.nextInt(gridWidth);
            	y = random.nextInt(gridHeight);
            	b = sparseSpace.getObjectsAtLocation(x, y);
            	
            }
            Agent a = new Agent(x, y, gender, attractiveness, messaging, preference, agentID, likeThreshold);
            Bag likesSent = new Bag();
            Bag matches = new Bag(); 
            a.likesSent = likesSent;
            a.matches = matches; 
            allAgents.add(a);
	        sparseSpace.setObjectLocation(a, x, y);
	        a.colorByGender(this, a); //TODO: debug this 
	        a.event = schedule.scheduleRepeating(a);
            
		}
	}
	
	/* This function initialized the bag of neighbors each Agent has
	 * Note: This bag is already created with members of the same sex removed
	 * i.e. The bag only contains neighbors of the opposite sex
	 * */
	public void initializeNeighbors(){
		for (int i=0; i<allAgents.numObjs; i++) {
			Agent a = (Agent) allAgents.get(i);
//			System.out.println("Agent A ID = "+a.agentID);
			Bag neighbors; 
			
			neighbors = sparseSpace.getMooreNeighbors(a.x, a.y, searchRadius, sparseSpace.TOROIDAL, false);
			a.neighbors = neighbors; 
			
			//now iterate though the neighbors founds and eliminate agents with the same gender 
			for (int j =0; j<neighbors.numObjs; j++) {
				//extract currently examined agent 
				Agent b = (Agent) neighbors.get(j);
				
				//if the agent has the same gender, remove them from the neighbors bag
				if (a.gender == b.gender) {
					neighbors.remove(j);
				}
			}
		
		}
	}
	
	/* This function checks whether all agents finished swiping on their neighbors
	 * Once all agents are done swiping, finish the simulation. */
	public void doneMatching() {
//		System.out.println("in doneMatching()");
		
		for (int i =0; i< allAgents.numObjs;i++) {
			Agent a = (Agent)allAgents.get(i);
			if (a.noNeighbors==false) {
//				System.out.println("Not done matching!");
				return;
			}
		}
		finish();
		System.out.println("====after finish(): All Agents are done matching!===");
		doneMatching = true; 
	}
	
	public void start() {
		super.start();
		// spaces = Spaces.SPARSE; // do we need this?
		makeSpace(gridWidth, gridHeight);
		makeAgents();
		initializeNeighbors();
		if(observer != null)
			observer.initialize(space, spaces);
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Environment.id = id;
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getNumAgents() {
		return numAgents;
	}

	public void setNumAgents(int numAgents) {
		this.numAgents = numAgents;
	}

	public double getMaleP() {
		return _maleP;
	}

	public void setMaleP(double maleP) {
		this._maleP = maleP;
	}

	public int getNumMales() {
		return numMales;
	}

	public void setNumMales(int numMales) {
		this.numMales = numMales;
	}

	public int getNumFemales() {
		return numFemales;
	}

	public void setNumFemales(int numFemales) {
		this.numFemales = numFemales;
	}

	public double getSd() {
		return sd;
	}

	public void setSd(double sd) {
		this.sd = sd;
	}

	public double getLikeSD() {
		return likeSD;
	}

	public void setLikeSD(double likeSD) {
		this.likeSD = likeSD;
	}

	public double getMeanAttractiveness() {
		return meanAttractiveness;
	}

	public void setMeanAttractiveness(double meanAttractiveness) {
		this.meanAttractiveness = meanAttractiveness;
	}

	public double getMeanMessagingProb() {
		return meanMessagingProb;
	}

	public void setMeanMessagingProb(double meanMessagingProb) {
		this.meanMessagingProb = meanMessagingProb;
	}

	public boolean isCharts() {
		return charts;
	}

	public void setCharts(boolean charts) {
		this.charts = charts;
	}

	public boolean isDoneMatching() {
		return doneMatching;
	}

	public void setDoneMatching(boolean doneMatching) {
		this.doneMatching = doneMatching;
	}

	public Bag getAllAgents() {
		return allAgents;
	}

	public void setAllAgents(Bag allAgents) {
		this.allAgents = allAgents;
	}

}

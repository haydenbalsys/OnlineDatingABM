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
	public int searchRadius = 1;
	//parameters to decide population size (sweeped) 
	public int gridHeight = 3;
	public int gridWidth = 3;
	public int numAgents = gridHeight * gridWidth;
	
	

	//this needs to be sweeped to change gender ratio across trials 
	public double maleP = 0.5; 
	public int numMales = (int)(numAgents * maleP); 
	public int numFemales = numAgents - numMales; 
	
	public int preference = 0; // 0 is attractiveness 1 is messaging
	
	/* To do : decide the parameters that define our normal distributions*/
	public double sd = 0.1; 
	public double likeSD = 0.1; 
	public double meanAttractiveness = 0.5;
	public double meanMessagingProb = 0.5; 
	
	
	public boolean charts = false; 
	
	//a bag to store all agents 
	public Bag allAgents; 

	public Environment(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer, String runTimeFileName) {
		super(seed, observer, runTimeFileName);
		// TODO Auto-generated constructor stub
	}
	
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
			preference = Math.round((float) randomNumber);
			
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			// a Bag is a class in MASON that works similarly to a dynamic array
            //the method gets the objects at the location x,y
            Bag b = sparseSpace.getObjectsAtLocation(x, y);
            while(b != null) {
                /*DO set x = random integer between 0 and gridWidth
                  DO set y = random integer between 0 and gridHeight
                  DO update b with new location*/
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
			
			// ==Print Statements for Testing Purposes == 
//			System.out.println("a.x= "+a.x+" a.y= "+a.y);
//			System.out.println("Neighbors size = "+ neighbors.numObjs);
//			System.out.println("a.neighbors size = "+ a.neighbors.numObjs);
//
//			for (int j =0; j<a.neighbors.size(); j++) {
//				System.out.println("neighbor "+j+" ID= "+((Agent)(a.neighbors.get(j))).agentID);
//
//			}
		
		}
	}
	
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
	}
	
	public void start() {
		super.start();
		spaces = Spaces.SPARSE; // do we need this?
		make2DSpace(spaces, gridWidth, gridHeight);
		makeAgents();
		initializeNeighbors();
	}

}

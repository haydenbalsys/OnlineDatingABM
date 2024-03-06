package onlineDatingABM;
import java.util.Random;
import sim.util.Bag;
import sim.util.distribution.Normal;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
	public static int id = 0;
	
	//the search radius for all agents, sweeped in script 
	public int searchRadius = 30;
	//parameters to decide population size (sweeped) 
	public int gridHeight = 60;
	public int gridWidth = 60;
	public int numAgents = gridHeight * gridWidth;

	public int preference = 0; // 0 is attractiveness 1 is messaging
	
	/* To do : decide the parameters that define our normal distributions*/
	public double sd = 0.1; 
	public double meanAttractiveness = 0.5;
	public double meanMessagingProb = 0.5; 
	
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
			double attractiveness = normal.nextDouble(meanAttractiveness, sd); 
			double messaging = normal.nextDouble(meanMessagingProb, sd); 
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
            Agent a = new Agent(x, y, attractiveness, messaging, preference, agentID);
            allAgents.add(a);
            
		}
	}
	
	public void start() {
		super.start();
		spaces = Spaces.SPARSE; // do we need this?
		make2DSpace(spaces, gridWidth, gridHeight);
		makeAgents();
	}

}

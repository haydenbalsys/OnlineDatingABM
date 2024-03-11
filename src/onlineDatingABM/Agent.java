package onlineDatingABM;

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class Agent implements Steppable, Stoppable {
	
    int x; 
    int y; 
    double attractiveness; 
    double messaging;
    int preference;
    int agentID;
    double likeThreshold; 
    public Bag neighbors; 
   


	public Agent(int x, int y, double attractiveness, double messaging, int preference, int agentID, double likeThreshold) {
		super(); //WHAT IS SEED? 
		this.x = x; 
		this.y = y;
		this.attractiveness = attractiveness;
		this.messaging = messaging;
		this.preference = preference;
		this.agentID = agentID; 
		this.likeThreshold = likeThreshold; 
	}
	
	
	
	

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void step(SimState state) {
		// TODO Auto-generated method stub
		

	}

}

package onlineDatingABM;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
	
	
	/*
	 * Add parameters that will later be reset here 
	 * TBH, I'm not sure which parameters we should put here
	 * */
	
	int totalMatchesMalesHave = 0; 
	int totalMatchesFemalesHave = 0; 

	
	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
		// TODO Auto-generated constructor stub
	}
	
	public void stop(Environment state) {
		// When all agents are done matching, stop 
		if (state.doneMatching == true) {
			event.stop();
		}
	}
	
	public boolean reset(SimState state) {
		/*reset the parameters defined at the top of this class to initial values */
		return true; 
	}
	
	public void countMatchAverage(Environment state) {
		Bag agents = state.allAgents; 
		
	}
	
	
	public boolean nextInterval() {
		/* data.add for data we want to add*/
		
		return false; 
	}
	
	public void step(SimState state) {
		
	}
}

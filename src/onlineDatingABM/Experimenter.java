package onlineDatingABM;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
	
	int totalMatchesMalesHave = 0; 
	int totalMatchesFemalesHave = 0; 

	
	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
		// TODO Auto-generated constructor stub
	}
	
	public void stop(Environment state) {
		// When all agents are done matching, stop 
		System.out.println("HELLO HELLO HELLO HELLO HELLO HELLO");
		if (state.doneMatching == true) {
			event.stop();
		}
	}
	
	public boolean reset(SimState state) {
		/*reset the parameters defined at the top of this class to initial values */
		totalMatchesMalesHave = 0;
		totalMatchesFemalesHave = 0;
		return true; 
	}
	
	public void countMatchesPerGender(Environment state) {
		Bag agents = state.allAgents;
		for (int i=0;i<agents.numObjs;i++) {
			Agent currentAgent = (Agent)agents.get(i);
			
			if (currentAgent.gender==0) {
				//count for males
				totalMatchesMalesHave += currentAgent.matches.numObjs;
			}
			else {
				//count for females 
				totalMatchesFemalesHave += currentAgent.matches.numObjs;
			}
		}
		// ==Print Statements for Testing Purposes == 
		//System.out.println("total male matches= " + totalMatchesMalesHave);
		//System.out.println("total female matches= " + totalMatchesFemalesHave);
	}
	
	
	public boolean nextInterval() {
		/* data.add for data we want to add*/
		data.add(totalMatchesMalesHave);
		data.add(totalMatchesFemalesHave);
		/* compute and add averages here */
		return false; 
	}
	
	public void step(SimState state) {
		super.step(state);
		if(((Environment)state).paramSweeps && getdata){
			reset(state);
			countMatchesPerGender((Environment)state);
			nextInterval();
		}
		
	}
}

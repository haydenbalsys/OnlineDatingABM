package onlineDatingABM;

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class Agent implements Steppable, Stoppable {
	public Stoppable event; 
	int x;
	int y;
	int gender; // gender = 0 is male, gender = 1 is female
	double attractiveness;
	double messaging;
	int preference; // attractiveness = 0, messaging = 1
	int agentID;
	double likeThreshold;
	public Bag neighbors;
	public Bag likesSent;
	public Bag matches;
	public boolean noNeighbors = false; 

	public Agent(int x, int y, int gender, double attractiveness, double messaging, int preference, int agentID,
			double likeThreshold) {
		super(); // WHAT IS SEED?
		this.x = x;
		this.y = y;
		this.gender = gender;
		this.attractiveness = attractiveness;
		this.messaging = messaging;
		this.preference = preference;
		this.agentID = agentID;
		this.likeThreshold = likeThreshold;
	}

	public void colorByGender(Environment state, Agent a) {
		switch (a.gender) {
		case 0:
			state.gui.setOvalPortrayal2DColor(a, (float) 0, (float) 0, (float) 1, (float) 1); // color males blue
			break;
		case 1:
			state.gui.setOvalPortrayal2DColor(a, (float) 1, (float) 0, (float) 0, (float) 1); // color males blue
			break;
		default:
			state.gui.setOvalPortrayal2DColor(a, (float) 0, (float) 1, (float) 0, (float) 1); // color males blue
			break;
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		System.out.println("In stop(): Agent ID= "+agentID+" has no neighbors left to swipe on.");
	}

	public boolean sendLike(Agent profile) {

		// check our own strategy
		if (preference == 1) {
			// if prefer messaging -> evaluate profile messaging
			if (profile.messaging > likeThreshold) {
				// if they meet our threshold, send a like
				return true;
			}

		} else {
			// if prefer attractiveness -> evaluate profile attractiveness
			if (profile.attractiveness > likeThreshold) {
				// if they meet our threshold, send a like
				return true;
			}
		}
		return false;
	}

	@Override
	public void step(SimState state) {

		// Check: Do I still have neighbors I haven't swiped on?
		if (neighbors.numObjs > 0) {
			// If so, pull x more neighbors / however many are left if less than x (we need
			// to decide what x should be)
			int batchSize = 1;
			// check that there are enough matches to create a batch of 10
			if (neighbors.numObjs < batchSize) {
				// if there are less than 10 agents, swipe on however many neighbors are left
				batchSize = neighbors.numObjs;
			}

			for (int i = 0; i < batchSize; i++) {

				// look at the first agent in your neighbors bag to swipe on them
				Agent currentSwipe = (Agent) neighbors.get(0);
				// evaluate the profile to decide if you should send a like
				if (sendLike(currentSwipe)) {
					// if we send a like, we add the agent to our like list
					likesSent.add(currentSwipe);

					/* check if I am in this Agent's like list to create a match! */
					// iterate through all the likes they sent to other agents
					// NOTE: this code MAY be simplified by using
					// "currentSwipe.likesSent.get(agentID)"

					// check that the agents has any likes sent, if so check if we're one of their
					// likes
					if (currentSwipe.likesSent.numObjs > 0) {
						for (int j = 0; j < currentSwipe.likesSent.numObjs; j++) {
							Agent theirLike = (Agent) currentSwipe.likesSent.get(j);
							// check if I am one of the profiles they sent a like to
							if (theirLike.agentID == this.agentID) {

								// this is a match!
								// add to my match bag AND their match bag
								matches.add(currentSwipe); // adding them to my match bag
								currentSwipe.matches.add(this); // adding myself to their match bag

								// remove from my likes bag AND their likes bag
								likesSent.remove(currentSwipe); // remove them from my likesSent
								currentSwipe.likesSent.remove(this); // remove myself from their likesSent
								
								break; //match found, no need to keep iterating through their like list
							}
						}
					}
				}

				neighbors.remove(0);

			}
		}
		else {
			noNeighbors = true;
			stop();
			event.stop(); // I believe this is what ACTUALLY removes the agent from the schedule
			((Environment)state).doneMatching(); //call this Environment function to identify if all other agents are done matching 
			
		}
	}

}

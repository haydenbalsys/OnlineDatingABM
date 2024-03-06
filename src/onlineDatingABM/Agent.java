package onlineDatingABM;

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;

public class Agent extends SimState implements Steppable, Stoppable {

	public Agent(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	public Agent(MersenneTwisterFast random, Schedule schedule) {
		super(random, schedule);
		// TODO Auto-generated constructor stub
	}

	public Agent(long seed, Schedule schedule) {
		super(seed, schedule);
		// TODO Auto-generated constructor stub
	}

	public Agent(MersenneTwisterFast random) {
		super(random);
		// TODO Auto-generated constructor stub
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

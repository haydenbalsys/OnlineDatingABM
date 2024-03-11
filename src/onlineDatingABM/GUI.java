package onlineDatingABM;

import java.awt.Color;

import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class GUI extends GUIStateSweep {

	public GUI(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
		// TODO Auto-generated constructor stub
	}

	public GUI(SimStateSweep state) {
		super(state);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI.initialize(Environment.class, null, GUI.class, 400, 400, Color.WHITE, Color.BLUE, true, Spaces.SPARSE);
		
	}

}

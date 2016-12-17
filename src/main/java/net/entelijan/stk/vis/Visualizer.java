package net.entelijan.stk.vis;

public class Visualizer {

	private static final String[] vis = { 
			"*          ", 
			" *         ", 
			"  *        ", 
			"   *       ", 
			"    *      ", 
			"     *     ", 
			"      *    ", 
			"       *   ", 
			"        *  ", 
			"         * ",
			"          *"
	};
	
	private static final String empty = "           ";

	private double from;
	private double to;
	private double diff;
	

	public Visualizer(double from, double to) {
		super();
		this.from = from;
		this.to = to;
		assert (from < to);
		this.diff = to - from;
	}

	public String visualize(double val) {
		if (val < from || val > to) {
			return empty;
		} else {
			int ival = Double.valueOf(Math.floor(10 * val / diff)).intValue();
			return vis[ival];
		}
	}

}

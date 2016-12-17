package net.entelijan.stk.vis;

public class Visualizer {

	private double from;
	private double to;
	private double diff;

	private final int size;
	
	private String[] vis;

	private String empty;

	public Visualizer(double from, double to, int size) {
		super();
		this.from = from;
		this.to = to;
		assert (from < to);
		this.diff = to - from;
		assert(size > 0);
		this.size = size;
		vis = createVis();
		empty = createEmpty();
	}

	private String[] createVis() {
		String[] re1 = new String[size + 1];
		for (int j = 0; j <= size; j++) {
			char[] re = new char[size + 1];
			for (int i = 0; i <= size; i++) {
				if (i == j)
					re[i] = '\u2022';
				else if (i == 0)
					re[i] = '|';
				else if (i == size)
					re[i] = '|';
				else
					re[i] = ' ';
			}
			re1[j] = new String(re);
		}
		return re1;
	}

	private String createEmpty() {
		char[] re = new char[size + 1];
		for (int i = 0; i <= size; i++) {
			re[i] = ' ';
		}
		return new String(re);
	}

	public String visualize(double val) {
		if (val < from || val > to) {
			return empty;
		} else {
			int ival = Double.valueOf(Math.floor(size * val / diff)).intValue();
			return vis[ival];
		}
	}

}

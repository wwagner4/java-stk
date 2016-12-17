package net.entelijan.stk.slope;

public class Slope {

	private Slope() {
	}
	
	enum State {
		BEFORE_START, STARTED, STOPPED, AFTER_STOPPED
	}

	public static ISlope linear(double duration, double from, double to) {
		final double delta = (to - from)  / (duration * ISlope.FRAME_RATE);
		return new ISlope() {

			private State state = State.BEFORE_START;
			private double val = from;

			@Override
			public void start() {
				state = State.STARTED;
			}

			@Override
			public void stop() {
				if (state == State.STARTED) {
					state = State.STOPPED;
				}
			}

			@Override
			public double nextVal() {
				if (!finished()) {
					switch (state) {
					case BEFORE_START:
						return val;
					case STARTED:
						val += delta;
						if (delta < 0) {
							return Math.max(val, to);
						} else {
							return Math.min(val, to);
						}
					case STOPPED:
						return val;
					case AFTER_STOPPED:
						return val;
					default:
						throw new IllegalStateException("Unknown state: " + state);
					}
				} else {
					return to;
				}
			}

			@Override
			public boolean finished() {
				if (delta < 0) {
					return val <= to;
				} else {
					return val >= to;
				}
			}

		};
	}

}

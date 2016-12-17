package net.entelijan.stk.slope;

@FunctionalInterface
public interface ISlopeFactory {
	
	ISlope slope(double duration, double from, double to);

}

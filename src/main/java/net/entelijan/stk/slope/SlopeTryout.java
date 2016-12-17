package net.entelijan.stk.slope;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.entelijan.stk.vis.Visualizer;

public class SlopeTryout {

	public static void main(String[] args) {
		runExec();
		
	}

	static void runLinearSlopeValues() {
		{
			System.out.printf("-- Ascending slope in 20 steps\n");
			ISlope ls = Slope.linear(0.1, 10.0, 20);
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			ls.start();
			System.out.printf("-- started\n");
			for (int i = 0; i < 18; i++) {
				ls.nextVal();
			}
			System.out.printf("S #19 %5.2f\n", ls.nextVal());
			System.out.printf("S #20 %5.2f\n", ls.nextVal());
			System.out.printf("S #21 %5.2f\n", ls.nextVal());
			System.out.printf("S #22 %5.2f\n", ls.nextVal());
		}
		{
			System.out.printf("-- Descending slope in 20 steps\n");
			ISlope ls = Slope.linear(0.1, 200.0, 0);
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			ls.start();
			System.out.printf("-- started\n");
			for (int i = 0; i < 16; i++) {
				ls.nextVal();
			}
			System.out.printf("S #17 %5.2f\n", ls.nextVal());
			System.out.printf("S #18 %5.2f\n", ls.nextVal());
			System.out.printf("S #19 %5.2f\n", ls.nextVal());
			System.out.printf("S #20 %5.2f\n", ls.nextVal());
			System.out.printf("S #21 %5.2f\n", ls.nextVal());
			System.out.printf("S #22 %5.2f\n", ls.nextVal());
		}
		{
			System.out.printf("-- Ascending slope in less than one step\n");
			double fl = 1.0 / ISlope.FRAME_RATE;
			System.out.printf("-- FrameLength %5.2f seconds\n", fl);

			ISlope ls = Slope.linear(fl * 0.9, 10., 20);
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			ls.start();
			System.out.printf("-- started\n");
			System.out.printf("S #1 %5.2f\n", ls.nextVal());
			System.out.printf("S #2 %5.2f\n", ls.nextVal());
			System.out.printf("S #3 %5.2f\n", ls.nextVal());
			System.out.printf("S #4 %5.2f\n", ls.nextVal());
		}
		{
			System.out.printf("-- Descending slope in less than one step\n");
			double fl = 1.0 / ISlope.FRAME_RATE;
			System.out.printf("-- FrameLength %5.2f seconds\n", fl);

			ISlope ls = Slope.linear(fl * 0.1, 100., -20);
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			System.out.printf("BS %5.2f\n", ls.nextVal());
			ls.start();
			System.out.printf("-- started\n");
			System.out.printf("S #1 %5.2f\n", ls.nextVal());
			System.out.printf("S #2 %5.2f\n", ls.nextVal());
			System.out.printf("S #3 %5.2f\n", ls.nextVal());
			System.out.printf("S #4 %5.2f\n", ls.nextVal());
		}
	}

	static void runExec() {
		ScheduledExecutorService es  = Executors.newScheduledThreadPool(20);
		
		runExecOneSlope(es);
//		runExecTwoSlope(es);
		
		es.shutdown();
	}

	static void runExecOneSlope(ScheduledExecutorService es) {
		Param p = new TParam(0, new Visualizer(0, 100, 20));
		ParamExec e = new ParamExec(p);
		
		e.runSlope(0.05, 100, (d, f, t) -> Slope.linear(d, f, t), es);
		pause(10);
		e.start();
		System.out.println("-- started");
		pause(100);
	}

	static void runExecTwoSlope(ScheduledExecutorService es) {
		Param p = new TParam(0, new Visualizer(0, 100, 30));
		ParamExec e = new ParamExec(p);
		
		e.runSlope(0.01877, 100, (d, f, t) -> Slope.linear(d, f, t), es);
		pause(10);
		e.start();
		System.out.println("-- started 01");
		pause(20);
		e.runSlope(0.02324, 0, (d, f, t) -> Slope.linear(d, f, t), es);
		pause(20);
		e.start();
		System.out.println("-- started 02");

		pause(80);
	}

	private static void pause(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			// Nothing to do here
		}
	}

}

class ParamExec {

	long periode = Double.valueOf(1000000.0 / ISlope.FRAME_RATE).longValue();

	private Param param;
	
	Optional<ISlope> slope = Optional.empty();

	Optional<ScheduledFuture<?>> process = Optional.empty();

	public ParamExec(Param param) {
		this.param = param;
	}

	public void start() {
		slope.ifPresent(s -> s.start());
	}

	public void runSlope(double duration, double to, ISlopeFactory slopeFactory, ScheduledExecutorService es) {
		
		process.ifPresent(p -> {
			p.cancel(true);
			System.out.println("-- cancelled process");
		});
		
		slope = Optional.of(slopeFactory.slope(duration, param.getValue(), to));

		Runnable run = new Runnable() {

			@Override
			public void run() {
				slope.ifPresent(s -> param.setValue(s.nextVal()));
			}

		};
		process = Optional.of(es.scheduleAtFixedRate(run, 0, periode, TimeUnit.MICROSECONDS));
	}

}

interface Param {

	void setValue(double v);

	double getValue();
}

class TParam implements Param {

	private double val;
	private Visualizer vis;
	
	public TParam(double val, Visualizer vis) {
		this.val = val;
		this.vis = vis;
	}

	@Override
	public void setValue(double val) {
		System.out.printf("-> TParam setValue %10.3f    %s\n", val, vis.visualize(val));
		this.val = val;
	}

	@Override
	public double getValue() {
		System.out.printf("<- TParam getValue %10.3f\n", val );
		return val;
	}

}

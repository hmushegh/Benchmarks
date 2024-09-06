package App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

//@BenchmarkMode(Mode.Throughput) 
@BenchmarkMode(Mode.AverageTime) 
@Warmup(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
public class BenchmarksLoopsStreams {

	@State(Scope.Benchmark)
	public static class Params {

		public int listSize = 200000; // 200K
	}
	
	public static List<Integer> l1 = new ArrayList<Integer>();	
	public static List<Integer> l2 = new LinkedList<Integer>();
	public static List<List<Integer>> list = fillUpLists();

	@Benchmark
	public static List<List<Integer>> fillUpLists() {

		Params obj = new Params();
		int k = 0;
		while (k < obj.listSize) {
			l1.add(ThreadLocalRandom.current().nextInt(0,1000));
			l1.add(null);
			
			l2.add(ThreadLocalRandom.current().nextInt(100,200));
			l2.add(null);
			k++;
		}
		
		List<List<Integer>> l3 =  Arrays.asList(l1, null, l2);
		return l3;
		
	}
	
	
	@Benchmark
	public static void testForLoops(Blackhole blackhole) {
		 
	    long sum = 0;
	   
	    for (List<Integer> lt: list) {
	    	 if (lt != null) {
	    		 for (Integer i : lt)
	    			 if (i != null) {
	    	            sum += i;		    	            
	    			 }    	 
	    	 }	    	
	    }
	   
	    blackhole.consume(sum);
	}
	
	@Benchmark
	public static void testStreams(Blackhole blackhole) {

		 long sum = 0;
		 for (List<Integer> lt: list) {
			 if (lt != null) {
				 sum += lt.stream()
					.filter(Objects::nonNull)
					  .mapToInt(Integer::intValue)
					  .sum();				
			 	
			 }
		 }
				 
		 blackhole.consume(sum);
	}
	
	
	
	@Benchmark
	public static void testParallelStreams(Blackhole blackhole) {

		 long sum = 0;
		 for (List<Integer> lt: list) {
			 if (lt != null) {
				 sum += lt.parallelStream()
					.filter(Objects::nonNull)
					  .mapToInt(Integer::intValue)
					  .sum();				
			 	
			 }
		 }
				 
		 blackhole.consume(sum);
	}
	
	
	
   public static void main(String[] args) throws Exception {
		org.openjdk.jmh.Main.main(args);
	}

}

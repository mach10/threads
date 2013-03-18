package uk.co.mach10.app.task;

import java.util.concurrent.CountDownLatch;

public class LatchHarness {
	public long timeTasks(int nThreads, final Runnable task)throws InterruptedException{
		final CountDownLatch startingGate = new CountDownLatch(1);
		final CountDownLatch endingGate = new CountDownLatch(nThreads);
		
		for(int i=0; i<nThreads; i++){
			Thread t = new Thread(){
				public void run(){
					try{
						System.out.println(Thread.currentThread().getName()+" awaiting starting gate");
						startingGate.await();
						try{
							task.run();
						}finally{
							endingGate.countDown();
						}
					}catch(InterruptedException ignored){}
				}
			};
			t.start();
		}
		
		long start = System.nanoTime();
		startingGate.countDown();
		endingGate.await();
		long end = System.nanoTime();
		return end-start;
	}

}

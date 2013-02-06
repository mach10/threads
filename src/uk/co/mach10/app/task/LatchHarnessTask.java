package uk.co.mach10.app.task;

public class LatchHarnessTask implements Runnable{

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		System.out.println("brought to you courtesy of "+threadName);
		try {
			Thread.sleep(2000);
			System.out.println(threadName+" game over");
		} catch (InterruptedException e) {}
		
	}

}

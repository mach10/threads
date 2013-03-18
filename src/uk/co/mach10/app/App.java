package uk.co.mach10.app;

import uk.co.mach10.app.task.LatchHarness;
import uk.co.mach10.app.task.LatchHarnessTask;
import uk.co.mach10.app.task.LifeCycleWebClient;
import uk.co.mach10.app.task.LifecycleWebServer;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
    	
        App.webServerTest();
        
    }
    
    private static void latchHarnessTest(){
    	LatchHarness latchHarness = new LatchHarness();
    	try {
			long timeTaken = latchHarness.timeTasks(10, new LatchHarnessTask());
			System.out.println("those ten tasks took "+timeTaken+" nanos to complete");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static void webServerTest(){
    	System.out.println("welcome to the threads project");
        LifecycleWebServer webServer = new LifecycleWebServer();
        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();  //@todo handle this
        }
        
        //LifeCycleWebClient client = new LifeCycleWebClient();
        //client.connect();
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("stopping the web server");
			webServer.stop();
		}
    }
}

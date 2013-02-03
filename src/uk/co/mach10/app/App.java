package uk.co.mach10.app;

import uk.co.mach10.app.task.LifecycleWebServer;

import java.io.IOException;

/**
 * Copyright: Rob Shield 2012
 * rotovol - volunteer administration system
 */
public class App {

    public static void main(String[] args) {
        System.out.println("welcome to the threads project");
        LifecycleWebServer webServer = new LifecycleWebServer();
        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();  //@todo handle this
        }
    }
}

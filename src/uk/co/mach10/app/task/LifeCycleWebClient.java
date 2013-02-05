package uk.co.mach10.app.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LifeCycleWebClient {

	public void connect(){
		InetAddress addr;
		try {
			addr = InetAddress.getByName(null);
			Socket socket = new Socket(addr, LifecycleWebServer.PORT_NUMBER);
			BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()));
			PrintWriter out =
                new PrintWriter(
                    new BufferedWriter(
                        new OutputStreamWriter(
                            socket.getOutputStream())),true);
            for(int i = 0; i < 10; i ++) {
                out.println("howdy: " + String.valueOf(i));

                String str = in.readLine();

                System.out.println(str);

            }
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        
	}
}

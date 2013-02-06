package uk.co.mach10.app.task;

import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class LifecycleWebServer {
    public static final int PORT_NUMBER = 9000;
	private final ExecutorService exec = Executors.newFixedThreadPool(10);
    private final int bufferLength = 256;

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(PORT_NUMBER);

        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handleRequest(conn);
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                });
            } catch (RejectedExecutionException ree) {
                if (!exec.isShutdown()) {
                    System.out.println("task submission rejected");
                }
            }
        }
    }

    public void stop() {
        exec.shutdown();
    }

    private void handleRequest(Socket conn) throws IOException {
    	System.out.println("Handled by "+Thread.currentThread().getName());
        InputStream inputStream = conn.getInputStream();
        OutputStream requestOutputStream = conn.getOutputStream();
        writeResponse(inputStream, requestOutputStream);
        conn.close();
    }

    private void writeResponse(InputStream inputStream, OutputStream requestOutputStream) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(requestOutputStream, Charset.forName("UTF-8"));
        BufferedReader inReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = buildBodyFromBufferedReader(inReader);
//        StringBuilder builder = buildBodyFromInputStream(inputStream);

        String body = body(builder);

        writeContent(out, body);
        out.flush();
        out.close();
    }

    /**
     * just wanted to see why inputStream.read
     * never seemed to terminate. Read on...
     * @param inputStream
     * @return
     */
    private StringBuilder buildBodyFromInputStream(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        byte[] buffer = new byte[bufferLength];
        boolean finished = false;
        try {
            while (!finished) {
                int read = inputStream.read(buffer, 0, bufferLength);
                builder.append(new String(buffer, 0, read));
                finished = read<bufferLength;//HTTP 1.1 defaults to keep connection open, meaning inputStream.read
                                            //never receives EOF, so will never terminate. This overcomes that.
            }
        } catch (IOException e) {
            builder.append(e.getMessage());
        } finally {
            try {
                if (null != inputStream)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder;
    }

    /**
     * html table representation of request headers
     * @param inReader
     * @return
     * @throws IOException
     */
    private StringBuilder buildBodyFromBufferedReader(BufferedReader inReader) throws IOException {
        StringBuilder builder = new StringBuilder("<table>");
        String inputLine = "";
        while (!(inputLine = inReader.readLine()).equalsIgnoreCase("")) {
            if (inputLine.contains(": ")) {
                String[] split = inputLine.split(": ");
                if (split.length == 2) {
                    builder.append("<tr><td>").append(split[0])
                            .append("</td><td>").append(split[1])
                            .append("</td></tr>");
                }

            } else {
                builder.append("<tr><td colspan='2'>").append(inputLine).append("</tr></tr>");
            }
        }
        builder.append("</table>");
        return builder;
    }

    private void writeContent(OutputStreamWriter out, String body) throws IOException {
        writeHeaders(out, body);
        out.write(body.toString());
    }

    private void writeHeaders(OutputStreamWriter out, String body) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Date: Sat, 02 Feb 2013 23:59:59 GMT\r\n");
        out.write("Server: Apache/0.8.4\r\n");
        out.write("Content-Type: text/html\r\n");
        out.write("Content-Length: " + body.getBytes("UTF-8").length + "\r\n");
        out.write("Expires: Sat, 01 Jan 2014 00:59:59 GMT\r\n");
        out.write("Last-modified: Sat, 02 Feb 2013 23:59:59 GMT\r\n");
        out.write("\r\n");
    }

    private String body(StringBuilder requestHeaders) {
        StringBuilder builder = new StringBuilder("<html>");
        builder.append("<header><title>test page</title></header>")
                .append("<body><p>response page</p>")
                .append("<div>").append(requestHeaders)
                .append("</div>").append("</body></html>");
        return builder.toString();
    }
}

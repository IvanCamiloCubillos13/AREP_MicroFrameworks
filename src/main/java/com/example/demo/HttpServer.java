package com.example.demo;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class HttpServer {
    static Map<String, WebMethod> endPoints = new HashMap();
    public static void main(String[] args) throws IOException, URISyntaxException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running=true;
        while (running){
                try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean isFirstLine = true;
            String reqpath = "";

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);

                if(isFirstLine){
                    String[] flTokens = inputLine.split(" ");
                    String method = flTokens[0];
                    String struripath = flTokens[1];
                    String protocolversion = flTokens[2];

                    URI uripath = new URI(struripath);
                    reqpath = uripath.getPath();

                    isFirstLine = false;
                }

                if (!in.ready()) {
                    break;
                }
            }

            WebMethod currentWm = endPoints.get(reqpath);

            if(currentWm != null){
                outputLine = "HTTP/1.1 200 OK\n\r"
                    +    "Content-Type: text/html\n\r" 
                    +    "\n\r"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Title of the document</title>\n"
                    + "</head>"
                    + "<body>"
                    + currentWm.execute()
                    + "</body>"
                    + "</html>";

            } else {
                outputLine = "HTTP/1.1 200 OK\n\r"
                    +    "Content-Type: text/html\n\r" 
                    +    "\n\r"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Title of the document</title>\n"
                    + "</head>"
                    + "<body>"
                    + "My Web Site"
                    + "</body>"
                    + "</html>";
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void get(String path, WebMethod wm){
        endPoints.put(path, wm);
    }
}
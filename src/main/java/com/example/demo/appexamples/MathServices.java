package com.example.demo.appexamples;

import static com.example.demo.HttpServer.get;
import static com.example.demo.HttpServer.staticfiles;

import java.io.IOException;
import java.net.URISyntaxException;

import com.example.demo.HttpServer;

public class MathServices {
    public static void main(String[] args) throws IOException, URISyntaxException {
        
        staticfiles("/webroot");
        get("/App/pi", (req, res) -> "PI=" + Math.PI);
        get("/App/hello", (req, res) -> "hello " + req.getValue("name"));
        HttpServer.main(args);
    }
}

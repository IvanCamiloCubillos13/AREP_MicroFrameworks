package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoApplicationTests {

    @BeforeEach
    void resetServer() {
        HttpServer.endPoints.clear();
        HttpServer.staticFilesPath = null;
    }


    @Test
    void testGetValues_singleParam() {
        HttpRequest req = new HttpRequest("name=Pedro");
        assertEquals("Pedro", req.getValues("name"));
    }

    @Test
    void testGetValues_multipleParams() {
        HttpRequest req = new HttpRequest("name=Pedro&age=25");
        assertEquals("Pedro", req.getValues("name"));
        assertEquals("25", req.getValues("age"));
    }

    @Test
    void testGetValues_missingParam_returnsEmpty() {
        HttpRequest req = new HttpRequest("name=Pedro");
        assertEquals("", req.getValues("notexists"));
    }

    @Test
    void testGetValues_nullQuery_returnsEmpty() {
        HttpRequest req = new HttpRequest(null);
        assertEquals("", req.getValues("name"));
    }

    @Test
    void testGetValues_emptyQuery_returnsEmpty() {
        HttpRequest req = new HttpRequest("");
        assertEquals("", req.getValues("name"));
    }

    @Test
    void testGetValue_aliasWorks() {
        HttpRequest req = new HttpRequest("name=Ivan");
        assertEquals("Ivan", req.getValue("name"));
    }

    @Test
    void testGetMethod_registersEndpoint() {
        HttpServer.get("/hello", (req, res) -> "hello world");
        assertTrue(HttpServer.endPoints.containsKey("/hello"));
    }

    @Test
    void testGetMethod_lambdaExecutesCorrectly() {
        HttpServer.get("/hello", (req, res) -> "hello world");
        WebMethod wm = HttpServer.endPoints.get("/hello");
        String result = wm.execute(new HttpRequest(""), new HttpResponse());
        assertEquals("hello world", result);
    }

    @Test
    void testGetMethod_lambdaUsesQueryParam() {
        HttpServer.get("/hello", (req, res) -> "hello " + req.getValues("name"));
        WebMethod wm = HttpServer.endPoints.get("/hello");
        String result = wm.execute(new HttpRequest("name=Pedro"), new HttpResponse());
        assertEquals("hello Pedro", result);
    }

    @Test
    void testGetMethod_multipleEndpoints() {
        HttpServer.get("/hello", (req, res) -> "hello");
        HttpServer.get("/pi", (req, res) -> String.valueOf(Math.PI));
        assertEquals(2, HttpServer.endPoints.size());
        assertTrue(HttpServer.endPoints.containsKey("/hello"));
        assertTrue(HttpServer.endPoints.containsKey("/pi"));
    }

    @Test
    void testGetMethod_piReturnsCorrectValue() {
        HttpServer.get("/pi", (req, res) -> String.valueOf(Math.PI));
        WebMethod wm = HttpServer.endPoints.get("/pi");
        String result = wm.execute(new HttpRequest(""), new HttpResponse());
        assertEquals(String.valueOf(Math.PI), result);
    }

    @Test
    void testGetMethod_overwritesExistingEndpoint() {
        HttpServer.get("/hello", (req, res) -> "first");
        HttpServer.get("/hello", (req, res) -> "second");
        WebMethod wm = HttpServer.endPoints.get("/hello");
        assertEquals("second", wm.execute(new HttpRequest(""), new HttpResponse()));
    }

    @Test
    void testGetMethod_unknownEndpoint_returnsNull() {
        assertNull(HttpServer.endPoints.get("/notregistered"));
    }

    @Test
    void testStaticfiles_setsPath() {
        HttpServer.staticfiles("/webroot");
        assertEquals("/webroot", HttpServer.staticFilesPath);
    }

    @Test
    void testStaticfiles_defaultIsNull() {
        assertNull(HttpServer.staticFilesPath);
    }

    @Test
    void testStaticfiles_canBeOverwritten() {
        HttpServer.staticfiles("/webroot");
        HttpServer.staticfiles("/public");
        assertEquals("/public", HttpServer.staticFilesPath);
    }

    @Test
    void testWebMethod_lambdaImplementation() {
        WebMethod wm = (req, res) -> "test response";
        assertEquals("test response", wm.execute(new HttpRequest(""), new HttpResponse()));
    }

    @Test
    void testWebMethod_canReturnNumericString() {
        WebMethod wm = (req, res) -> String.valueOf(42);
        assertEquals("42", wm.execute(new HttpRequest(""), new HttpResponse()));
    }
}
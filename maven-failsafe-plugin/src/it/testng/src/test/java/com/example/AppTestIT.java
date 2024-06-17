package com.example;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTestIT {

    @Test
    public AppTestIT(String testName) {
        App myClass = new App();
        String message = myClass.getMessage();
        assertEquals(message, "Hello World!");
    }

}

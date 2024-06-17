package com.example;

public class AppTest {
    public void setUp() {
        // Setup code here
        System.out.println("Setup before test");
    }

    public void tearDown() {
        // Teardown code here
        System.out.println("Teardown after test");
    }

    public void testMyApp() {
        setUp();
        try {
            // Create a new MyPojo instance
            App pojo = new App("John Doe", 30);

            // Test getters
            assert "John Doe".equals(pojo.getName()) : "Expected name 'John Doe', but got " + pojo.getName();
            assert pojo.getAge() == 30 : "Expected age 30, but got " + pojo.getAge();

            // Test setters
            pojo.setName("Jane Doe");
            pojo.setAge(25);
            assert "Jane Doe".equals(pojo.getName()) : "Expected name 'Jane Doe', but got " + pojo.getName();
            assert pojo.getAge() == 25 : "Expected age 25, but got " + pojo.getAge();

            System.out.println("All tests passed.");
        } finally {
            tearDown();
        }
    }

    public static void main(String[] args) {
        AppTest tester = new AppTest();
        tester.testMyApp();
    }
}

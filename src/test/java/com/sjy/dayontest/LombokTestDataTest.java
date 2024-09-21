package com.sjy.dayontest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LombokTestDataTest {

    @Test
    public void testDataTest() {
        TestData testData = new TestData();
        testData.setName("진영");

        Assertions.assertEquals("진영", testData.getName());
    }
}
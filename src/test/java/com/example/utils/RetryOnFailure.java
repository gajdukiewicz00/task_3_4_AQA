package com.example.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryOnFailure implements IRetryAnalyzer {
    private int count=0, max=1;
    @Override public boolean retry(ITestResult result) { return count++ < max; }
}
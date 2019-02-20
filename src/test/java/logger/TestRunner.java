package logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CyclicDetectionTester.class,
                SimpleTester.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if(result.wasSuccessful()){
            System.out.println("Success!");
        } else{
            System.out.println("Failure!");
        }
    }
}

package logger;

import annotations.Log;

class TestCycleInner implements Loggable{

    TestCycleOuter outer;

    @Log
    String s = "inner";

    void setOuter(TestCycleOuter outer){
        this.outer = outer;
    }

}

class TestCycleOuter implements Loggable{

    TestCycleInner inner;

    @Log
    String s = "outer";

    void setInner(TestCycleInner inner) {
        this.inner = inner;
    }
}
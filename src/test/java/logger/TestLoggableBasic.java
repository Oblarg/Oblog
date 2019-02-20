package logger;

import annotations.Log;

class TestLoggableBasic implements Loggable {

    TestLoggableBasic(int a){
        this.a = a;
    }

    @Override
    public String configureLogName(){
        return "TestLoggableBasic" + a;
    }

    @Log
    int a;

    @Log
    private int getA(){
        return a;
    }
}

package logger;

import annotations.Log;

class TestLoggableBasic implements Loggable {

    TestLoggableBasic(int a){
        this.a = a;
    }

    @Override
    public String configureLogName(){
        return "logger.TestLoggableBasic" + a;
    }

    @Log
    int a;

    @Log
    private int getB(){
        return 2;
    }
}

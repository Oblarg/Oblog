package io.github.oblarg.oblog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

class SetterRunner implements Executor {

    private List<Runnable> setters = new ArrayList<>();

    @Override
    public synchronized void execute(Runnable command) {
        setters.add(command);
    }

    synchronized void runSynchronous() {
        for (Runnable setter : setters) {
            setter.run();
        }
        setters.clear();
    }
}

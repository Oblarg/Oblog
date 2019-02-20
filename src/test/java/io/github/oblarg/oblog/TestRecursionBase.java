package io.github.oblarg.oblog;

import io.github.oblarg.oblog.annotations.Log;

class TestRecursionBase {

    int a = 1;

    int b = 2;

}

class TestRecursionSuper extends TestRecursionBase implements Loggable {

    @Log
    int b = 10;

    int c = 3;
}

class TestRecursionSub extends TestRecursionSuper {
    @Log
    int d = 4;
}
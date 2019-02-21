package io.github.oblarg.oblog;

import io.github.oblarg.oblog.annotations.Log;

class TestRecursionBase {

    int a = 1;

    int b = 10;

}

class TestRecursionSuper extends TestRecursionBase implements Loggable {

    @Log
    int b = 2;

    int c = 3;

    int d = 10;
}

class TestRecursionSub extends TestRecursionSuper {
    @Log
    int d = 4;
}
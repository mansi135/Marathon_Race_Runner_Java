package com.mycompany.app;

/**
 * @author Mansi
 *
 * Utils class
 */
public class Utils {
    public static void checkState(boolean condition) {
        // Our own 'assert' method, since asserts are usually disabled
        if (!condition) {
            throw new IllegalStateException("Condition failed");
        }
    }
}

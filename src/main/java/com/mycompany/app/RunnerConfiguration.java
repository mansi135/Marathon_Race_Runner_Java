package com.mycompany.app;

/**
 * Every runner has a configuration that dictates the behavior.
 */
public class RunnerConfiguration {
    final String name;
    final int stepsAtATime;
    final double restProbability;

    public RunnerConfiguration(String name, int stepsAtATime, double restProbability) {
        this.name = name;
        Utils.checkState(restProbability >= 0 && restProbability < 1.0);
        Utils.checkState(stepsAtATime >= 0);
        this.stepsAtATime = stepsAtATime;
        this.restProbability = restProbability;
    }

    public int step() {
        return (restProbability > 0 && Math.random() < restProbability) ? 0 : stepsAtATime;
    }
}

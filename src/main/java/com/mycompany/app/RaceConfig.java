package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

public class RaceConfig {
    final int raceLength;
    final int msPerTick;

    List<RunnerConfiguration> runnerConfigurations;

    public RaceConfig() {
        this(IConstants.TRACK_LENGTH, IConstants.SLEEP_TIME);
    }

    public int getNumRacers() {
        return runnerConfigurations.size();
    }

    public RaceConfig(int raceLength, int msPerTick) {
        Utils.checkState(raceLength >= 0);
        Utils.checkState(msPerTick >= 0);
        this.raceLength = raceLength;
        this.msPerTick = msPerTick;
        runnerConfigurations = new ArrayList<>();
    }

    public void add(String name, int speed, int restPercent) {
        addRunnerConfiguration(new RunnerConfiguration(name, speed, restPercent * 0.01));
    }

    public void addRunnerConfiguration(RunnerConfiguration runnerConfiguration) {
        runnerConfigurations.add(runnerConfiguration);
    }

    boolean hasWon(int steps) {
        return steps >= raceLength;
    }
}


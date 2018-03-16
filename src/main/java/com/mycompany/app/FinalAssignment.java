package com.mycompany.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Mansi
 *
 * The main class
 */
public class FinalAssignment {
    /**
     * This class models the global RaceKeeper. There is one of these per race.
     * It ensures that there is only one winner, and that no runner waits if a victory has been declared.
     */
    private static class RaceKeeper {
        private final RaceConfig raceConfig;
        private RunnerConfiguration winningRunner;
        private CyclicBarrier barrier;

        public RaceKeeper(RaceConfig raceConfig) {
            this.winningRunner = null;
            this.raceConfig = raceConfig;
            this.barrier = new CyclicBarrier(raceConfig.getNumRacers());
        }

        public synchronized boolean raceIsStillOn() {
            return winningRunner == null;
        }

        void awaitStartingPoint() throws InterruptedException {
            try {
                barrier.await();
            } catch (BrokenBarrierException be) {
                // This is thrown when another thread was interrupted while we were waiting
                // i.e. when the barrier wouldn't be full filled, treat this like an Interrupted
                // error on this thread to signal termination
                Thread.currentThread().interrupt();
            }
        }

        public boolean tickIfRaceIsOn() throws InterruptedException {
            long curTime = System.currentTimeMillis();
            final long endTimeOfTick = curTime + raceConfig.msPerTick;
            synchronized (this) {
                do {
                    wait(raceConfig.msPerTick);
                } while (raceIsStillOn() && System.currentTimeMillis() < endTimeOfTick);
                return raceIsStillOn();
            }
        }

        /**
         * Returns true iff 'id' has won
         */
        public synchronized boolean advance(final int curPos, final RunnerConfiguration runner) {
            if (raceIsStillOn() && raceConfig.hasWon(curPos)) {
                winningRunner = runner;
                notifyAll();
                return true;
            } else {
                return false;
            }
        }

        public synchronized RunnerConfiguration doRaceAndWaitForWinner() throws InterruptedException {
            while(raceIsStillOn()) {
                wait();
            }
            Utils.checkState(winningRunner != null);
            return winningRunner;
        }
    }

    /**
     * This class models a runner.
     */
    private static class Runner implements Runnable {
        private final RunnerConfiguration config;
        private final RaceKeeper raceKeeper;

        public Runner (RunnerConfiguration config, RaceKeeper raceKeeper) {
            this.config = config;
            this.raceKeeper = raceKeeper;
        }

        @Override
        public void run() {
            try {
                int curPos = 0;
                raceKeeper.awaitStartingPoint();
                while (raceKeeper.tickIfRaceIsOn()) {
                    curPos += config.step();
                    System.out.println(String.format("%s : %d", config.name, curPos));
                    if (raceKeeper.advance(curPos, config)) {
                        System.out.println(String.format("%s : I finished!", config.name));
                    }
                }
            } catch (InterruptedException ie) {
                System.out.println(String.format("Runner %s is interrupted", config.name));
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void doOneRace(final RaceConfig raceConfig) throws InterruptedException {
        RaceKeeper raceKeeper = new RaceKeeper(raceConfig);

        List<Thread> runners = new ArrayList<>();
        for (int i = 0; i < raceConfig.getNumRacers(); ++i) {
            runners.add(new Thread(new Runner(raceConfig.runnerConfigurations.get(i), raceKeeper)));
        }

        runners.forEach(t -> t.start());
        RunnerConfiguration winner = raceKeeper.doRaceAndWaitForWinner();
        for (Thread t : runners) {
            t.join();
        }


        System.out.println(String.format("\nThe race is over! The %s is the winner.", winner.name));
        for (RunnerConfiguration runner : raceConfig.runnerConfigurations) {
            if (runner != winner) {
                System.out.println(String.format("%s: You beat me fair and square.", runner.name));
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Select your data source:\n");
        System.out.println("1.	Derby database");
        System.out.println("2.	XML file");
        System.out.println("3.	Text file");
        System.out.println("4.	Default two runners");
        System.out.println("5.	Exit");
        System.out.println();
    }

    /**
	 * Gets the user choice
	 */
    public static RaceConfig getRaceConfigFromUserInput(){
    	//get valid option from user
    	Scanner sc = new Scanner(System.in);
    	final int action = Validator.getUserChoice(sc, "Enter your choice: ");  
        if (action == IConstants.INVALID_CHOICE) {
            return null;
        } else {
        		RaceConfig defaultConfig = new RaceConfig();
        		try {
        			RaceConfigDAO dao = DAOFactory.getDAO(action);
        			if (dao != null) {
        				return dao.getRaceConfig();
        			} else {
        				return null;
        			}
        		} catch (IOException ie) {
        			System.out.println(String.format("Encountered error with action %d: %s", action, ie.getMessage()));
        		}
        		return defaultConfig;
        }
    }

    public static void main(String args[]) {
        System.out.println("Welcome to the Marathon Race Runner Program");
        while (true) {
            displayMenu();
            final RaceConfig raceConfig = getRaceConfigFromUserInput();
            if (raceConfig == null) {
                // User asked to quit
                break;
            }
            if (raceConfig.getNumRacers() == 0) {
                System.out.println("No racers found, retrying");
                continue;
            }
            System.out.println("Get set...Go!");
            try {
                doOneRace(raceConfig);
            } catch (InterruptedException ie) {
                System.out.println("Got interrupted exception, quitting");
                break;
            }
            System.out.println("Press any key to continue ...");
            try {
                System.in.read();
            } catch (IOException ie) {
                System.out.println("Error waiting for user keypress");
                break;
            }
        }
        System.out.println("Thank you for using my Marathon Race Program");
    }
}

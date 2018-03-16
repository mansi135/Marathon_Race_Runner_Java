package com.mycompany.app;

import java.util.ArrayList;

/**
 * @author Mansi
 *
 * This class creates the default runners for the Marathon.
 */
public class RunnerDefault implements RaceConfigDAO {
	/**
	 * Provides the list of default runners
	 */
    @Override
	public RaceConfig getRaceConfig(){      
        final RaceConfig raceConfig = new RaceConfig();
        raceConfig.add("Tortoise",10,0);
		raceConfig.add("Hare", 100, 90);
		return raceConfig;
	}
}

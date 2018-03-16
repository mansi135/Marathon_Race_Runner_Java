package com.mycompany.app;

import java.io.IOException;

/**
 * @author Mansi
 *
 * Interface for getting a race config from any available data source
 */
public interface RaceConfigDAO {
    RaceConfig getRaceConfig() throws IOException;
}

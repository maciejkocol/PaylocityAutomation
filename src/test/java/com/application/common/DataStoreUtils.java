package com.application.common;

import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.log4j.Logger;

/**
 * @author Maciej Kocol
 * <p>
 * This is the class used to store and retrieve variables between scenarios.
 */
public class DataStoreUtils {

    private final static Logger logger = Logger.getLogger(DataStoreUtils.class);
    private static DataStore dataStore;

    /**
     * Store value at scenario level
     * @param key
     * @param value
     */
    public static void storeStringToScenarioDataStore(String key, String value) {
        dataStore = DataStoreFactory.getScenarioDataStore();
        dataStore.put(key, value);
        logger.info("\"" + key + " : " + value + "\" store in scenario data store.");
    }

    /**
     * Retrieve value at scenario level
     * @param key
     * @return
     */
    public static String fetchStringFromScenarioDataStore(String key) {
        dataStore = DataStoreFactory.getScenarioDataStore();
        return (String) dataStore.get(key);
    }

    /**
     * Retrieves Generic object from Scenario level.
     * @param key
     * @return
     */
    public static <T> T fetchObjectFromScenarioDataStore(String key) {
        dataStore = DataStoreFactory.getScenarioDataStore();
        return (T) dataStore.get(key);
    }

    /**
     * Store object at scenario level.
     *
     * @param key
     * @param object
     */
    public static void storeObjectToScenarioDataStore(String key, Object object) {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put(key, object);
    }

    /**
     * Retrieves Generic object from Suite level.
     * @param key
     * @return
     */
    public static <T> T fetchObjectFromSuiteDataStore(String key) {
        DataStore suite = DataStoreFactory.getSuiteDataStore();
        return (T) suite.get(key);
    }

    /**
     * Stores Generic object at Suite level.
     * @param key
     * @return
     */
    public static void storeObjectToSuiteDataStore(String key, Object object) {
        DataStore suiteStore = DataStoreFactory.getSuiteDataStore();
        suiteStore.put(key, object);
    }
}
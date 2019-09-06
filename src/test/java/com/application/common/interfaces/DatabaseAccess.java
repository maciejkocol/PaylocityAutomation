package com.application.common.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseAccess {

    Connection getConnection() throws SQLException;

    List<Map<String, Object>> getResultsFromQuery(String query);

    void executeUpdateQuery(String query);

    void executeStoredProcedure(String query);
}

package com.fiap.fintech.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

final class DaoUtils {

    private DaoUtils() {
    }

    static Date toSqlDate(java.util.Date date) {
        return new Date(date.getTime());
    }

    static void setNullableInteger(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }

    static boolean getBooleanFromNumber(int value) {
        return value == 1;
    }
}

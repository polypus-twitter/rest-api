/*
    Polypus: a Big Data Self-Deployable Architecture for Microblogging 
    Text Extraction and Real-Time Sentiment Analysis

    Copyright (C) 2017 Rodrigo Martínez (brunneis) <dev@brunneis.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.brunneis.polypus.restapi.model.dao;

import com.brunneis.polypus.restapi.conf.Conf;
import com.brunneis.polypus.restapi.conf.MariadbConf;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;

public final class MySQLPool {

    private static final BasicDataSource DATA_SOURCE = new BasicDataSource();

    private MySQLPool() {

    }

    static {
        DATA_SOURCE.setDriverClassName("org.mariadb.jdbc.Driver");
        DATA_SOURCE.setUrl("jdbc:mariadb://" + ((MariadbConf) Conf.DATABASE.value()).HOST.value()
                + ":" + ((MariadbConf) Conf.DATABASE.value()).PORT.value()
                + "/" + ((MariadbConf) Conf.DATABASE.value()).NAME.value());
        DATA_SOURCE.setUsername(((MariadbConf) Conf.DATABASE.value()).USER.value());
        DATA_SOURCE.setPassword(((MariadbConf) Conf.DATABASE.value()).PASSWORD.value());

        // COMPROBACIONES
        DATA_SOURCE.setValidationQuery("SELECT now()");
        DATA_SOURCE.setTestOnBorrow(true);
        DATA_SOURCE.setTestOnReturn(true);

        // POOL
        DATA_SOURCE.setInitialSize(10);
        DATA_SOURCE.setMaxActive(100);
        DATA_SOURCE.setMaxIdle(50);
    }

    public static Connection getConnection(Integer isolation) throws SQLException {
        Connection con = DATA_SOURCE.getConnection();
        con.setTransactionIsolation(isolation);
        con.setAutoCommit(true);
        return con;
    }

}

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
package com.brunneis.polypus.restapi.conf;

import com.brunneis.locker.Locker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brunneis
 */
public class Conf {

    public final static int MARIADB = 004;

    public final static Locker<String> CONF_FILE = new Locker<>();
    public final static Locker<DbConf> DATABASE = new Locker<>();
    public final static Locker<String> BASE_URL = new Locker<>();

    static {
        if (!CONF_FILE.isLocked()) {
            CONF_FILE.set("polypus-api.conf");
        }

        File file = new File(CONF_FILE.value());
        if (!file.exists()) {
            System.exit(1);
        }

        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(CONF_FILE.value());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Conf.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            properties.load(input);
        } catch (IOException ex) {
            Logger.getLogger(Conf.class.getName()).log(Level.SEVERE, null, ex);
        }

        MariadbConf mdbc = new MariadbConf();
        mdbc.CURRENT.set(MARIADB);
        mdbc.NAME.set(properties.getProperty("DB_NAME"));
        mdbc.HOST.set(properties.getProperty("DB_HOST"));
        mdbc.PORT.set(properties.getProperty("DB_PORT"));
        mdbc.USER.set(properties.getProperty("DB_USER"));
        mdbc.PASSWORD.set(properties.getProperty("DB_PASSWORD"));

        DATABASE.set(mdbc);
        BASE_URL.set(properties.getProperty("API_BASE")
                + ":" + properties.getProperty("API_PORT"));
    }

}

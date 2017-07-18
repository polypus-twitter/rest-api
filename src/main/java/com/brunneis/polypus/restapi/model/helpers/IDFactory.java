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
package com.brunneis.polypus.restapi.model.helpers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brunneis
 */
public class IDFactory {

    private static String previousId = "";

    public final static long REDUCTOR = 260620162016L;
    private final static String ENCODED_HOSTNAME = getEncodedHostname();

    public static synchronized String getNewId() {
        // Current timestamp
        Long timestamp = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

        // Modify the timestamp with a constant value
        timestamp -= REDUCTOR;

        // ID generation
        String result;

        do {
            result
                    = // Random number in [100-999]
                    +(new Random().nextInt(9000) + 1000)
                    // MagicString
                    + ENCODED_HOSTNAME
                    + // Modified timestamp
                    timestamp;
            // Retry when the generated ID is the previous one
        } while (result.equals(previousId));
        previousId = result;

        return result;
    }

    private static String getEncodedHostname() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            // Exit if can't get the hostname
            Logger.getLogger(IDFactory.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        int result = 0;
        for (char c : hostname.toCharArray()) {
            result += c;
        }
        // Encode the hostname
        return Integer.toString((result % 900) + 100);
    }

}
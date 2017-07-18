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

import com.brunneis.polypus.restapi.model.vo.JobInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brunneis
 */
public class SparkHelper {

    public static void launchJob(JobInfo ji) {

        // System command to run
        List<String> commands = new ArrayList<String>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("/data/spark-launcher.sh"
                + " --job_id=" + ji.getJob_id()
                + " --search_query=\"" + ji.getSearch_query() + "\""
                + " --language=" + ji.getLanguage()
                + " --start_timestamp=" + ji.getStart_timestamp()
                + " --stop_timestamp=" + ji.getStop_timestamp()
                + " --window_size=" + ji.getWindow_size()
                + " --job_tag=" + ji.getJob_tag()
        );

        System.out.println(commands);

        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        try {
            commandExecutor.executeCommand();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SparkHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

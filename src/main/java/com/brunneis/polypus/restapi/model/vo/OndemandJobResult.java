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
package com.brunneis.polypus.restapi.model.vo;

import java.util.HashMap;

/**
 *
 * @author brunneis
 */
public class OndemandJobResult {

    private String job_id;
    private String search_query;
    private HashMap<String, Double> global;
    private HashMap<Long, HashMap<String, Double>> windows;

    public OndemandJobResult() {
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getSearch_query() {
        return search_query;
    }

    public void setSearch_query(String search_query) {
        this.search_query = search_query;
    }

    public HashMap<String, Double> getGlobal() {
        return global;
    }

    public void setGlobal(HashMap<String, Double> global) {
        this.global = global;
    }

    public HashMap<Long, HashMap<String, Double>> getWindows() {
        return windows;
    }

    public void setWindows(HashMap<Long, HashMap<String, Double>> windows) {
        this.windows = windows;
    }

}

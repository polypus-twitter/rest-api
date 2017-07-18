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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author brunneis
 */
public class AplJobResult {

    private String job_id;
    private String search_query;
    private ArrayList<Double> total;
    private HashMap<Long, ArrayList<Double>> blocks;

    public AplJobResult() {
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

    public void setSearch_query(String query) {
        this.search_query = query;
    }

    public ArrayList<Double> getTotal() {
        return total;
    }

    public void setTotal(ArrayList<Double> total) {
        this.total = total;
    }

    public HashMap<Long, ArrayList<Double>> getBlocks() {
        return blocks;
    }

    public void setBlocks(HashMap<Long, ArrayList<Double>> blocks) {
        this.blocks = blocks;
    }

}

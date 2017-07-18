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

import com.brunneis.polypus.restapi.model.helpers.IDFactory;
import java.text.ParseException;

/**
 *
 * @author brunneis
 */
public class JobInfo {

    private String job_id;
    private String search_query;
    private String language;
    private Long start_timestamp;
    private Long stop_timestamp;
    private Long window_size;
    private String job_tag;

    public JobInfo() {
        this.job_id = IDFactory.getNewId();
        this.language = "es";
        this.start_timestamp = 1466978400000L;
        this.stop_timestamp = System.currentTimeMillis();
        this.window_size = 3600000L;
        this.job_tag = "untagged";
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
        // System.out.println(search_query);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getStart_timestamp() {
        return start_timestamp;
    }

    public void setStart_timestamp(Long start_timestamp) throws ParseException {

        if (start_timestamp < 1466978400000L || start_timestamp >= System.currentTimeMillis()) {
            this.start_timestamp = 1466978400000L;
        } else {
            this.start_timestamp = start_timestamp;
        }
    }

    public Long getStop_timestamp() {
        return stop_timestamp;
    }

    public void setStop_timestamp(Long stop_timestamp) throws ParseException {

        if (stop_timestamp > System.currentTimeMillis()) {
            this.stop_timestamp = System.currentTimeMillis();
        } else {
            this.stop_timestamp = stop_timestamp;
        }
        // Si la fecha de parada es anterior a la de inicio recorre todo lo que haya
    }

    public Long getWindow_size() {
        return window_size;
    }

    public void setWindow_size(Long window_size) {
        // Se convierte a milisegundos la ventana en minutos
        this.window_size = 60000 * window_size;
    }

    public String getJob_tag() {
        return job_tag;
    }

    public void setJob_tag(String job_tag) {
        this.job_tag = job_tag;
    }

}

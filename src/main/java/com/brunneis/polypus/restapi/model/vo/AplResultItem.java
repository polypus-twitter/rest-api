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

/**
 *
 * @author brunneis
 */
public class AplResultItem {

    private String term;
    private Long timestamp;
    private Double result;
    private Double relevance;
    private Long posts;

    public AplResultItem(String term, Long timestamp, Double result, Double relevance, Long posts) {
        this.term = term;
        this.timestamp = timestamp;
        this.result = result;
        this.relevance = relevance;
        this.posts = posts;
    }

    public AplResultItem() {
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Long getPosts() {
        return posts;
    }

    public void setPosts(Long posts) {
        this.posts = posts;
    }

    public Double getRelevance() {
        return relevance;
    }

    public void setRelevance(Double relevance) {
        this.relevance = relevance;
    }

}

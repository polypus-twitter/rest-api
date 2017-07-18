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

import com.brunneis.polypus.restapi.model.vo.AplResultItem;
import java.util.ArrayList;

/**
 *
 * @author brunneis
 */
public interface AplDAO {

    public void addTermToLPA(String term);

    public void removeTermFromLPA(String term);

    public ArrayList<String> getLPATerms();

    public ArrayList<String> getLPATerms(String query);

    public ArrayList<String> getLPATerms(int number, Long backTime);

    public AplResultItem getNextLPAResultItem(String term, Long lastTimestamp);

    public ArrayList<AplResultItem> getLPAResultItems(String term, Long startTimestamp, Long stopTimestamp);

}

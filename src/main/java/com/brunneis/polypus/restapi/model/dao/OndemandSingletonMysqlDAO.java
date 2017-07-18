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

import com.brunneis.polypus.restapi.model.exceptions.BadRequestResponseException;
import com.brunneis.polypus.restapi.model.exceptions.DaoException;
import com.brunneis.polypus.restapi.model.vo.AplJobResult;
import com.brunneis.polypus.restapi.model.vo.OndemandJobResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brunneis
 */
public class OndemandSingletonMysqlDAO implements OndemandDAO {

    private static final OndemandSingletonMysqlDAO INSTANCE = new OndemandSingletonMysqlDAO();

    public static OndemandSingletonMysqlDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public OndemandJobResult getJobResult(String id) {
        OndemandJobResult jr = null;

        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("SELECT * FROM `dpsa_tagged_results` "
                    + "WHERE job_id = ?")) {
                st.setString(1, id);
                ResultSet rs = st.executeQuery();

                if (rs.next()) {

                    try {
                        jr = new ObjectMapper().readValue(rs.getString("result"), OndemandJobResult.class);
                    } catch (IOException ex) {
                        Logger.getLogger(OndemandSingletonMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                        throw new BadRequestResponseException();
                    }

                    jr.setJob_id(id);
                    jr.setSearch_query(rs.getString("search_query"));

                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }

        return jr;
    }

    @Override
    public ArrayList<AplJobResult> searchJobResults(String query) {

        ArrayList<AplJobResult> results = new ArrayList<>();

        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("SELECT * FROM `dpsa_tagged_results` "
                    + "WHERE search_query LIKE ?")) {
                st.setString(1, "%" + query + "%");
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    AplJobResult jr;
                    try {
                        jr = new ObjectMapper().readValue(rs.getString("result"), AplJobResult.class);
                    } catch (IOException ex) {
                        Logger.getLogger(OndemandSingletonMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                        throw new BadRequestResponseException();
                    }
                    jr.setJob_id(rs.getString("job_id"));
                    jr.setSearch_query(rs.getString("search_query"));

                    results.add(jr);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }

        return results;

    }

}

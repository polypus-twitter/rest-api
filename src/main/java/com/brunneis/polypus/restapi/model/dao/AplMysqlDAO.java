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

import com.brunneis.polypus.restapi.model.exceptions.DaoException;
import com.brunneis.polypus.restapi.model.vo.AplResultItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brunneis
 */
public class AplMysqlDAO implements AplDAO {

    private static final AplMysqlDAO INSTANCE = new AplMysqlDAO();

    public static AplMysqlDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public void addTermToLPA(String term) {
        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("INSERT IGNORE INTO `dpsa_lpa_terms` (`term`) "
                    + "VALUES (?)")) {
                st.setString(1, term);
                st.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }
    }

    @Override
    public void removeTermFromLPA(String term) {
        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("DELETE FROM `dpsa_lpa_terms` "
                    + "WHERE `term` = ?")) {
                st.setString(1, term);
                st.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }
    }

    @Override
    public ArrayList<String> getLPATerms() {
        ArrayList<String> terms = new ArrayList<>();
        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("SELECT term FROM `dpsa_lpa_terms`")) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    terms.add(rs.getString("term"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }
        return terms;
    }

    @Override
    public ArrayList<String> getLPATerms(String query) {
        ArrayList<String> terms = new ArrayList<>();
        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("SELECT term FROM `dpsa_lpa_terms` "
                    + "WHERE `term` LIKE ?")) {
                st.setString(1, "%" + query + "%");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    terms.add(rs.getString("term"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }
        return terms;
    }

    // backTime en minutos
    @Override
    public ArrayList<String> getLPATerms(int number, Long backTime) {
        Long backTimeInMillis = backTime * 60000; // Minutos a milisegundos

        ArrayList<String> terms = new ArrayList<>();
        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement(""
                    + "SELECT DISTINCT r.term AS term FROM "
                    + "(SELECT * FROM `dpsa_lpa_results` "
                    + "WHERE TIMESTAMPDIFF(MICROSECOND, stop_timestamp, now())/1000 <= ? "
                    + "ORDER BY `processed_posts` DESC LIMIT 100) "
                    + "AS r LIMIT ?")) {
                st.setLong(1, backTimeInMillis);
                st.setInt(2, number);

                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    terms.add(rs.getString("term"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }
        return terms;
    }

    @Override
    public AplResultItem getNextLPAResultItem(String term, Long lastTimestamp) {
        AplResultItem apri = new AplResultItem();

        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("SELECT * "
                    + "FROM `dpsa_lpa_results` "
                    + "WHERE term = ? "
                    + "AND start_timestamp >= ? "
                    + "ORDER BY stop_timestamp ASC "
                    + "LIMIT 1")) {
                st.setString(1, term);
                st.setTimestamp(2, new Timestamp(lastTimestamp));
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    apri.setTerm(term);
                    apri.setResult(rs.getDouble("result"));
                    apri.setPosts(rs.getLong("processed_posts"));
                    apri.setRelevance(rs.getDouble("total_relevance"));
                    apri.setTimestamp(rs.getTimestamp("stop_timestamp").getTime());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }

        return apri;
    }

    @Override
    public ArrayList<AplResultItem> getLPAResultItems(String term, Long startTimestamp, Long stopTimestamp) {

        ArrayList<AplResultItem> apris = new ArrayList<>();

        try (Connection con = MySQLPool.getConnection(4)) {
            try (PreparedStatement st = con.prepareStatement("SELECT * "
                    + "FROM `dpsa_lpa_results` "
                    + "WHERE term = ? "
                    + "AND start_timestamp >= ? "
                    + "AND stop_timestamp <= ? "
                    + "ORDER BY stop_timestamp ASC")) {
                st.setString(1, term);
                st.setTimestamp(2, new Timestamp(startTimestamp));
                st.setTimestamp(3, new Timestamp(stopTimestamp));

                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    apris.add(new AplResultItem(
                            term,
                            rs.getTimestamp("stop_timestamp").getTime(),
                            rs.getDouble("result"),
                            rs.getDouble("total_relevance"),
                            rs.getLong("processed_posts")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AplMysqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DaoException();
        }

        return apris;
    }

}

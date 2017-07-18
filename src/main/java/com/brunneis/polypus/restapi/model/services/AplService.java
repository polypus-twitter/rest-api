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
package com.brunneis.polypus.restapi.model.services;

import com.brunneis.polypus.restapi.model.dao.SingletonFactoryDAO;
import com.brunneis.polypus.restapi.model.exceptions.NotFoundResponseException;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import com.brunneis.polypus.restapi.model.exceptions.BadRequestResponseException;
import com.brunneis.polypus.restapi.model.vo.AplResultItem;
import com.brunneis.polypus.restapi.model.vo.AplJobResult;
import java.util.HashMap;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import com.brunneis.polypus.restapi.model.dao.AplDAO;

/**
 *
 * @author brunneis
 */
@Path("/lpa")
public class AplService {

    // Add a term to the APL
    @PUT
    @Path("/{term}")
    @Produces("application/json;charset=utf-8")
    public Response addLPATerm(@PathParam("term") String term) {

        if (term == null) {
            throw new BadRequestResponseException();
        }

        if (term.equals("")) {
            throw new BadRequestResponseException();
        }

        // Filtrar entrada
        AplDAO lpad = SingletonFactoryDAO.getLPADAOInstance();
        lpad.addTermToLPA(term);

        return Response.ok().build();
    }

    // Delete a keyword from the APL
    @DELETE
    @Path("/{term}")
    @Produces("application/json;charset=utf-8")
    public Response removeLPATerm(@PathParam("term") String term) {

        if (term == null) {
            throw new BadRequestResponseException();
        }

        if (term.equals("")) {
            throw new BadRequestResponseException();
        }

        // Filtrar entrada
        AplDAO lpad = SingletonFactoryDAO.getLPADAOInstance();
        lpad.removeTermFromLPA(term);

        return Response.ok().build();
    }

    // Get the next record of keyword from the APL given a timestamp
    @GET
    @Path("/{term}/next")
    @Produces("application/json;charset=utf-8")
    public Response getLPAnextTermResult(@PathParam("term") String term,
            @QueryParam("since") Long start_timestamp) {

        if (term == null) {
            throw new BadRequestResponseException();
        }

        if (term.equals("")) {
            throw new BadRequestResponseException();
        }

        if (start_timestamp == null) {
            throw new BadRequestResponseException();
        }

        if (start_timestamp < 0) {
            throw new BadRequestResponseException();
        }

        AplDAO lpad = SingletonFactoryDAO.getLPADAOInstance();
        AplResultItem terms = lpad.getNextLPAResultItem(term, start_timestamp);

        if (terms == null) {
            throw new NotFoundResponseException();
        }

        return Response.ok(terms).build();
    }

    // Get results of a keyword from the APL in a given interval
    @GET
    @Path("/{term}/results")
    @Produces("application/json;charset=utf-8")
    public Response getLPATermResult(@PathParam("term") String term,
            @QueryParam("b") Long start_timestamp,
            @QueryParam("e") Long stop_timestamp) {

        if (term == null) {
            throw new BadRequestResponseException();
        }

        if (term.equals("")) {
            throw new BadRequestResponseException();
        }

        if (start_timestamp == null) {
            throw new BadRequestResponseException();
        }

        if (stop_timestamp == null) {
            throw new BadRequestResponseException();
        }

        if (start_timestamp < 0) {
            throw new BadRequestResponseException();
        }

        if (stop_timestamp < start_timestamp) {
            throw new BadRequestResponseException();
        }

        AplDAO lpad = SingletonFactoryDAO.getLPADAOInstance();
        ArrayList<AplResultItem> results = lpad.getLPAResultItems(term, start_timestamp, stop_timestamp);

        HashMap<Long, ArrayList<Double>> blocks = new HashMap<>();

        Double sumaProductos = 0.0;
        Long postTotales = 0L;
        for (AplResultItem result : results) {
            ArrayList<Double> item = new ArrayList<>();
            item.add(result.getResult());
            item.add(Double.parseDouble(result.getPosts() + ""));
            blocks.put(result.getTimestamp(), item);

            postTotales += result.getPosts();
            sumaProductos += result.getResult() * result.getPosts();
        }

        ArrayList<Double> total = new ArrayList<>();
        total.add(sumaProductos / postTotales);
        total.add(Double.parseDouble(postTotales + ""));

        AplJobResult jr = new AplJobResult();
        jr.setBlocks(blocks);
        jr.setSearch_query(term);
        jr.setTotal(total);
        jr.setJob_id("LPA_ANY");

        if (results == null) {
            throw new NotFoundResponseException();
        }

        return Response.ok(jr).build();
    }

    // Locate n terms with more activity in the last no minutes
    @GET
    @Path("/trending")
    @Produces("application/json;charset=utf-8")
    public Response getLPARelevantTerms(@QueryParam("no") int number,
            @QueryParam("min") Long minutes) {

        if (number < 1 || minutes < 1) {
            throw new BadRequestResponseException();
        }

        AplDAO lpad = SingletonFactoryDAO.getLPADAOInstance();
        ArrayList<String> terms = lpad.getLPATerms(number, minutes);

        if (terms == null) {
            throw new NotFoundResponseException();
        }

        return Response.ok(terms).build();
    }

    // Locate APL keywords by name
    @GET
    @Path("/search")
    @Produces("application/json;charset=utf-8")
    public Response getLPATermsByName(@QueryParam("q") String query) {

        if (query == null) {
            throw new BadRequestResponseException();
        }

        if (query.equals("")) {
            throw new BadRequestResponseException();
        }

        AplDAO lpad = SingletonFactoryDAO.getLPADAOInstance();
        ArrayList<String> terms = lpad.getLPATerms(query);

        if (terms
                == null) {
            throw new NotFoundResponseException();
        }

        return Response.ok(terms).build();
    }

    // Get all the keywords from the APL
    @GET
    @Produces("application/json;charset=utf-8")
    public Response getLPATerms() {

        AplDAO lpad = SingletonFactoryDAO.getLPADAOInstance();
        ArrayList<String> terms = lpad.getLPATerms();

        if (terms == null) {
            throw new NotFoundResponseException();
        }

        return Response.ok(terms).build();
    }

}

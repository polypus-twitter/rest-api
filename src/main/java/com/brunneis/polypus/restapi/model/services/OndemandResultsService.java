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
import com.brunneis.polypus.restapi.model.exceptions.BadRequestResponseException;
import com.brunneis.polypus.restapi.model.exceptions.NotFoundResponseException;
import com.brunneis.polypus.restapi.model.vo.AplJobResult;
import com.brunneis.polypus.restapi.model.vo.OndemandJobResult;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import com.brunneis.polypus.restapi.model.dao.OndemandDAO;

/**
 *
 * @author brunneis
 */
@Path("/results")
public class OndemandResultsService {

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public Response getJobResultById(@PathParam("id") String id) {

        if (id == null) {
            throw new BadRequestResponseException();
        }

        if (id.equals("")) {
            throw new BadRequestResponseException();
        }

        OndemandDAO odd = SingletonFactoryDAO.getOnDemandDAOInstance();
        OndemandJobResult jr = odd.getJobResult(id);

        if (jr == null) {
            throw new NotFoundResponseException();
        }

        return Response.ok(jr).build();
    }

    @GET
    @Produces("application/json;charset=utf-8")
    public Response getJobsResults(@QueryParam("q") String query) {

        if (query == null) {
            throw new BadRequestResponseException();
        }

        if (query.equals("")) {
            throw new BadRequestResponseException();
        }

        OndemandDAO odd = SingletonFactoryDAO.getOnDemandDAOInstance();
        ArrayList<AplJobResult> results = odd.searchJobResults(query);

        if (results == null) {
            throw new NotFoundResponseException();
        }

        return Response.ok(results).build();
    }

}

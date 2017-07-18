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

import com.brunneis.polypus.restapi.model.helpers.SparkHelper;
import com.brunneis.polypus.restapi.model.vo.JobInfo;
import com.brunneis.polypus.restapi.model.vo.ResponseContainerWithMeta;
import com.brunneis.polypus.restapi.conf.Conf;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author brunneis
 */
@Path("/submit")
public class SubmissionsService {

    @POST
    @Consumes("application/json;charset=utf-8")
    @Produces("application/json;charset=utf-8")
    public Response getConfiguration(JobInfo job) {
        SparkHelper.launchJob(job);
        return Response.ok(new ResponseContainerWithMeta(Conf.BASE_URL.value()
                + "/results/" + job.getJob_id(), job.getJob_id())).build();
    }

}

package com.example.demo.controllers;

import com.example.demo.services.MetricService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Controller
@Path("/msi")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MetricController {
    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @POST
    @Path("/metric")
    public Response getMetric() throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(metricService.getFullMetric());
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/metric/test")
    public Response test() {
        return Response.ok("json", MediaType.APPLICATION_JSON).build();
    }
}

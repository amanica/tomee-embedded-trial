package com.example;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@LocalBean
@Path("/duck")
@Produces(MediaType.APPLICATION_JSON)
public class DuckRS {

    @GET
    public Duck giveMeADuck() throws Exception {
        return new Duck();
    }
}

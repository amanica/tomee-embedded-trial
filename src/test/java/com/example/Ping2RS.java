package com.example;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@LocalBean
@Path("/ping2")
public class Ping2RS extends PingRS
{


    @EJB
    private Movies movies;

    @GET
    public String ping() throws Exception {
        return "pong2"
            + " " + movies.getMovies().get(0).getTitle();
    }
}

package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ping")
public class PingRS {


    // @EJB
    // private Movies movies;

    @GET
    public String ping() throws Exception {
        return "pong";
        // + " " + movies.getMovies().get(0).getTitle();
    }
}

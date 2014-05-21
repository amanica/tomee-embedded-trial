package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ping")
public class PingRS {

    @GET
    public String ping() {
        return "pong";
    }
}

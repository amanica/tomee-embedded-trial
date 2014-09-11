package com.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

@ApplicationPath("/MyRestApplication")
@Produces({MediaType.APPLICATION_JSON})
public class MyRestApplication extends Application {

    // public MyRestApplication() {
    // this.
    // packages("edu.kit.tm.cm.sc");
    //
    // register(MyJsonProvider.class);
    // register(JacksonJsonProvider.class);
    // }
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(
            PingRS.class, DuckRS.class));
    }
}

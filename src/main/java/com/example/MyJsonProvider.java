package com.example;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

// from:
// http://poor-developer.blogspot.com/2014/01/jackson-api-object-to-json-and-vice.html
// http://jackson-users.ning.com/forum/topics/configuring-custom-modules-when-using-jaxrs-provider
@Provider
@Consumes(MediaType.WILDCARD)
// NOTE: required to support "non-standard" JSON variants
@Produces(MediaType.WILDCARD)
public class MyJsonProvider extends JacksonJaxbJsonProvider {
    public MyJsonProvider() {
        super();
        // this creates a 'configured' mapper
        _mapperConfig.configure(SerializationFeature.INDENT_OUTPUT, true);

        ObjectMapper mapper = _mapperConfig.getConfiguredMapper(); // now we can
                                                                   // access it
                                                                   // and add
                                                                   // our own
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        // mapper.registerModule(new SomeModule());
        mapper.setSerializationInclusion(Include.NON_NULL);

        // mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            false);
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        // false);
        // mapper.setPropertyNamingStrategy(new MyPropertyNamingStrategy());
        // mapper.addMixInAnnotations(Snapshot.class, SnapshotMixin.class);
        // mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
    }
}

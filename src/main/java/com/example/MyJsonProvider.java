package com.example;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

// from:
// http://poor-developer.blogspot.com/2014/01/jackson-api-object-to-json-and-vice.html
// http://jackson-users.ning.com/forum/topics/configuring-custom-modules-when-using-jaxrs-provider
@Provider
@Consumes(MediaType.WILDCARD) // NOTE: required to support "non-standard" JSON variants
@Produces(MediaType.WILDCARD)
public class MyJsonProvider extends JacksonJaxbJsonProvider {
  public MyJsonProvider () {
    super();
        _mapperConfig.configure(Feature.INDENT_OUTPUT, true); // this creates a
                                                              // 'configured'
                                                              // mapper
    ObjectMapper mapper = _mapperConfig.getConfiguredMapper();    // now we can access it and add our own

        mapper.configure(Feature.WRITE_NULL_MAP_VALUES, false);

    //mapper.registerModule(new SomeModule());
        mapper.setSerializationInclusion(Inclusion.NON_NULL);

    //mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        // false);
    //mapper.setPropertyNamingStrategy(new MyPropertyNamingStrategy());
        // mapper.addMixInAnnotations(Snapshot.class, SnapshotMixin.class);
    //mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
  }
}

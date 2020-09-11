package com.example.demo.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.ext.Provider;

@Provider
public class MsiJacksonJsonProvider extends JacksonJsonProvider {

    public MsiJacksonJsonProvider() {
        final ObjectMapper mapper = _mapperConfig.getDefaultMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        super.setMapper(mapper);
    }

}
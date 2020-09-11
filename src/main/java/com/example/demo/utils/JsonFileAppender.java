package com.example.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class JsonFileAppender {
    private final ObjectMapper jsonMapper;

    public JsonFileAppender() {
        this.jsonMapper = JsonMapper.builder().build();
    }

    public void appendToArray(File jsonFile, Object value) throws IOException {
        Objects.requireNonNull(jsonFile);
        Objects.requireNonNull(value);
        if (jsonFile.isDirectory()) {
            throw new IllegalArgumentException("File can not be a directory!");
        }

        JsonNode node = readArrayOrCreateNew(jsonFile);
        if (node.isArray()) {
            ArrayNode array = (ArrayNode) node;
            array.addPOJO(value);
        } else {
            ArrayNode rootArray = jsonMapper.createArrayNode();
            rootArray.add(node);
            rootArray.addPOJO(value);
            node = rootArray;
        }
        jsonMapper.writeValue(jsonFile, node);
    }

    private JsonNode readArrayOrCreateNew(File jsonFile) throws IOException {
        if (jsonFile.exists() && jsonFile.length() > 0) {
            return jsonMapper.readTree(jsonFile);
        }

        return jsonMapper.createArrayNode();
    }
}

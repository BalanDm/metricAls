package com.example.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class JsonFileAppender {
    private final ObjectMapper jsonMapper;

    public JsonFileAppender() {
        this.jsonMapper = JsonMapper.builder().build();
    }

    public void appendToArrayOrReplace(File jsonFile, Map<?, ?> metricInfo) throws IOException {
        if (jsonFile.isDirectory()) {
            throw new IllegalArgumentException("File can not be a directory!");
        }

        final JsonNode node = readArrayOrCreateNew(jsonFile);
        if (node.isArray()) {
            ArrayNode array = (ArrayNode) node;
            for (Map.Entry entry : metricInfo.entrySet()) {
                if (array.findParent(entry.getKey().toString()) == null) {
                    array.addPOJO(entry);
                    jsonMapper.writeValue(jsonFile, node);
                } else {
                    replaceFile(jsonFile, metricInfo);
                }
            }
        }
    }

    public void replaceFile(File file, Map<?, ?> metricInfo) {
        JsonNode fileString = null;
        try {
            fileString = readArrayOrCreateNew(file);
            for (Map.Entry entry : metricInfo.entrySet()) {
                try {
                    change(fileString, entry.getKey().toString(), entry.getValue().toString());
                    jsonMapper.writeValue(file, fileString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void change(JsonNode parent, String fieldName, String newValue) {
        if (parent.has(fieldName)) {
            ((ObjectNode) parent).put(fieldName, newValue);
        }
        // Now, recursively invoke this method on all properties
        for (JsonNode child : parent) {
            change(child, fieldName, newValue);
        }
    }

    private JsonNode readArrayOrCreateNew(File jsonFile) throws IOException {
        if (jsonFile.exists() && jsonFile.length() > 0) {
            return jsonMapper.readTree(jsonFile);
        }
        return jsonMapper.createArrayNode();
    }
}

package com.example.demo.services;

import com.example.demo.utils.JsonFileAppender;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MetricExtractorService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    private JsonFileAppender fileAppender;
    private MetricService metricService;


    public MetricExtractorService(JsonFileAppender fileAppender, @Lazy MetricService metricService) {
        this.fileAppender = fileAppender;
        this.metricService = metricService;
    }

    public void extract() {

        //create directory of file log for file (.json) if not exist
        File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "/metric-log");
        System.out.println(jarDir.getAbsolutePath());
        if (!jarDir.exists()) {
            try {
                jarDir.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
        Path file = null;
        try {
            SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = pattern.format(new Date());
            if (!new File(jarDir.getPath() + "\\" + currentDate + ".json").exists()) {
                file = Paths.get(jarDir.getPath());
                file = Files.createFile(file.resolve(currentDate +".json"));
            }else{
                file = Paths.get(jarDir.getPath() + "\\" + currentDate + ".json");
            }
            File inFile = file.toFile();
            fileAppender.appendToArrayOrReplace(inFile, metricService.getFullMetric());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}

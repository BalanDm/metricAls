package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MetricService {
    private MetricExtractorService metricExtractorService;

    @Autowired
    public void setMetricExtractorService(MetricExtractorService metricExtractorService) {
        this.metricExtractorService = metricExtractorService;
    }

    private ConcurrentMap<String, ConcurrentHashMap<Integer, Integer>> metricMap;
    public MetricService(MetricExtractorService metricExtractorService) {
        this.metricExtractorService = metricExtractorService;
        metricMap = new ConcurrentHashMap<>();
    }

    public synchronized void increaseCount(String request, int status) {
        ConcurrentHashMap<Integer, Integer> statusMap = metricMap.get(request);
        if (statusMap == null) {
            statusMap = new ConcurrentHashMap<Integer, Integer>();
        }

        Integer count = statusMap.get(status);
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        statusMap.put(status, count);
        metricMap.put(request, statusMap);
        metricExtractorService.extract();
    }

    public Map getFullMetric() {
        return metricMap;
    }
}

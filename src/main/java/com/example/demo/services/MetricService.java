package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricService {
    private MetricExtractorService metricExtractorService;

    @Autowired
    public void setMetricExtractorService(MetricExtractorService metricExtractorService) {
        this.metricExtractorService = metricExtractorService;
    }

    private ConcurrentHashMap<Integer, AtomicInteger> metricMap;

    public MetricService(MetricExtractorService metricExtractorService) {
        this.metricExtractorService = metricExtractorService;
        metricMap = new ConcurrentHashMap<>();
    }

    public synchronized void increaseCount(int status) {
        metricMap.computeIfAbsent(status, k -> new AtomicInteger(0)).addAndGet(1);
        metricExtractorService.extract();
    }

    public Map getFullMetric() {
        return metricMap;
    }
}

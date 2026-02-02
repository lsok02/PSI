package org.example.flightservice.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FlightLockService {

    private final Map<String, List<Long>> lockedFlightsCache = new ConcurrentHashMap<>();

    public boolean isFlightLocked(Long flightId) {
        return lockedFlightsCache.values().stream()
                .anyMatch(flightIds -> flightIds.contains(flightId));
    }

    public void lockFlights(String terminal, LocalDate date, List<Long> flightIds) {
        String cacheKey = generateCacheKey(terminal, date);
        lockedFlightsCache.put(cacheKey, new ArrayList<>(flightIds));
    }

    public List<Long> unlockFlights(String terminal, LocalDate date) {
        String cacheKey = generateCacheKey(terminal, date);
        return lockedFlightsCache.remove(cacheKey);
    }

    public boolean hasLockedFlights(String terminal, LocalDate date) {
        String cacheKey = generateCacheKey(terminal, date);
        return lockedFlightsCache.containsKey(cacheKey);
    }

    private String generateCacheKey(String terminal, LocalDate date) {
        return terminal + "_" + date.toString();
    }
}
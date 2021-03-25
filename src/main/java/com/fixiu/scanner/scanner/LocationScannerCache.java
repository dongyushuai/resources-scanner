package com.fixiu.scanner.scanner;

import java.util.HashMap;
import java.util.Map;

import com.fixiu.scanner.scanner.classpath.ClassPathLocationScanner;

public class LocationScannerCache {

    /**
     * Cache the location scanner for each protocol.
     */
    private final Map<String, ClassPathLocationScanner> cache = new HashMap<>();

    public boolean containsKey(String protocol) {
        return cache.containsKey(protocol);
    }

    public ClassPathLocationScanner get(String protocol) {
        return cache.get(protocol);
    }

    public void put(String protocol, ClassPathLocationScanner scanner) {
        cache.put(protocol, scanner);
    }
}
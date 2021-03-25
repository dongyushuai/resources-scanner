package com.fixiu.scanner.scanner;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fixiu.scanner.scanner.classpath.ClassPathLocationScanner;

public class ResourceNameCache {
    /**
     * Cache resource names.
     */
    private final Map<ClassPathLocationScanner, Map<URL, Set<String>>> resourceNameCache = new HashMap<>();

    public void put(ClassPathLocationScanner classPathLocationScanner, Map<URL, Set<String>> map){
        resourceNameCache.put(classPathLocationScanner, map);
    }

    public void put(ClassPathLocationScanner classPathLocationScanner, URL resolvedUrl, Set<String> names){
        resourceNameCache.get(classPathLocationScanner).put(resolvedUrl, names);
    }

    public Set<String> get(ClassPathLocationScanner classPathLocationScanner, URL resolvedUrl){
        return resourceNameCache.get(classPathLocationScanner).get(resolvedUrl);
    }
}
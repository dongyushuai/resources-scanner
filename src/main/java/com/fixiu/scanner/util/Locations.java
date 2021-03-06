package com.fixiu.scanner.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogFactory;
import com.fixiu.scanner.scanner.Location;

/**
 * Encapsulation of a location list.
 */
public class Locations {
    private static final Log LOG = LogFactory.getLog(Locations.class);

    /**
     * The backing list.
     */
    private final List<Location> locations = new ArrayList<>();

    /**
     * Creates a new Locations wrapper with these raw locations.
     *
     * @param rawLocations The raw locations to process.
     */
    public Locations(String... rawLocations) {
        List<Location> normalizedLocations = new ArrayList<>();
        for (String rawLocation : rawLocations) {
            normalizedLocations.add(new Location(rawLocation));
        }
        processLocations(normalizedLocations);
    }

    /**
     * Creates a new Locations wrapper with these locations.
     *
     * @param rawLocations The locations to process.
     */
    public Locations(List<Location> rawLocations) {
        processLocations(rawLocations);
    }

    private void processLocations(List<Location> rawLocations) {
        List<Location> sortedLocations = new ArrayList<>(rawLocations);
        Collections.sort(sortedLocations);

        for (Location normalizedLocation : sortedLocations) {
            if (locations.contains(normalizedLocation)) {
                LOG.warn("Discarding duplicate location '" + normalizedLocation + "'");
                continue;
            }

            Location parentLocation = getParentLocationIfExists(normalizedLocation, locations);
            if (parentLocation != null) {
                LOG.warn("Discarding location '" + normalizedLocation + "' as it is a sublocation of '" + parentLocation + "'");
                continue;
            }

            locations.add(normalizedLocation);
        }
    }

    /**
     * @return The locations.
     */
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * Retrieves this location's parent within this list, if any.
     *
     * @param location       The location to check.
     * @param finalLocations The list to search.
     * @return The parent location. {@code null} if none.
     */
    private Location getParentLocationIfExists(Location location, List<Location> finalLocations) {
        for (Location finalLocation : finalLocations) {
            if (finalLocation.isParentOf(location)) {
                return finalLocation;
            }
        }
        return null;
    }
}
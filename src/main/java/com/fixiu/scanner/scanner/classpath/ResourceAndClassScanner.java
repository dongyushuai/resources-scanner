package com.fixiu.scanner.scanner.classpath;

import java.util.Collection;

import com.fixiu.scanner.resource.LoadableResource;

/**
 * Scanner for both resources and classes.
 */
public interface ResourceAndClassScanner<I> {
    /**
     * Scans the classpath for resources under the configured location.
     *
     * @return The resources that were found.
     */
    Collection<LoadableResource> scanForResources();

    /**
     * Scans the classpath for concrete classes under the specified package implementing the specified interface.
     * Non-instantiable abstract classes are filtered out.
     *
     * @return The non-abstract classes that were found.
     */
    Collection<Class<? extends I>> scanForClasses();
}
package com.fixiu.scanner.scanner;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fixiu.scanner.clazz.ClassProvider;
import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogFactory;
import com.fixiu.scanner.resource.LoadableResource;
import com.fixiu.scanner.resource.ResourceProvider;
import com.fixiu.scanner.scanner.android.AndroidScanner;
import com.fixiu.scanner.scanner.classpath.ClassPathScanner;
import com.fixiu.scanner.scanner.classpath.ResourceAndClassScanner;
import com.fixiu.scanner.scanner.filesystem.FileSystemScanner;
import com.fixiu.scanner.util.FeatureDetector;
import com.fixiu.scanner.util.StringUtils;

/**
 * Scanner for Resources and Classes.
 */
public class Scanner<I> implements ResourceProvider, ClassProvider<I> {
	private static final Log LOG = LogFactory.getLog(Scanner.class);

	private final List<LoadableResource> resources = new ArrayList<>();
	private final List<Class<? extends I>> classes = new ArrayList<>();
	private ResourceNameCache resourceNameCache;
	private LocationScannerCache locationScannerCache;
	
	public Scanner() { /*Nothing to do*/ }

	/*
	 * Constructor. Scans the given locations for resources, and classes
	 * implementing the specified interface.
	 */
	public Scanner(Class<I> implementedInterface, Collection<Location> locations) {
		Charset charset = StandardCharsets.UTF_8;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		resourceNameCache = new ResourceNameCache();
		locationScannerCache = new LocationScannerCache();
		
		initScanner(implementedInterface, locations, classLoader, charset, resourceNameCache, locationScannerCache);
	}

	/*
	 * Constructor. Scans the given locations for resources, and classes
	 * implementing the specified interface.
	 */
	public Scanner(Class<I> implementedInterface, Collection<Location> locations, ClassLoader classLoader,
			Charset encoding, ResourceNameCache resourceNameCache, LocationScannerCache locationScannerCache) {
		
		initScanner(implementedInterface, locations, classLoader, encoding, resourceNameCache, locationScannerCache);
	}

	/**
	 * Initialization the Scanner
	 * 
	 * @param implementedInterface The interface the class is expected to implement.
	 * @param locations The locations to scan
	 * @param classLoader The ClassLoader to use.
	 * @param encoding The encoding to use.
	 * @param resourceNameCache The resource name cache to use.
	 * @param locationScannerCache The scanner cache to use.
	 */
	public void initScanner(Class<I> implementedInterface, Collection<Location> locations, ClassLoader classLoader,
			Charset encoding, ResourceNameCache resourceNameCache, LocationScannerCache locationScannerCache) {
		FileSystemScanner fileSystemScanner = new FileSystemScanner(encoding);

		boolean android = new FeatureDetector(classLoader).isAndroidAvailable();

		for (Location location : locations) {
			if (location.isFileSystem()) {
				resources.addAll(fileSystemScanner.scanForResources(location));
			} else {
				ResourceAndClassScanner<I> resourceAndClassScanner = android
						? new AndroidScanner<>(implementedInterface, classLoader, encoding, location)
						: new ClassPathScanner<>(implementedInterface, classLoader, encoding, location,
								resourceNameCache, locationScannerCache);
				resources.addAll(resourceAndClassScanner.scanForResources());
				classes.addAll(resourceAndClassScanner.scanForClasses());
			}
		}
	}

	@Override
	public LoadableResource getResource(String name) {
		for (LoadableResource resource : resources) {
			String fileName = resource.getRelativePath();
			if (fileName.equals(name)) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Returns all known resources starting with the specified prefix and ending
	 * with any of the specified suffixes.
	 *
	 * @param prefix   The prefix of the resource names to match.
	 * @param suffixes The suffixes of the resource names to match.
	 * @return The resources that were found.
	 */
	public Collection<LoadableResource> getResources(String prefix, String... suffixes) {
		List<LoadableResource> result = new ArrayList<>();
		for (LoadableResource resource : resources) {
			String fileName = resource.getFilename();
			if (StringUtils.startsAndEndsWith(fileName, prefix, suffixes)) {
				result.add(resource);
			} else {
				LOG.debug("Filtering out resource: " + resource.getAbsolutePath() + " (filename: " + fileName + ")");
			}
		}
		return result;
	}
	
	/**
	 * Returns all known resources starting with the specified prefix
	 *
	 * @param prefix   The prefix of the resource names to match.
	 * @return The resources that were found.
	 */
	public Collection<LoadableResource> getResourcesByPrefix(String prefix) {
		List<LoadableResource> result = new ArrayList<>();
		for (LoadableResource resource : resources) {
			String fileName = resource.getFilename();
			if (StringUtils.startsWith(fileName, prefix)) {
				result.add(resource);
			} else {
				LOG.debug("Filtering out resource: " + resource.getAbsolutePath() + " (filename: " + fileName + ")");
			}
		}
		return result;
	}
	
	/**
	 * Returns all known resources ending with any of the specified suffixes.
	 *
	 * @param suffixes The suffixes of the resource names to match.
	 * @return The resources that were found.
	 */
	public Collection<LoadableResource> getResourcesBySuffixes(String... suffixes) {
		List<LoadableResource> result = new ArrayList<>();
		for (LoadableResource resource : resources) {
			String fileName = resource.getFilename();
			if (StringUtils.endsWith(fileName, suffixes)) {
				result.add(resource);
			} else {
				LOG.debug("Filtering out resource: " + resource.getAbsolutePath() + " (filename: " + fileName + ")");
			}
		}
		return result;
	}
	
	/**
	 * Returns all known resources
	 * 
	 * @return The resources that were found.
	 */
	public Collection<LoadableResource> getResources() {
		return resources;
	}

	/**
	 * Scans the classpath for concrete classes under the specified package
	 * implementing the specified interface. Non-instantiable abstract classes are
	 * filtered out.
	 *
	 * @return The non-abstract classes that were found.
	 */
	public Collection<Class<? extends I>> getClasses() {
		return Collections.unmodifiableCollection(classes);
	}
}
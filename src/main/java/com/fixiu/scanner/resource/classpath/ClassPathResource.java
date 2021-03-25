package com.fixiu.scanner.resource.classpath;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import com.fixiu.scanner.resource.LoadableResource;
import com.fixiu.scanner.scanner.Location;
import com.fixiu.scanner.scanner.ScannerException;
import com.fixiu.scanner.util.UrlUtils;

/**
 * A resource on the classpath.
 */
public class ClassPathResource extends LoadableResource {
    /**
     * The fileNameWithAbsolutePath of the resource on the classpath.
     */
    private final String fileNameWithAbsolutePath;
    private final String fileNameWithRelativePath;

    /**
     * The ClassLoader to use.
     */
    private final ClassLoader classLoader;
    private final Charset encoding;

    /**
     * Creates a new ClassPathResource.
     *
     * @param fileNameWithAbsolutePath The path and filename of the resource on the classpath.
     * @param classLoader              The ClassLoader to use.
     */
    public ClassPathResource(Location location, String fileNameWithAbsolutePath, ClassLoader classLoader,
                             Charset encoding) {
        this.fileNameWithAbsolutePath = fileNameWithAbsolutePath;
        this.fileNameWithRelativePath = location == null ? fileNameWithAbsolutePath : location.getPathRelativeToThis(fileNameWithAbsolutePath);
        this.classLoader = classLoader;
        this.encoding = encoding;
    }

    @Override
    public String getRelativePath() {
        return fileNameWithRelativePath;
    }

    @Override
    public String getAbsolutePath() {
        return fileNameWithAbsolutePath;
    }

    @Override
    public String getAbsolutePathOnDisk() {
        URL url = getUrl();
        if (url == null) {
            throw new ScannerException("Unable to find resource on disk: " + fileNameWithAbsolutePath);
        }
        return new File(UrlUtils.decodeURL(url.getPath())).getAbsolutePath();
    }

    /**
     * @return The url of this resource.
     */
    private URL getUrl() {
        return classLoader.getResource(fileNameWithAbsolutePath);
    }

    @Override
    public Reader read() {
        InputStream inputStream = classLoader.getResourceAsStream(fileNameWithAbsolutePath);
        if (inputStream == null) {
            throw new ScannerException("Unable to obtain inputstream for resource: " + fileNameWithAbsolutePath);
        }
        return new InputStreamReader(inputStream, encoding.newDecoder());
    }

    @Override
    public String getFilename() {
        return fileNameWithAbsolutePath.substring(fileNameWithAbsolutePath.lastIndexOf("/") + 1);
    }

    public boolean exists() {
        return getUrl() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassPathResource that = (ClassPathResource) o;

        return fileNameWithAbsolutePath.equals(that.fileNameWithAbsolutePath);
    }

    @Override
    public int hashCode() {
        return fileNameWithAbsolutePath.hashCode();
    }
}
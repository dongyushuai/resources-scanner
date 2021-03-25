package com.fixiu.scanner.resource.filesystem;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;

import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogFactory;
import com.fixiu.scanner.resource.LoadableResource;
import com.fixiu.scanner.scanner.Location;
import com.fixiu.scanner.scanner.ScannerException;
import com.fixiu.scanner.util.BomStrippingReader;

/**
 * A resource on the filesystem.
 */
public class FileSystemResource extends LoadableResource {

	private static final Log LOG = LogFactory.getLog(FileSystemResource.class);

	/**
	 * The location of the resource on the filesystem.
	 */
	private final File file;
	private final String relativePath;
	private final Charset encoding;

	/**
	 * Creates a new ClassPathResource.
	 *
	 * @param fileNameWithPath The path and filename of the resource on the
	 *                         filesystem.
	 */
	public FileSystemResource(Location location, String fileNameWithPath, Charset encoding) {
		this.file = new File(new File(fileNameWithPath).getPath());
		this.relativePath = location == null ? file.getPath() : location.getPathRelativeToThis(file.getPath()).replace("\\", "/");
		this.encoding = encoding;
	}

	/**
	 * @return The location of the resource on the filesystem.
	 */
	@Override
	public String getAbsolutePath() {
		return file.getPath();
	}

	/**
	 * Retrieves the location of this resource on disk.
	 *
	 * @return The location of this resource on disk.
	 */
	@Override
	public String getAbsolutePathOnDisk() {
		return file.getAbsolutePath();
	}

	@Override
	public Reader read() {
		try {
			return Channels.newReader(FileChannel.open(file.toPath(), StandardOpenOption.READ), encoding.newDecoder(), 4096);
		} catch (IOException e) {
			LOG.debug("Unable to load filesystem resource" + file.getPath() + " using FileChannel.open."
					+ " Falling back to FileInputStream implementation. Exception message: " + e.getMessage());
		}

		try {
			return new BufferedReader(new BomStrippingReader(new InputStreamReader(new FileInputStream(file), encoding)));
		} catch (IOException e) {
			throw new ScannerException("Unable to load filesystem resource: " + file.getPath() + " (encoding: " + encoding + ")", e);
		}
	}

	/**
	 * @return The filename of this resource, without the path.
	 */
	@Override
	public String getFilename() {
		return file.getName();
	}

	@Override
	public String getRelativePath() {
		return relativePath;
	}
}
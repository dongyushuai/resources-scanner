package com.fixiu.scanner.resource.android;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.fixiu.scanner.resource.LoadableResource;
import com.fixiu.scanner.scanner.Location;
import com.fixiu.scanner.scanner.ScannerException;

/**
 * Resource within an Android App.
 */
public class AndroidResource extends LoadableResource {
    private final AssetManager assetManager;
    private final String fileName;
    private final String fileNameWithAbsolutePath;
    private final String fileNameWithRelativePath;
    private final Charset encoding;

    public AndroidResource(Location location, AssetManager assetManager, String path, String name, Charset encoding) {
        this.assetManager = assetManager;
        this.fileNameWithAbsolutePath = path + "/" + name;
        this.fileName = name;
        this.fileNameWithRelativePath = location == null ? fileNameWithAbsolutePath : location.getPathRelativeToThis(fileNameWithAbsolutePath);
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
        return null;
    }

    @Override
    public Reader read() {
        try {
            return new InputStreamReader(assetManager.open(fileNameWithAbsolutePath), encoding.newDecoder());
        } catch (IOException e) {
            throw new ScannerException("Unable to read asset: " + getAbsolutePath(), e);
        }
    }

    @Override
    public String getFilename() {
        return fileName;
    }
}
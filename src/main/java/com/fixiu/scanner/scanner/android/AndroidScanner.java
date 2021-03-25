package com.fixiu.scanner.scanner.android;

import android.content.Context;
import dalvik.system.DexFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import com.fixiu.scanner.android.ContextHolder;
import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogFactory;
import com.fixiu.scanner.resource.LoadableResource;
import com.fixiu.scanner.resource.android.AndroidResource;
import com.fixiu.scanner.scanner.Location;
import com.fixiu.scanner.scanner.ScannerException;
import com.fixiu.scanner.scanner.classpath.ResourceAndClassScanner;
import com.fixiu.scanner.util.ClassUtils;

/**
 * Class & resource scanner for Android.
 */
public class AndroidScanner<I> implements ResourceAndClassScanner<I> {
    private static final Log LOG = LogFactory.getLog(AndroidScanner.class);

    private final Context context;

    private final Class<I> implementedInterface;
    private final ClassLoader clazzLoader;
    private final Charset encoding;
    private final Location location;

    public AndroidScanner(Class<I> implementedInterface, ClassLoader clazzLoader, Charset encoding, Location location) {
        this.implementedInterface = implementedInterface;
        this.clazzLoader = clazzLoader;
        this.encoding = encoding;
        this.location = location;
        context = ContextHolder.getContext();
        if (context == null) {
            throw new ScannerException("Unable to scan for resources! Context not set. " +
                    "Within an activity you can fix this with com.fixiu.scanner.android.ContextHolder.setContext(this);");
        }
    }

    @Override
    public Collection<LoadableResource> scanForResources() {
        List<LoadableResource> resources = new ArrayList<>();

        String path = location.getRootPath();
        try {
            for (String asset : context.getAssets().list(path)) {
                if (location.matchesPath(asset)) {
                    resources.add(new AndroidResource(location, context.getAssets(), path, asset, encoding));
                }
            }
        } catch (IOException e) {
            LOG.warn("Unable to scan for resources: " + e.getMessage());
        }

        return resources;
    }

    @Override
    public Collection<Class<? extends I>> scanForClasses() {
        String pkg = location.getRootPath().replace("/", ".");

        List<Class<? extends I>> classes = new ArrayList<>();
        String sourceDir = context.getApplicationInfo().sourceDir;
        DexFile dex = null;
        try {
            dex = new DexFile(sourceDir);
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                if (className.startsWith(pkg)) {
                    Class<? extends I> clazz = ClassUtils.loadClass(implementedInterface, className, clazzLoader);
                    if (clazz != null) {
                        classes.add(clazz);
                    }
                }
            }
        } catch (IOException e) {
            LOG.warn("Unable to scan DEX file (" + sourceDir + "): " + e.getMessage());
        } finally {
            if (dex != null) {
                try {
                    dex.close();
                } catch (IOException e) {
                    LOG.debug("Unable to close DEX file (" + sourceDir + "): " + e.getMessage());
                }
            }
        }
        return classes;
    }
}
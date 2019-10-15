package jinlo.gum.core.utils;


import jinlo.gum.core.exception.EnvironmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;


public class ClassPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPath.class);

    private static final String PATH_SEPARATOR = "path.separator";

    private static final String JAVA_CLASS_PATH = "java.class.path";

    private static final String CLASS_FILE_NAME_EXTENSION = ".class";


    /**
     * Represents a class path resource that can be either a class file or any other resource file
     * loadable from the class path.
     */
    public static class Resource {
        private final String resourceName;

        final ClassLoader loader;

        Resource(String resourceName, ClassLoader loader) {
            this.resourceName = Objects.requireNonNull(resourceName);
            this.loader = Objects.requireNonNull(loader);
        }

        /**
         * Returns the url identifying the resource.
         *
         * <p>See {@link ClassLoader#getResource}
         *
         * @throws NoSuchElementException if the resource cannot be loaded through the class loader,
         *                                despite physically existing in the class path.
         */
        public final URL url() {
            URL url = loader.getResource(resourceName);
            if (url == null) {
                throw new NoSuchElementException(resourceName);
            }
            return url;
        }

        public final ClassLoader getClassLoader() {
            return this.loader;
        }

        /**
         * Returns the fully qualified name of the resource. Such as "com/mycomp/foo/bar.txt".
         */
        public final String getResourceName() {
            return resourceName;
        }

        public final boolean isClass() {
            return resourceName.endsWith(CLASS_FILE_NAME_EXTENSION);
        }

        public final String getClassName() {
            if (!isClass()) {
                throw new IllegalArgumentException();
            } else {
                String className = resourceName.substring(0, resourceName.length() - CLASS_FILE_NAME_EXTENSION.length());
                return className.replace("/", ".");
            }
        }

        @Override
        public int hashCode() {
            return resourceName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Resource) {
                Resource that = (Resource) obj;
                return resourceName.equals(that.resourceName) && loader == that.loader;
            }
            return false;
        }

        // Do not change this arbitrarily. We rely on it for sorting Resource.
        @Override
        public String toString() {
            return resourceName;
        }
    }


    private long scanTime;

    private Set<File> scannedUris;
    private List<Resource> resources;

    public ClassPath(ClassLoader loader) {

        LOGGER.debug("start can class path from {}", loader);
        long startTime = System.currentTimeMillis();

        this.scannedUris = new HashSet<>();
        this.resources = new ArrayList<>();

        if (!(loader instanceof URLClassLoader)) {
            throw new EnvironmentException("must scan classpath with URLClassLoader");
        }
        try {
            scan(loader);
        } catch (IOException e) {
            throw new EnvironmentException(e);
        }
        long endTime = System.currentTimeMillis();
        this.scanTime = endTime - startTime;
        LOGGER.debug("{}", this);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public long getScanTime() {
        return scanTime;
    }


    public final void scan(ClassLoader loader) throws IOException {
        LOGGER.debug("scanning  {}", loader);
        for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(loader).entrySet()) {
            scan(entry.getKey(), entry.getValue());
        }
    }

    private static Map<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
        LinkedHashMap<File, ClassLoader> entries = new LinkedHashMap<>();
        // Search parent first, since it's the order ClassLoader#loadClass() uses.
        ClassLoader parent = classloader.getParent();
        if (parent != null) {
            entries.putAll(getClassPathEntries(parent));
        }
        for (URL url : getClassLoaderUrls(classloader)) {
            if (url.getProtocol().equals("file")) {
                File file = toFile(url);
                if (!entries.containsKey(file)) {
                    entries.put(file, classloader);
                }
            }
        }
        return entries;
    }

    private static List<URL> getClassLoaderUrls(ClassLoader classloader) {
        if (classloader instanceof URLClassLoader) {
            return Arrays.asList(((URLClassLoader) classloader).getURLs());
        }
        if (classloader.equals(ClassLoader.getSystemClassLoader())) {
            return parseJavaClassPath();
        }
        return Collections.emptyList();
    }

    /**
     * Returns the URLs in the class path specified by the {@code java.class.path} {@linkplain
     * System#getProperty system property}.
     */
    private static List<URL> parseJavaClassPath() {
        List<URL> urls = new ArrayList<>();
        for (String entry : System.getProperty(JAVA_CLASS_PATH).split(System.getProperty(PATH_SEPARATOR))) {
            try {
                try {
                    urls.add(new File(entry).toURI().toURL());
                } catch (SecurityException e) { // File.toURI checks to see if the file is a directory
                    urls.add(new URL("file", null, new File(entry).getAbsolutePath()));
                }
            } catch (MalformedURLException e) {
                LOGGER.warn("malformed classpath entry: " + entry, e);
            }
        }
        return urls;
    }

    private void scan(File file, ClassLoader classloader) throws IOException {
        if (scannedUris.add(file.getCanonicalFile())) {
            scanFrom(file, classloader);
        }
    }

    private void scanFrom(File file, ClassLoader classloader) throws IOException {
        try {
            if (!file.exists()) {
                return;
            }
        } catch (SecurityException e) {
            LOGGER.warn("Cannot access " + file + ": " + e);
            // TODO: consider whether to log other failure cases too.
            return;
        }
        if (file.isDirectory()) {
            scanDirectory(classloader, file);
        } else {
            scanJar(file, classloader);
        }
    }

    private void scanJar(File file, ClassLoader classloader) throws IOException {
        JarFile jarFile;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            // Not a jar file
            return;
        }
        try {
            for (File path : getClassPathFromManifest(file, jarFile.getManifest())) {
                scan(path, classloader);
            }
            scanJarFile(classloader, jarFile);
        } finally {
            try {
                jarFile.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void scanJarFile(ClassLoader classloader, JarFile file) {
        Enumeration<JarEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory() || entry.getName().equals(JarFile.MANIFEST_NAME)) {
                continue;
            }
            resources.add(new Resource(entry.getName(), classloader));
        }
    }

    private void scanDirectory(ClassLoader classloader, File directory) throws IOException {
        Set<File> currentPath = new HashSet<>();
        currentPath.add(directory.getCanonicalFile());
        scanDirectory(directory, classloader, "", currentPath);
    }

    /**
     * Recursively scan the given directory, adding resources for each file encountered. Symlinks
     * which have already been traversed in the current tree path will be skipped to eliminate
     * cycles; otherwise symlinks are traversed.
     *
     * @param directory     the root of the directory to scan
     * @param classloader   the classloader that includes resources found in {@code directory}
     * @param packagePrefix resource path prefix inside {@code classloader} for any files found
     *                      under {@code directory}
     * @param currentPath   canonical files already visited in the current directory tree path, for
     *                      cycle elimination
     */
    private void scanDirectory(
            File directory, ClassLoader classloader, String packagePrefix, Set<File> currentPath)
            throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            LOGGER.warn("Cannot read directory " + directory);
            // IO error, just skip the directory
            return;
        }
        for (File f : files) {
            String name = f.getName();
            if (f.isDirectory()) {
                File deref = f.getCanonicalFile();
                if (currentPath.add(deref)) {
                    scanDirectory(deref, classloader, packagePrefix + name + "/", currentPath);
                    currentPath.remove(deref);
                }
            } else {
                String resourceName = packagePrefix + name;
                if (!resourceName.equals(JarFile.MANIFEST_NAME)) {
                    resources.add(new Resource(resourceName, classloader));
                }
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + this.resources.size() + " resources, " + this.scannedUris.size() + " uris, cost " + scanTime + " ms]";
    }


    /**
     * Returns the absolute uri of the Class-Path entry value as specified in <a
     * href="http://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html#Main_Attributes">JAR
     * File Specification</a>. Even though the specification only talks about relative urls,
     * absolute urls are actually supported too (for example, in Maven surefire plugin).
     */
    private static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
        return new URL(jarFile.toURI().toURL(), path);
    }

    /**
     * Returns the class path URIs specified by the {@code Class-Path} manifest attribute, according
     * to <a
     * href="http://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html#Main_Attributes">JAR
     * File Specification</a>. If {@code manifest} is null, it means the jar file has no manifest,
     * and an empty set will be returned.
     */
    private static Set<File> getClassPathFromManifest(File jarFile, Manifest manifest) {
        if (manifest == null) {
            return Collections.emptySet();
        }
        Set<File> files = new HashSet<>();
        String classpathAttribute =
                manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
        if (classpathAttribute != null) {
            // split by blank, skip empty
            for (String path : classpathAttribute.split(" ")) {
                if (path.isEmpty()) {
                    continue;
                }
                URL url;
                try {
                    url = getClassPathEntry(jarFile, path);
                } catch (MalformedURLException e) {
                    // Ignore bad entry
                    LOGGER.warn("Invalid Class-Path entry: {}", path);
                    continue;
                }
                if (url.getProtocol().equals("file")) {
                    files.add(toFile(url));
                }
            }
        }
        return files;
    }

    private static File toFile(URL url) {
        if (!url.getProtocol().equals("file")) {
            throw new IllegalArgumentException(url + " is not file");
        }
        try {
            return new File(url.toURI()); // Accepts escaped characters like %20.
        } catch (URISyntaxException e) { // URL.toURI() doesn't escape chars.
            return new File(url.getPath()); // Accepts non-escaped chars like space.
        }
    }
}
package jinlo.gum.core.utils;


import jinlo.gum.core.exception.EnvironmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ClassPath {

    private static final Logger LOGGER= LoggerFactory.getLogger(ClassPath.class);

    /**
     * A representation for a resource on the classpath
     */
    public interface Resource {

        /**
         * @return
         */
        Path getPath();

        /**
         * @return
         */
        String getResourceName();

        /**
         * @return <code>true</code> if candidate to be loaded by its
         * {@link ClassLoader} otherwise <code>false</code>
         */
        boolean isClassResource();
    }


    private long scanTime = -1;

    private List<Resource> resources;

    private List<String> errorMessages;

    public ClassPath(ClassLoader loader){

        LOGGER.debug("start can class path from {}",loader);
        long startTime=System.currentTimeMillis();

        this.resources=new ArrayList<>();
        this.errorMessages =new ArrayList<>();
        if (!(loader instanceof URLClassLoader)){
            throw new EnvironmentException("must scan classpath with URLClassLoader");
        }
        scan((URLClassLoader)loader);
        long endTime=System.currentTimeMillis();
        this.scanTime=endTime-startTime;
        LOGGER.debug("{}",this);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public long getScanTime() {
        return scanTime;
    }

    private void scan(URLClassLoader loader) {
        LOGGER.debug("scanning  {}",loader);
        for (URL url : loader.getURLs()) {

            if (url.getFile() != null && !url.getFile().isEmpty()) {
                final File f = new File(URLDecoder.decode(url.getFile()));
                if (f.exists() && f.isDirectory()) {
                    scanDirectory(loader, url, f);
                } else if (f.exists() && f.isFile() && f.getName().toLowerCase().endsWith(".jar")) {
                    scanJar(loader, url, f);
                } else {
                    String msg="can not found "+f;
                    LOGGER.warn(msg);
                    errorMessages.add(msg);
                }
            }
        }
    }



    private void scanJar(URLClassLoader classLoader, URL url, File jarFile) {
        try {
            final FileSystem fs = FileSystems.newFileSystem(Paths.get(url.toURI()), null);
            Path start=fs.getRootDirectories().iterator().next();
            LOGGER.debug("scanning {}",jarFile);
            Files.walkFileTree(start, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)  {
                    String theResourceName = file.toString().substring(1);
                    scanPath(classLoader, theResourceName, file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)  {
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            String msg="failed scanning jar:"+jarFile;
            LOGGER.warn(msg,e);
            this.errorMessages.add(msg+e);
        }
    }

    private void scanDirectory(URLClassLoader classLoader, URL url, File dir) {
        try {
            final File rootDir = dir.getCanonicalFile();
            final int rootDirNameLen = rootDir.getCanonicalPath().length();
            Path start=rootDir.toPath();
            LOGGER.debug("scanning {}",rootDir);
            Files.walkFileTree(start, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final String resourceName = file.toFile().getCanonicalPath().substring(rootDirNameLen + 1);
                    scanPath(classLoader, resourceName, file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)  {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)  {
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
            String msg="failed scanning directory:"+dir;
            LOGGER.warn(msg,e);
            this.errorMessages.add(msg+e);
        }
    }

    private void scanPath(final URLClassLoader classLoader, final String resourceName,
                          final Path path) {
        final boolean isClassResource = resourceName.toLowerCase().endsWith(".class");
        final String finalResourceName = !isClassResource ? resourceName
                : resourceName.substring(0, resourceName.length() - 6).replace('/', '.').replace('\\', '.');
        LOGGER.trace("scanning {}",finalResourceName);
        final Resource resource = new Resource() {

            @Override
            public Path getPath() {
                return path;
            }

            @Override
            public String getResourceName() {
                return finalResourceName;
            }

            @Override
            public boolean isClassResource() {
                return isClassResource;
            }

            @Override
            public String toString() {
                StringBuilder sb=new StringBuilder();
                sb.append(Resource.class.getSimpleName() )
                        .append(" [resourceName=")
                        .append(finalResourceName)
                        .append(", path=")
                        .append(path)
                        .append(", isClassResource=")
                        .append(isClassResource)
                        .append("]");
                return sb.toString();
            }

        };

        this.resources.add(resource);
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(getClass().getSimpleName() )
                .append("[")
                .append(this.resources.size())
                .append(" resources")
                .append(", ")
                .append(this.errorMessages.size())
                .append(" error messages")
                .append(", scan time ")
                .append(scanTime)
                .append(" ms")
                .append("]");
        return sb.toString();
    }
}
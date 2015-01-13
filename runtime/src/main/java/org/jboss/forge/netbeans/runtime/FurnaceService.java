/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.runtime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.exception.ContainerException;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.se.FurnaceFactory;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.forge.furnace.spi.ContainerLifecycleListener;
import org.jboss.forge.furnace.util.OperatingSystemUtils;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 * Furnace services helper class
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum FurnaceService {

    INSTANCE;

    private Furnace furnace;

    public boolean isLoaded() {
        return furnace != null && furnace.getStatus().isStarted();
    }

    public CommandFactory getCommandFactory() {
        return lookup(CommandFactory.class);
    }

    public CommandControllerFactory getCommandControllerFactory() {
        return lookup(CommandControllerFactory.class);
    }

    public ConverterFactory getConverterFactory() {
        return lookup(ConverterFactory.class);
    }

    public ResourceFactory getResourceFactory() {
        return lookup(ResourceFactory.class);
    }

    public <S> Imported<S> lookupImported(Class<S> service) {
        if (furnace == null) {
            createFurnace();
        }
        Imported<S> importedService = null;
        if (furnace != null) {
            importedService = furnace.getAddonRegistry().getServices(service);
        }
        return importedService;
    }

    public <S> S lookup(Class<S> service) {
        if (furnace == null) {
            createFurnace();
        }
        Imported<S> importedService = null;
        if (furnace != null) {
            importedService = furnace.getAddonRegistry().getServices(service);
        }
        return (importedService == null || importedService.isUnsatisfied()) ? null : importedService.get();
    }

    public void stop() {
        if (furnace != null) {
            furnace.stop();
            furnace = null;
        }
    }

    /**
     * TODO: This method should be moved to another class
     */
    private void createFurnace() {
        try {
            // MODULES-136
            System.setProperty("modules.ignore.jdk.factory", "true");
            URL codeSourceLocation = bootpath.BootpathMarker.class.getProtectionDomain().getCodeSource().getLocation();
            URL location = new URL(codeSourceLocation.toString().replace("jar:", "").replace("!/", ""));
            List<URL> urls = new ArrayList<>();
            try (ZipInputStream zis = new ZipInputStream(location.openStream())) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().contains(".jar")) {
                        urls.add(bootpath.BootpathMarker.class.getResource(entry.getName().replace("bootpath/", "")));
                    }
                }
            }
            urls = flushURLs(urls);
            final URLClassLoader furnaceClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
            furnace = FurnaceFactory.getInstance(getClass().getClassLoader(), furnaceClassLoader);
//            String cnb = Modules.getDefault().ownerOf(getClass()).getCodeNameBase();
//            File locate = InstalledFileLocator.getDefault().locate("addon-repository", cnb, false);
//            System.out.println("LOCATE> "+locate);
            furnace.addRepository(AddonRepositoryMode.IMMUTABLE, new File("/home/ggastald/workspace/netbeans-plugin/runtime/target/classes/addon-repository"));
            furnace.addRepository(AddonRepositoryMode.MUTABLE, new File(OperatingSystemUtils.getUserForgeDir(), "addons"));
            furnace.addContainerLifecycleListener(new ContainerLifecycleListener() {

                @Override
                public void beforeStart(Furnace furnace) throws ContainerException {
                }

                @Override
                public void beforeConfigurationScan(Furnace furnace) throws ContainerException {
                }

                @Override
                public void afterConfigurationScan(Furnace furnace) throws ContainerException {
                }

                @Override
                public void beforeStop(Furnace furnace) throws ContainerException {
                }

                @Override
                public void afterStop(Furnace furnace) throws ContainerException {
                    try {
                        // Close open URLClassLoader
                        furnaceClassLoader.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
            furnace.startAsync();
            while (!furnace.getStatus().isStarted()) {
                Thread.sleep(500);
            }

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private List<URL> flushURLs(List<URL> urls) throws IOException {
        List<URL> result = new ArrayList<>();
        File tmpDir = OperatingSystemUtils.createTempDir();
        for (URL url : urls) {
            String path = url.getPath();
            path = path.substring(path.lastIndexOf("/") + 1);
            File newFile = new File(tmpDir, path);
            Files.copy(url.openStream(), newFile.toPath());
            result.add(Utilities.toURI(newFile).toURL());
        }
        return result;
    }
}

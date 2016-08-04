/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.runtime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
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
import org.jboss.forge.furnace.util.AddonCompatibilityStrategies;
import org.jboss.forge.furnace.util.OperatingSystemUtils;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.Modules;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
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
            createFurnace(true);
        }
        Imported<S> importedService = null;
        if (furnace != null) {
            importedService = furnace.getAddonRegistry().getServices(service);
        }
        return importedService;
    }

    public <S> S lookup(Class<S> service) {
        if (furnace == null) {
            createFurnace(true);
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

    public void start() {
        createFurnace(false);
    }

    /**
     * TODO: This method should be moved to another class
     */
    private synchronized void createFurnace(boolean wait) {
        try {
            if (furnace != null) {
                while (wait && !furnace.getStatus().isStarted()) {
                    Thread.sleep(500);
                }
                return;
            }
            // MODULES-136
            System.setProperty("modules.ignore.jdk.factory", "true");
            String cnb = Modules.getDefault().ownerOf(getClass()).getCodeNameBase();
            File bootpath = InstalledFileLocator.getDefault().locate("bootpath", cnb, false);
            URL[] urls = toURLs(bootpath.listFiles());
            final URLClassLoader furnaceClassLoader = new URLClassLoader(urls);
            furnace = FurnaceFactory.getInstance(getClass().getClassLoader(), furnaceClassLoader);
            
            File locate = InstalledFileLocator.getDefault().locate("addon-repository", cnb, false);
            if (locate != null) {
                furnace.addRepository(AddonRepositoryMode.IMMUTABLE, locate);
            } else {
                throw new Exception("Default Addon Repository path not found");
            }
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
                public void afterStart(Furnace frnc) throws ContainerException {
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
            for (ContainerLifecycleListener listener : Lookup.getDefault().lookupAll(ContainerLifecycleListener.class)) {
                furnace.addContainerLifecycleListener(listener);
            }
            // Using a lenient addon compatibility strategy
            furnace.setAddonCompatibilityStrategy(AddonCompatibilityStrategies.LENIENT);
            furnace.startAsync();
            while (wait && !furnace.getStatus().isStarted()) {
                Thread.sleep(500);
            }

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private URL[] toURLs(File[] files) throws IOException {
        int length = files.length;
        URL[] urls = new URL[length];
        for (int i = 0; i < length; i++) {
            urls[i] = Utilities.toURI(files[i]).toURL();
        }
        return urls;
    }
}

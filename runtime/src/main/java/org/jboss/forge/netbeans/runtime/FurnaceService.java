/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.runtime;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.context.UIContextListener;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.exception.ContainerException;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.se.FurnaceFactory;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.forge.furnace.spi.ContainerLifecycleListener;
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
            // TODO: List the JARs dinamically
            URL[] urls = {
                Utilities.toURI(new File("/home/ggastald/.m2/repository/org/jboss/forge/forge-javassist/2/forge-javassist-2.jar")).toURL(),
                Utilities.toURI(new File("/home/ggastald/.m2/repository/org/jboss/forge/furnace/furnace/2.12.1.Final/furnace-2.12.1.Final.jar")).toURL(),
                Utilities.toURI(new File("/home/ggastald/.m2/repository/org/jboss/forge/furnace/furnace-api/2.12.1.Final/furnace-api-2.12.1.Final.jar")).toURL(),
                Utilities.toURI(new File("/home/ggastald/.m2/repository/org/jboss/forge/furnace/furnace-proxy/2.12.1.Final/furnace-proxy-2.12.1.Final.jar")).toURL(),
                Utilities.toURI(new File("/home/ggastald/.m2/repository/org/jboss/logmanager/jboss-logmanager/1.4.1.Final/jboss-logmanager-1.4.1.Final.jar")).toURL(),
                Utilities.toURI(new File("/home/ggastald/.m2/repository/org/jboss/forge/jboss-modules/1.3.0.Final-forge/jboss-modules-1.3.0.Final-forge.jar")).toURL(),
                Utilities.toURI(new File("/home/ggastald/.m2/repository/net/sf/jgrapht/jgrapht/0.8.3/jgrapht-0.8.3.jar")).toURL(),
                Utilities.toURI(new File("/home/ggastald/.m2/repository/org/jboss/forge/xml-parser/1.0.0.Final/xml-parser-1.0.0.Final.jar")).toURL()
            };
            final URLClassLoader furnaceClassLoader = new URLClassLoader(urls);
            furnace = FurnaceFactory.getInstance(getClass().getClassLoader(), furnaceClassLoader);
            //TODO: Change this
            furnace.addRepository(AddonRepositoryMode.IMMUTABLE, new File("/home/ggastald/workspace/netbeans-plugin/runtime/target/classes/addon-repository"));
            furnace.addRepository(AddonRepositoryMode.MUTABLE, new File("/home/ggastald/.forge/addons"));
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
}

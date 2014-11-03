/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.runtime;

import bootpath.BootpathMarker;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.se.FurnaceFactory;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 * Performs the necessary steps to boot up Furnace
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        createFurnace();
    }

    @Override
    public boolean closing() {
        FurnaceService.INSTANCE.stop();
        return super.closing();
    }

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
            URLClassLoader furnaceClassLoader = new URLClassLoader(urls);
            Furnace furnace = FurnaceFactory.getInstance(getClass().getClassLoader(), furnaceClassLoader);
            //TODO: Change this
            furnace.addRepository(AddonRepositoryMode.IMMUTABLE, new File("/home/ggastald/workspace/netbeans-plugin/runtime/target/classes/addon-repository"));
            furnace.addRepository(AddonRepositoryMode.MUTABLE, new File("/home/ggastald/.forge/addons"));
            furnace.startAsync();
            FurnaceService.INSTANCE.setFurnace(furnace);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}

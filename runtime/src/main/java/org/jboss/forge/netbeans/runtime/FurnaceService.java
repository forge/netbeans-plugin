/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.runtime;

import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.services.Imported;

/**
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

    public <S> S lookup(Class<S> service) {
        Imported<S> exportedInstance = null;
        if (furnace != null) {
            exportedInstance = furnace.getAddonRegistry().getServices(service);
        }
        return (exportedInstance == null || exportedInstance.isUnsatisfied()) ? null : exportedInstance.get();
    }

    public void setFurnace(Furnace furnace) {
        this.furnace = furnace;
    }

    public void stop() {
        if (furnace != null) {
            furnace.stop();
            furnace = null;
        }
    }
}

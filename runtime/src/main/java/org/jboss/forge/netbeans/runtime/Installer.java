/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.runtime;

import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.util.Selections;
import org.openide.modules.ModuleInstall;

/**
 * Performs the necessary steps to clean up Furnace
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Installer extends ModuleInstall {

    /**
     * Stops the running Furnace instance
     *
     * @return always true
     */
    @Override
    public boolean closing() {
        UISelection<Object> emptySelection = Selections.emptySelection();
        FurnaceService.INSTANCE.stop();
        return true;
    }

}

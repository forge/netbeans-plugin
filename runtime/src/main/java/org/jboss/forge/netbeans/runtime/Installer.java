/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.runtime;

import org.openide.modules.ModuleInstall;

/**
 * Performs the necessary steps to clean up Furnace
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Installer extends ModuleInstall {

    @Override
    public boolean closing() {
        FurnaceService.INSTANCE.stop();
        return super.closing();
    }

}

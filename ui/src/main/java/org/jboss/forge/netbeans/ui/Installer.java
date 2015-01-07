/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui;

import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ProjectListener;
import org.jboss.forge.furnace.spi.ListenerRegistration;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.jboss.forge.netbeans.ui.listener.NbProjectListener;
import org.openide.modules.ModuleInstall;

/**
 * Actions to be performed during the module's lifecycle
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Installer extends ModuleInstall {

    private ListenerRegistration<ProjectListener> projectListenerRegistration;

    @Override
    public void restored() {
        // Install ProjectListener
        ProjectFactory projectFactory = FurnaceService.INSTANCE.lookup(ProjectFactory.class);
        if (projectFactory != null) {
            projectListenerRegistration = projectFactory.addProjectListener(new NbProjectListener());
        }
    }

    @Override
    public boolean closing() {
        if (projectListenerRegistration != null) {
            projectListenerRegistration.removeListener();
        }
        return true;
    }
}

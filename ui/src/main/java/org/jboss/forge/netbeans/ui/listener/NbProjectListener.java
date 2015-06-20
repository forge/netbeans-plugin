/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.listener;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ProjectListener;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.exception.ContainerException;
import org.jboss.forge.furnace.spi.ContainerLifecycleListener;
import org.jboss.forge.furnace.spi.ListenerRegistration;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.ServiceProvider;

/**
 * Called when the plugin starts and auto-registers on the ProjectFactory
 * <p/>
 * It implements the {@link ContainerLifecycleListener} interface to be called
 * when Furnace starts
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(service = ContainerLifecycleListener.class)
public class NbProjectListener implements ProjectListener, ContainerLifecycleListener {

    private final RequestProcessor processor = new RequestProcessor(NbProjectListener.class);
    private ListenerRegistration<ProjectListener> projectListenerRegistration;

    @Override
    public void projectCreated(Project project) {
        ProjectImporter.INSTANCE.addProject(project);
    }

    @Override
    public void beforeStart(Furnace furnace) throws ContainerException {
    }

    @Override
    public void afterStart(Furnace frnc) throws ContainerException {
        // Install ProjectListener
        processor.submit(new Runnable() {
            @Override
            public void run() {
                ProjectFactory projectFactory = FurnaceService.INSTANCE.lookup(ProjectFactory.class);
                while (projectFactory == null) {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException ex) {
                        return;
                    }
                    projectFactory = FurnaceService.INSTANCE.lookup(ProjectFactory.class);
                }
                projectListenerRegistration = projectFactory.addProjectListener(NbProjectListener.this);
            }
        });
    }

    @Override
    public void beforeStop(Furnace furnace) throws ContainerException {
        if (projectListenerRegistration != null) {
            projectListenerRegistration.removeListener();
        }
        processor.shutdown();
    }

    @Override
    public void afterStop(Furnace furnace) throws ContainerException {
    }

    @Override
    public void beforeConfigurationScan(Furnace furnace) throws ContainerException {
    }

    @Override
    public void afterConfigurationScan(Furnace furnace) throws ContainerException {
    }

}

/*
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JBoss by Red Hat - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.listener;

import org.jboss.forge.addon.ui.command.CommandExecutionListener;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.result.Result;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum ProjectImporterCommandExecutionListener implements CommandExecutionListener {
    INSTANCE;

    @Override
    public void preCommandExecuted(UICommand command, UIExecutionContext context) {
    }

    @Override
    public void postCommandExecuted(UICommand command, UIExecutionContext context, Result result) {
        ProjectImporter.INSTANCE.importProjects();
    }

    @Override
    public void postCommandFailure(UICommand command, UIExecutionContext context, Throwable failure) {
        ProjectImporter.INSTANCE.clear();
    }
    
    
}

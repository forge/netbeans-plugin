/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.progress;

import org.jboss.forge.addon.ui.progress.UIProgressMonitor;
import org.netbeans.api.progress.ProgressHandle;

/**
 * Implementation of UIProgressMonitor
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIProgressMonitor implements UIProgressMonitor {

    private final ProgressHandle progressHandle;
    private boolean cancelled;

    public NbUIProgressMonitor(ProgressHandle progressHandle) {
        this.progressHandle = progressHandle;
    }

    @Override
    public void beginTask(String name, int totalWork) {
        progressHandle.setDisplayName(name);
        progressHandle.start(totalWork);
    }

    @Override
    public void done() {
        progressHandle.finish();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public void setTaskName(String name) {
        progressHandle.setDisplayName(name);
    }

    @Override
    public void subTask(String name) {
        progressHandle.progress(name);
    }

    @Override
    public void worked(int work) {
        progressHandle.progress(work);
    }
}

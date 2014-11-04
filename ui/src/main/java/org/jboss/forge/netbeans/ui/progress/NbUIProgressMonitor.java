/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.progress;

import org.jboss.forge.addon.ui.progress.UIProgressMonitor;
import org.netbeans.api.progress.ProgressHandle;

/**
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
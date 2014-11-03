/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.context;

import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.UIProvider;
import org.jboss.forge.addon.ui.context.AbstractUIContext;
import org.jboss.forge.addon.ui.context.UIContextListener;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.openide.util.Exceptions;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIContext extends AbstractUIContext {

    private final UISelection<Resource<?>> initialSelection;

    public NbUIContext(UISelection<Resource<?>> initialSelection) {
        this.initialSelection = initialSelection;
        initialize();
    }

    @Override
    public <SELECTIONTYPE> UISelection<SELECTIONTYPE> getInitialSelection() {
        return (UISelection<SELECTIONTYPE>) initialSelection;
    }

    @Override
    public UIProvider getProvider() {
        return NbUIProvider.INSTANCE;
    }

    private void initialize() {
        Imported<UIContextListener> services = FurnaceService.INSTANCE
                .lookupImported(UIContextListener.class);
        if (services != null) {
            for (UIContextListener listener : services) {
                try {
                    listener.contextInitialized(this);
                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                }
            }
        }
    }

    @Override
    public void close() {
        super.close();
        Imported<UIContextListener> services = FurnaceService.INSTANCE
                .lookupImported(UIContextListener.class);
        if (services != null) {
            for (org.jboss.forge.addon.ui.context.UIContextListener listener : services) {
                try {
                    listener.contextDestroyed(this);
                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                }
            }
        }
    }
}

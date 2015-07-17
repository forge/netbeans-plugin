/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui;

import org.jboss.forge.addon.ui.UIDesktop;
import org.jboss.forge.addon.ui.UIProvider;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.jboss.forge.netbeans.ui.output.NbUIOutput;

/**
 * Implementation of UIProvider interface
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum NbUIProvider implements UIProvider {

    GUI(true),
    NON_GUI(false);

    private final boolean gui;

    private NbUIProvider(boolean gui) {
        this.gui = gui;
    }

    @Override
    public boolean isGUI() {
        return gui;
    }

    @Override
    public UIOutput getOutput() {
        return new NbUIOutput();
    }

    @Override
    public UIDesktop getDesktop() {
        return new NbUIDesktop();
    }

    @Override
    public String getName() {
        return "NetBeans";
    }

    @Override
    public boolean isEmbedded() {
        return true;
    }
}

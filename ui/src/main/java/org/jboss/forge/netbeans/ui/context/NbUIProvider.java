/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.context;

import org.jboss.forge.addon.ui.UIProvider;
import org.jboss.forge.addon.ui.output.UIOutput;

/**
 * Implementation of UIProvider interface
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum NbUIProvider implements UIProvider {

    INSTANCE;

    @Override
    public boolean isGUI() {
        return true;
    }

    @Override
    public UIOutput getOutput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

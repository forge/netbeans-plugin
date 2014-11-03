/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.provider;

import org.jboss.forge.addon.ui.UIRuntime;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.progress.UIProgressMonitor;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIRuntime implements UIRuntime {

    @Override
    public UIProgressMonitor createProgressMonitor(UIContext context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UIPrompt createPrompt(UIContext context) {
        return new NbUIPrompt();
    }
    
}

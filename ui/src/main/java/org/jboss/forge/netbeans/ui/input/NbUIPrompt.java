/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.input;

import javax.swing.JOptionPane;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIPrompt implements UIPrompt {

    @Override
    public String prompt(String message) {
        NotifyDescriptor.InputLine d = new NotifyDescriptor.InputLine(message, "");
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION) {
            return d.getInputText();
        }
        return null;
    }

    @Override
    public String promptSecret(String message) {
        //TODO: Mask input
        NotifyDescriptor.InputLine d = new NotifyDescriptor.InputLine(message, "");
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION) {
            return d.getInputText();
        }
        return null;
    }

    @Override
    public boolean promptBoolean(String message) {
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(message, "");
        return (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION);
    }

    @Override
    public boolean promptBoolean(String message, boolean defaultValue) {
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(message, "");
        Object result = DialogDisplayer.getDefault().notify(d);
        if (result == NotifyDescriptor.CANCEL_OPTION) {
            return defaultValue;
        }
        return (result == NotifyDescriptor.OK_OPTION);
    }
}

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
        if (NotifyDescriptor.OK_OPTION.equals(DialogDisplayer.getDefault().notify(d))) {
            return d.getInputText();
        }
        return null;
    }

    @Override
    public String promptSecret(String message) {
        //TODO: Mask input
        NotifyDescriptor.InputLine d = new NotifyDescriptor.InputLine(message, "");
        if (NotifyDescriptor.OK_OPTION.equals(DialogDisplayer.getDefault().notify(d))) {
            return d.getInputText();
        }
        return null;
    }

    @Override
    public boolean promptBoolean(String message) {
        return promptBoolean(message, false);
    }

    @Override
    public boolean promptBoolean(String message, boolean defaultValue) {
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(message, "");
        Object result = DialogDisplayer.getDefault().notify(d);
        if (NotifyDescriptor.CANCEL_OPTION.equals(result)) {
            return defaultValue;
        }
        return (NotifyDescriptor.OK_OPTION.equals(result));
    }
}

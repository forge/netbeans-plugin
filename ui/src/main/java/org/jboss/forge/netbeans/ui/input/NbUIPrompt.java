/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.input;

import org.jboss.forge.addon.ui.input.UIPrompt;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Implementation of the {@link UIPrompt} interface
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

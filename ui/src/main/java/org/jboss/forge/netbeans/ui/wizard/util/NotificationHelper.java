/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.wizard.util;

import java.util.Iterator;
import org.jboss.forge.addon.ui.output.UIMessage;
import org.jboss.forge.addon.ui.result.CompositeResult;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.NotificationLineSupport;
import org.openide.WizardDescriptor;
import org.openide.awt.NotificationDisplayer;
import org.openide.util.ImageUtilities;

/**
 * A helper class for notification purposes
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NotificationHelper {

    @StaticResource
    private static final String ICON = "org/jboss/forge/netbeans/ui/forge.png";

    /**
     * Displays notifications on the provided {@link WizardDescriptor}
     *
     * @param descriptor
     * @param messages
     */
    public static void displayNotifications(WizardDescriptor descriptor, Iterable<UIMessage> messages) {
        final NotificationLineSupport notificationLineSupport = descriptor.getNotificationLineSupport();
        notificationLineSupport.clearMessages();
        Iterator<UIMessage> it = messages.iterator();
        // Only one message is displayed at a time
        if (it.hasNext()) {
            UIMessage message = it.next();
            if (message.getSeverity() == UIMessage.Severity.ERROR) {
                notificationLineSupport.setErrorMessage(message.getDescription());
            } else if (message.getSeverity() == UIMessage.Severity.INFO) {
                notificationLineSupport.setWarningMessage(message.getDescription());
            } else {
                notificationLineSupport.setInformationMessage(message.getDescription());
            }
        }
    }

    /**
     * Displays the result in the IDE
     *
     * @param result
     */
    public static void displayResult(String title, Result result) {
        if (result != null) {
            if (result instanceof CompositeResult) {
                for (Result childResult : ((CompositeResult) result).getResults()) {
                    displayResult(title, childResult);
                }
            } else {
                final NotificationDisplayer notificationDisplayer = NotificationDisplayer.getDefault();
                if (result.getMessage() != null) {
                    if (result instanceof Failed) {
                        notificationDisplayer.notify(title, ImageUtilities.loadImageIcon(ICON, false), result.getMessage(), null);
                    } else {
                        notificationDisplayer.notify(title, ImageUtilities.loadImageIcon(ICON, false), result.getMessage(), null);
                    }
                }
            }
        }
    }
}

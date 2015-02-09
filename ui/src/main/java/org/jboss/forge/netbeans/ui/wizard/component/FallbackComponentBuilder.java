/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import org.jboss.forge.addon.ui.input.InputComponent;

/**
 * Generates a JTextField with a JLabel
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class FallbackComponentBuilder extends TextboxComponentBuilder {

    @Override
    public boolean handles(InputComponent<?, ?> input) {
        return true;
    }

}

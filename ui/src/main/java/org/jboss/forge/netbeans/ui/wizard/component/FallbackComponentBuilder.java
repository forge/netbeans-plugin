/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

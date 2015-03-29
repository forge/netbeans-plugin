/*
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    George Gastaldi <ggastald@redhat.com> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.wizard.component.completion;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.CompletionProviderBase;
import org.fife.ui.autocomplete.ParameterizedCompletion;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UICompleter;

/**
 * Implementation of {@link CompletionProvider}
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ForgeAutoCompletionProvider extends CompletionProviderBase {

    private final UIContext context;
    private final InputComponent<?, ?> inputComponent;
    private final UICompleter<String> completer;

    public ForgeAutoCompletionProvider(UIContext context, InputComponent<?, ?> inputComponent, UICompleter<String> completer) {
        this.context = context;
        this.inputComponent = inputComponent;
        this.completer = completer;
    }

    private List<Completion> getCompletionByInputText(String inputText) {
        List<Completion> result = new ArrayList<>();
        if (inputText != null) {
            Iterable<String> completionProposals = completer.getCompletionProposals(context, (InputComponent<?, String>) inputComponent, inputText);
            if (completionProposals != null) {
                for (String proposal : completionProposals) {
                    result.add(new BasicCompletion(this, proposal));
                }
            }
        }
        return result;
    }

    @Override
    public String getAlreadyEnteredText(JTextComponent comp) {
        return comp.getText();
    }

    @Override
    protected List<Completion> getCompletionsImpl(JTextComponent comp) {
        String inputText = getAlreadyEnteredText(comp);
        return getCompletionByInputText(inputText);
    }

    @Override
    public List<Completion> getCompletionsAt(JTextComponent comp, Point p) {
        return getCompletions(comp);
    }

    @Override
    public List<ParameterizedCompletion> getParameterizedCompletions(JTextComponent tc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

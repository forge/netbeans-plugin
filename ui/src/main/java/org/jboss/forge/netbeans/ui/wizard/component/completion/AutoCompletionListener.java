/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component.completion;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UICompleter;
import org.openide.util.Exceptions;

/**
 * Autocomplete listener based on Oracle's tutorial:
 * http://docs.oracle.com/javase/tutorial/uiswing/components/textarea.html
 *
 * Based on
 * http://stackabuse.com/article/example-code/example-adding-autocomplete-to-jtextfield
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class AutoCompletionListener implements DocumentListener {

    private static final String COMMIT_ACTION = "commit";
    private final JTextComponent textField;
    private final UIContext context;
    private final InputComponent<?, Object> component;
    private final UICompleter<String> completer;
    private boolean completed;

    public AutoCompletionListener(final JTextComponent textField, UIContext context, InputComponent<?, Object> component, UICompleter<String> completer) {
        this.textField = textField;
        this.context = context;
        this.component = component;
        this.completer = completer;

        // Maps the tab key to the commit action, which finishes the autocomplete
        // when given a suggestion 
        textField.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        textField.getActionMap().put(COMMIT_ACTION, new CommitAction());
    }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        if (ev.getLength() != 1) {
            return;
        }

        int pos = ev.getOffset();
        String content = "";
        try {
            content = textField.getText(0, pos + 1);
        } catch (BadLocationException e) {
            Exceptions.printStackTrace(e);
        }

        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (!Character.isLetter(content.charAt(w))) {
                break;
            }
        }

        // Too few chars
        if (pos - w < 2) {
            return;
        }

        String prefix = content.substring(w + 1);
        Iterable<String> completionProposals = completer.getCompletionProposals(context, null, prefix);
        Iterator<String> iterator = completionProposals.iterator();
        if (iterator.hasNext()) {
            String match = iterator.next();
            // A completion is found
            String completion = match.substring(pos - w);
            // We cannot modify Document from within notification,
            // so we submit a task that does the change later
            SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
        } else {
            // Nothing found
            completed = false;
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // Do nothing
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Do nothing
    }

    private class CommitAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (completed) {
                int pos = textField.getSelectionEnd();
                StringBuilder sb = new StringBuilder(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos + 1);
                completed = false;
            } else {
                textField.replaceSelection("\t");
            }
        }
    }

    private class CompletionTask implements Runnable {

        private final String completion;
        private final int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        @Override
        public void run() {
            StringBuilder sb = new StringBuilder(textField.getText());
            sb.insert(position, completion);
            textField.setText(sb.toString());
            textField.setCaretPosition(position + completion.length());
            textField.moveCaretPosition(position);
            completed = true;
        }
    }

}

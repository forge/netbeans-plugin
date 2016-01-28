/* 
 * Copyright (c) 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.context;

import java.util.Optional;
import javax.swing.JEditorPane;
import javax.swing.text.Element;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.context.UIRegion;

/**
 * Implementation of the UIRegion interface
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIRegion implements UIRegion<Resource<?>> {

    private final Resource<?> resource;
    private final JEditorPane editorPane;

    public NbUIRegion(Resource<?> resource, JEditorPane editorPane) {
        this.resource = resource;
        this.editorPane = editorPane;
    }

    @Override
    public int getStartPosition() {
        return editorPane.getSelectionStart();
    }

    @Override
    public int getEndPosition() {
        return editorPane.getSelectionEnd();
    }

    @Override
    public int getStartLine() {
        Element map = editorPane.getDocument().getDefaultRootElement();
        return map.getElementIndex(getStartPosition());
    }

    @Override
    public int getEndLine() {
        Element map = editorPane.getDocument().getDefaultRootElement();
        return map.getElementIndex(getEndPosition());
    }

    @Override
    public Optional<String> getText() {
        return Optional.ofNullable(editorPane.getSelectedText());
    }

    @Override
    public Resource<?> getResource() {
        return resource;
    }
}

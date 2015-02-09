/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.controller.WizardCommandController;
import org.jboss.forge.addon.ui.result.Result;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;

/**
 * The Wizard Descriptor Iterator
 *
 * @author ggastald
 */
public class ForgeWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {

    private final WizardCommandController controller;
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private final LinkedList<String> wizardTitles = new LinkedList<>();

    private WizardDescriptor wizardDescriptor;
    private ForgeWizardPanel current;
    private Result executionResult;

    public ForgeWizardIterator(WizardCommandController controller) {
        this.controller = controller;
        wizardTitles.add(controller.getMetadata().getName());
    }

    @Override
    public ForgeWizardPanel current() {
        if (current == null) {
            refreshCurrentPanel();
        }
        return current;
    }

    @Override
    public String name() {
        return controller.getMetadata().getDescription();
    }

    @Override
    public boolean hasNext() {
        return controller.canMoveToNextStep();
    }

    @Override
    public boolean hasPrevious() {
        return controller.canMoveToPreviousStep();
    }

    @Override
    public void nextPanel() {
        try {
            controller.next().initialize();
            wizardTitles.add(controller.getMetadata().getName());
            refreshCurrentPanel();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void previousPanel() {
        try {
            controller.previous();
            wizardTitles.removeLast();
            refreshCurrentPanel();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void refreshCurrentPanel() {
        this.current = new ForgeWizardPanel(controller);
        this.current.setWizardDescriptor(wizardDescriptor);
        wizardDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DATA, wizardTitles.toArray(new String[wizardTitles.size()]));
        wizardDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, wizardTitles.size() - 1);
    }

    @Override
    public Set instantiate() throws IOException {
        return instantiate(null);
    }

    /**
     * Called when finish is clicked
     */
    @Override
    public Set instantiate(ProgressHandle handle) throws IOException {
        UIContext context = controller.getContext();
        try {
            if (handle != null) {
                // see NbUIRuntime.createProgressMonitor
                Map<Object, Object> attributeMap = context.getAttributeMap();
                attributeMap.put(ProgressHandle.class, handle);
            }
            this.executionResult = controller.execute();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return selectNodes(context);
    }

    private Set<Node> selectNodes(UIContext context) {
        Set<Node> nodes = new LinkedHashSet<>();
        for (Object resource : context.getSelection()) {
            if (resource instanceof FileResource) {
                File file = ((FileResource<?>) resource).getUnderlyingResourceObject();
                DataObject dataObject = FileUtil.toFileObject(file).getLookup().lookup(DataObject.class);
                if (dataObject != null) {
                    nodes.add(dataObject.getNodeDelegate());
                }
            }
        }
        return nodes;
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        changeSupport.addChangeListener(cl);
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        changeSupport.removeChangeListener(cl);
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizardDescriptor = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        this.wizardDescriptor = null;
    }

    public Result getExecutionResult() {
        return executionResult;
    }

}

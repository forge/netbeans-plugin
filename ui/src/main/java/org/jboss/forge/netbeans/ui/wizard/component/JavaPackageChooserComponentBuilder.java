/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.spi.java.project.support.ui.PackageView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 110, service = ComponentBuilder.class)
public class JavaPackageChooserComponentBuilder extends ComponentBuilder<JComboBox> {

    @Override
    public JComboBox build(Container container, final InputComponent<?, Object> input, final CommandController controller, final ChangeSupport changeSupport) {
        Resource<File> resource = (Resource<File>) controller.getContext().getInitialSelection().get();
        FileObject dir = FileUtil.toFileObject(resource.getUnderlyingResourceObject());
        Project project;
        try {
            project = ProjectManager.getDefault().findProject(dir);
            ComboBoxModel model;
            if (project != null) {
                Sources sources = ProjectUtils.getSources(project);
                // TODO: Add another combo to select source groups?
                SourceGroup[] groups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
                model = PackageView.createListView(groups[0]);
            } else {
                model = new DefaultComboBoxModel();
            }
            model.setSelectedItem(input.getValue());
            final JComboBox combo = new JComboBox(model);
            if (input instanceof UIInput) {
                combo.setEditable(true);
                final JTextComponent tc = (JTextComponent) combo.getEditor().getEditorComponent();
                tc.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        controller.setValueFor(input.getName(), tc.getText());
                        changeSupport.fireChange();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        controller.setValueFor(input.getName(), tc.getText());
                        changeSupport.fireChange();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        controller.setValueFor(input.getName(), tc.getText());
                        changeSupport.fireChange();
                    }
                });
            }
            combo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // To prevent nullifying input's value when model is cleared
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        controller.setValueFor(input.getName(), combo.getSelectedItem());
                        changeSupport.fireChange();
                    }
                }
            });
            combo.setRenderer(PackageView.listRenderer());
            container.add(new JLabel(InputComponents.getLabelFor(input, true)));
            container.add(combo);
            return combo;
        } catch (IOException | IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Class<String> getProducedType() {
        return String.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.JAVA_PACKAGE_PICKER;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UIInput.class, UISelectOne.class};
    }

}

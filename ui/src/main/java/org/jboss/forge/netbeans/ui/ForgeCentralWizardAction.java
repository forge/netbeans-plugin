/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JComponent;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ProjectProvider;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.util.OperatingSystemUtils;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

@ActionID(category = "Build", id = "org.jboss.forge.netbeans.ui.ForgeCentralWizardAction")
@ActionRegistration(displayName = "#OpenForgeWizard")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 33333),
    // Ctrl-B on normal systems and Command-B on Mac
    @ActionReference(path = "Shortcuts", name = "D-B")
})
@NbBundle.Messages({
    "OpenForgeWizard=JBoss &Forge Wizard"
})
public final class ForgeCentralWizardAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<>();
        panels.add(new ForgeCentralWizardPanel1());
        panels.add(new ForgeCentralWizardPanel2());
        panels.add(new ForgeCentralWizardPanel3());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle("...dialog title...");
        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
            try {
                File f = File.createTempFile("forge", ".dir");
                f.delete();
                f.mkdirs();
                FileObject dir = FileUtil.toFileObject(f);
                FileObject pom = dir.createData("pom.xml");
                Project p = ProjectManager.getDefault().findProject(dir);
                OpenProjects.getDefault().open(new Project[]{p}, false, true);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}

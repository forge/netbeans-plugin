/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.listener;

import java.io.File;
import java.io.IOException;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectListener;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 * Called when the plugin starts and auto-registers on the ProjectFactory
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbProjectListener implements ProjectListener {

    @Override
    public void projectCreated(Project project) {
        File root = (File) project.getRoot().getUnderlyingResourceObject();
        FileObject dir = FileUtil.toFileObject(root);
        try {
            org.netbeans.api.project.Project p = ProjectManager.getDefault().findProject(dir);
            OpenProjects.getDefault().open(new org.netbeans.api.project.Project[]{p}, true, true);
        } catch (IOException | IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
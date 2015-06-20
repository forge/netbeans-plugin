/*
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JBoss by Red Hat - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.listener;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import org.jboss.forge.addon.projects.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 * Queues projects and allows to import at a later stage
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum ProjectImporter {

    INSTANCE;

    private final Queue<Project> projects = new LinkedList<>();

    public void addProject(Project project) {
        projects.add(project);
    }

    public void importProjects() {
        while (projects.size() > 0) {
            doImport(projects.poll());
        }
    }

    private void doImport(Project project) {
        File root = (File) project.getRoot().getUnderlyingResourceObject();
        FileObject dir = FileUtil.toFileObject(root);
        try {
            org.netbeans.api.project.Project p = ProjectManager.getDefault().findProject(dir);
            OpenProjects.getDefault().open(new org.netbeans.api.project.Project[]{p}, true, true);
        } catch (IOException | IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void clear() {
        projects.clear();
    }
}

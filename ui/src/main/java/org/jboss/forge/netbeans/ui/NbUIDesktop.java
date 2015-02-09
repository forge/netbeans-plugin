/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.swing.SwingUtilities;
import org.jboss.forge.addon.ui.DefaultUIDesktop;
import org.jboss.forge.addon.ui.UIDesktop;
import org.openide.awt.HtmlBrowser;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

/**
 * {@link UIDesktop} Implementation
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIDesktop extends DefaultUIDesktop {

    @Override
    public void open(File file) throws IOException {
        edit(file);
    }

    @Override
    public void edit(final File file) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    DataObject.find(FileUtil.toFileObject(file)).
                            getLookup().lookup(OpenCookie.class).open();
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    @Override
    public void browse(URI uri) throws IOException {
        HtmlBrowser.URLDisplayer.getDefault().showURL(uri.toURL());
    }
}

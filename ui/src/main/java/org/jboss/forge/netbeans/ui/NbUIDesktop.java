/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

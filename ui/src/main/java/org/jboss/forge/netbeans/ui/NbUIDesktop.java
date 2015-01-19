/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.jboss.forge.addon.ui.DefaultUIDesktop;
import org.jboss.forge.addon.ui.UIDesktop;
import org.openide.awt.HtmlBrowser;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

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
    public void edit(File file) throws IOException {
        DataObject.find(FileUtil.toFileObject(file)).
                getLookup().lookup(OpenCookie.class).open();
    }

    @Override
    public void browse(URI uri) throws IOException {
        HtmlBrowser.URLDisplayer.getDefault().showURL(uri.toURL());
    }
}

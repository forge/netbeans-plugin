/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.runtime;

import org.openide.modules.ModuleInstall;
import org.openide.util.RequestProcessor;

/**
 * Performs the necessary steps to clean up Furnace
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Installer extends ModuleInstall {

    private static final RequestProcessor RP = new RequestProcessor(Installer.class);

    @Override
    public void restored() {
        RP.post(() -> {
            FurnaceService.INSTANCE.start();
        });
    }

    /**
     * Stops the running Furnace instance
     *
     * @return always true
     */
    @Override
    public boolean closing() {
        RP.shutdown();
        FurnaceService.INSTANCE.stop();
        return true;
    }

}

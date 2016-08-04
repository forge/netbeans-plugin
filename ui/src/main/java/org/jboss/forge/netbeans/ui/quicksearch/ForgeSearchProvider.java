/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.quicksearch;

import java.util.Set;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.jboss.forge.netbeans.ui.context.NbUIContext;
import org.jboss.forge.netbeans.ui.wizard.RunForgeWizardRunnable;
import org.netbeans.spi.quicksearch.SearchProvider;
import org.netbeans.spi.quicksearch.SearchRequest;
import org.netbeans.spi.quicksearch.SearchResponse;

public class ForgeSearchProvider implements SearchProvider {

    @Override
    public void evaluate(final SearchRequest request, final SearchResponse response) {
        CommandFactory commandFactory = FurnaceService.INSTANCE.getCommandFactory();
        if (commandFactory == null) {
            // Furnace hasn't been fully initialized yet, try again later
            return;
        }
        try (NbUIContext context = new NbUIContext()) {
            Set<String> commandNames = commandFactory.getEnabledCommandNames(context);
            String text = request.getText();
            if (commandNames.contains(text)) {
                if (response.addResult(new RunForgeWizardRunnable(text), text)) {
                    return;
                }
            }
            String query = text.toLowerCase();
            for (final String commandName : commandNames) {
                if (commandName.toLowerCase().contains(query)) {
                    if (!response.addResult(new RunForgeWizardRunnable(commandName), commandName)) {
                        break;
                    }
                }
            }
        }
    }

}

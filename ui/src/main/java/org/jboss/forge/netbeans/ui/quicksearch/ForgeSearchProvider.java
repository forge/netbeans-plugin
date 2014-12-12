/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.quicksearch;

import java.util.Set;
import javax.swing.SwingUtilities;
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CommandFactory commandFactory = FurnaceService.INSTANCE.getCommandFactory();
                try (NbUIContext context = new NbUIContext()) {
                    Set<String> commandNames = commandFactory.getEnabledCommandNames(context);
                    String query = request.getText().toLowerCase();
                    for (final String commandName : commandNames) {
                        if (commandName.toLowerCase().contains(query)) {
                            if (!response.addResult(new RunForgeWizardRunnable(commandName), commandName)) {
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

}

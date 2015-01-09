/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.output.UIMessage;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilder;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilderRegistry;
import org.jboss.forge.netbeans.ui.wizard.util.NotificationHelper;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

/**
 * A Wizard panel
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ForgeWizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor> {

    private final CommandController controller;
    private final JPanel panel;
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private final Map<InputComponent<?, ?>, JComponent> guiComponents = new HashMap<>();
    private WizardDescriptor descriptor;

    public ForgeWizardPanel(CommandController controller) {
        this.controller = controller;
        panel = new JPanel(new MigLayout("fillx,wrap 2", "[left]rel[grow,fill]"));
        panel.setName(controller.getMetadata().getDescription());
        initComponents();
    }

    public void setWizardDescriptor(WizardDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    private void initComponents() {
        Map<String, InputComponent<?, ?>> inputs = controller.getInputs();
        for (Map.Entry<String, InputComponent<?, ?>> entry : inputs.entrySet()) {
            final String key = entry.getKey();
            final InputComponent<?, Object> value = (InputComponent<?, Object>) entry.getValue();

            final ComponentBuilder builder = ComponentBuilderRegistry.INSTANCE.getBuilderFor(value);
            final JComponent jc = builder.build(panel, value, controller, changeSupport);

            jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);

            guiComponents.put(value, jc);

            // Update state after a change is detected
            addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    builder.updateState(jc, value);
                }
            });
        }
        // Show messages after every change
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                NotificationHelper.displayNotifications(descriptor, controller.validate());
            }
        });
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        return controller.isValid();
    }

    @Override
    public void validate() throws WizardValidationException {
        for (UIMessage message : controller.validate()) {
            if (message.getSeverity() == UIMessage.Severity.ERROR) {
                JComponent component = guiComponents.get(message.getSource());
                throw new WizardValidationException(component, message.getDescription(), null);
            }
        }
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        // use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        // use wiz.putProperty to remember current panel state
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard;

import java.awt.Component;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilder;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilderRegistry;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

/**
 * A Wizard panel
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ForgeWizardPanel extends JPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    private final CommandController controller;
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public ForgeWizardPanel(CommandController controller) {
        super(new MigLayout("fillx,wrap 2", "[left]rel[grow,fill]"));
        setSize(300,500);
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        Map<String, InputComponent<?, ?>> inputs = controller.getInputs();
        for (Map.Entry<String, InputComponent<?, ?>> entry : inputs.entrySet()) {
            String key = entry.getKey();
            InputComponent<?, Object> value = (InputComponent<?, Object>) entry.getValue();
            ComponentBuilder builder = ComponentBuilderRegistry.INSTANCE.getBuilderFor(value);
            JComponent jc = builder.build(this, value, controller);
            jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
        }
    }

    @Override
    public Component getComponent() {
        return this;
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
        // If it is always OK to press Next or Finish, then:
//        return buttonValid();
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
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

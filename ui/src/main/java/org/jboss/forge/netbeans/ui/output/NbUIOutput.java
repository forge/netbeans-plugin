/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.output;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.openide.util.Exceptions;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Writes to output.
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIOutput implements UIOutput {

    private final InputOutput io;
    private final PrintStream out;
    private final PrintStream err;

    public NbUIOutput() {
        this.io = IOProvider.getDefault().getIO("JBoss Forge", false);
        this.io.select();

        this.out = new PrintStream(new WriterOutputStream(this.io.getOut()), true);
        this.err = new PrintStream(new WriterOutputStream(this.io.getErr()), true);
    }

    @Override
    public PrintStream out() {
        return out;
    }

    @Override
    public PrintStream err() {
        return err;
    }

    @Override
    public void success(PrintStream out, String message) {
        try {
            IOColorLines.println(io, "***SUCCESS*** " + message, Color.GREEN);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void error(PrintStream out, String message) {
        try {
            IOColorLines.println(io, "***ERROR*** " + message, Color.RED);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void info(PrintStream out, String message) {
        try {
            IOColorLines.println(io, "***INFO*** " + message, Color.BLUE);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void warn(PrintStream out, String message) {
        try {
            IOColorLines.println(io, "***WARNING*** " + message, Color.ORANGE);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
}

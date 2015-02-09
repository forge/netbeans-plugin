/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.output;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.openide.util.Exceptions;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Writes to output.
 *
 * Depends on commons-io due to WriterOutputStream class
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class NbUIOutput implements UIOutput {

    private final InputOutput io;
    private final PrintStream out;
    private final PrintStream err;

    public NbUIOutput() {
        this.io = IOProvider.getDefault().getIO("JBoss Forge", false);
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
            this.io.select();
            IOColorLines.println(io, "***SUCCESS*** " + message, Color.GREEN);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void error(PrintStream out, String message) {
        try {
            this.io.select();
            IOColorLines.println(io, "***ERROR*** " + message, Color.RED);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void info(PrintStream out, String message) {
        try {
            this.io.select();
            IOColorLines.println(io, "***INFO*** " + message, Color.BLUE);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void warn(PrintStream out, String message) {
        try {
            this.io.select();
            IOColorLines.println(io, "***WARNING*** " + message, Color.ORANGE);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
}

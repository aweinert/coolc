package net.alexweinert.coolc;

import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Commandline {
    @Parameter(names = "-backend", description = "The formalism to be unparsed to, one of [jar,java]")
    public String backend = "jar";

    @Parameter(description = "The files to be compiled")
    public List<String> inputFiles = new LinkedList<>();

    @Parameter(names = { "-h", "--help" }, description = "Display usage information", help = true)
    private boolean help = false;

    private final JCommander commander;

    public Commandline(JCommander commander) {
        this.commander = commander;
    }

    public boolean isValid() {
        return this.help || this.inputFiles.size() == 1;
    }

    public boolean showHelp() {
        return this.help;
    }

    public JCommander getParser() {
        return this.commander;
    }
}

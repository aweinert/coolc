package net.alexweinert.coolc;

import java.util.List;

import com.beust.jcommander.Parameter;

public class Commandline {
    @Parameter(names = "-backend", description = "The formalism to be unparsed to, one of [jar,java]")
    public String backend = "jar";

    @Parameter(description = "The files to be compiled")
    public List<String> inputFiles;

    public boolean isValid() {
        return this.inputFiles.size() == 1;
    }
}

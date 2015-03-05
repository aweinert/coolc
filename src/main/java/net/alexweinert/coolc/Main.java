package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.processors.coolfrontend.CoolFrontend;
import net.alexweinert.coolc.processors.coolfrontend.CoolUnparser;

public class Main {
    public static void main(String[] args) {
        final Backend<String> toolchain = new CoolFrontend().append(new CoolUnparser());
        toolchain.process(args[0]);
    }
}

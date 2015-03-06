package net.alexweinert.coolc.processors.stringdumper;

import net.alexweinert.coolc.infrastructure.Backend;

public class StringDumper implements Backend<String> {

    @Override
    public void process(String input) {
        System.out.println(input);
    }

}

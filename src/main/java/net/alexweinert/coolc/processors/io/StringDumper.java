package net.alexweinert.coolc.processors.io;

import net.alexweinert.pipelines.Backend;

public class StringDumper implements Backend<String> {

    @Override
    public void process(String input) {
        System.out.println(input);
    }

}

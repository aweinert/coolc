package net.alexweinert.coolc.processors.cool.frontend.parser;

import net.alexweinert.coolc.representations.cool.ast.Feature;
import net.alexweinert.coolc.representations.cool.ast.Features;

import java.util.LinkedList;
import java.util.List;

class FeaturesPrototype {
    private String filename;
    private List<Feature> elements = new LinkedList<>();

    FeaturesPrototype (String filename) {
        this.filename = filename;
    }

    /**
     * @param f The feature to add
     * @return This object in order to support daisy-chaining.
     */
    FeaturesPrototype add(Feature f) {
        this.elements.add(f);
        return this;
    }

    /**
     * @param defaultLineno The line number of the Features-node if no features have been given.
     * @return A newly constructed Features-node containing the Features added so far. If no Features have been added,
     * the return value has the given defaultLineno. Otherwise, the return value has the same line number as the first
     * Feature
     */
    Features build(int defaultLineno) {
        final int lineno = this.elements.isEmpty() ? defaultLineno : this.elements.get(0).getLineNumber();
        return new Features(this.filename, lineno, this.elements);
    }
}

package net.alexweinert.coolc;

public class Output {
    public void error(String errorMessage) {
        System.out.println("ERROR: " + errorMessage.replace("\n", "\n  "));
    }
}

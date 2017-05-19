import net.alexweinert.coolc.Main;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EndToEndTest {
    @Test
    public void helloWorld() {
        runTest("helloWorld", 1);
    }

    @Test
    public void assign1() {
        runTest("assign", 1);
    }

    private void runTestWithoutInput(String testName) {
        final String codePath = getCodePath(testName);
        final String jarPath = compile(codePath);

        try {
            runJarWithoutInput(jarPath);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertNull(e);
        }
    }

    private void runTest(String testName, int inputNumber) {
        final String codePath = getCodePath(testName);
        final String jarPath = compile(codePath);

        final String inputPath = getInputPath(testName, inputNumber);
        try {
            final String actualOutput = runJar(jarPath, inputPath);
            final String expectedOutputPath = getOutputPath(testName, inputNumber);
            final String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputPath)));

            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertNull(e);
        }
    }

    private void runJarWithoutInput(String jarPath) throws IOException, InterruptedException {
        final ProcessBuilder procBuilder = new ProcessBuilder("java", "-jar", jarPath);
        final Process proc = procBuilder.start();
        Assert.assertEquals(0, proc.waitFor());
    }

    private String runJar(String jarPath, String inputPath) throws IOException, InterruptedException {
        final String command = String.format("java -jar %s", jarPath);
        final ProcessBuilder procBuilder = new ProcessBuilder("java", "-jar", jarPath);
        procBuilder.redirectInput(new File(inputPath));

        final Process proc = procBuilder.start();
        /* Execute compiled program and check exit code
         * Cool programs always exit with code 0, so an exit code != 0 indicates a crash */
        Assert.assertEquals(0, proc.waitFor());
        // Method for turning InputStream into String taken from http://stackoverflow.com/a/5445161
        java.util.Scanner s = new java.util.Scanner(proc.getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String compile(String codePath) {
        final String[] args = new String[] { codePath };
        // Execute compiler and check for exit code
        Main.main(args);
        return "out.jar";
    }

    private String getInputPath(String testName, int inputNumber) {
        return String.format("src/test/resources/end2end/%s/%d.in", testName, inputNumber);
    }

    private String getOutputPath(String testName, int inputNumber) {
        return String.format("src/test/resources/end2end/%s/%d.out", testName, inputNumber);
    }

    private String getCodePath(String testName) {
        return String.format("src/test/resources/end2end/%s/code.cl", testName);
    }

}
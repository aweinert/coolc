import net.alexweinert.coolc.Main;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EndToEndTest {
    @Test
    public void helloWorld1() {
        runTest("helloWorld", 1);
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
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertNull(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.assertNull(e);
        }
    }

    private String runJar(String jarPath, String inputPath) throws IOException, InterruptedException {
        final String command = String.format("java -jar %s", jarPath);
        final ProcessBuilder procBuilder = new ProcessBuilder("java", "-jar", jarPath);
        procBuilder.redirectInput(new File(inputPath));

        final Process proc = procBuilder.start();
        proc.waitFor();
        // Method for turning InputStream into String taken from http://stackoverflow.com/a/5445161
        java.util.Scanner s = new java.util.Scanner(proc.getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String compile(String codePath) {
        final String[] args = new String[] { codePath };
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
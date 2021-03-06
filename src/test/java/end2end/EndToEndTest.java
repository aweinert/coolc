package end2end;

import net.alexweinert.coolc.Main;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EndToEndTest {
    @Test
    public void helloWorld() {
        runTest("helloworld", 1);
    }

    @Test
    public void assign1() {
        runTest("assign", 1);
    }

    @Test
    public void cells() {
        runTest("cells", 1);
    }

    @Test
    public void factorial() {
        runTest("factorial", 1);
    }

    @Test
    public void list() {
        runTest("list", 1);
    }

    @Test
    public void loop() {
        runTest("loop", 1);
    }

    @Test
    public void stack1() {
        runTest("stack", 1);
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
        final ProcessBuilder procBuilder = new ProcessBuilder("java", "-jar", jarPath);
        final Process proc = procBuilder.start();

        final File inputFile = new File(inputPath);
        final BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
        String currentLine = inputReader.readLine();
        while(currentLine != null) {
            // readLine removes the final newline. The cool-program may, however, require it.
            currentLine += "\n";
            proc.getOutputStream().write(currentLine.getBytes());
            proc.getOutputStream().flush();
            // Sleep in order to avoid timing issue causing Issue #4
            Thread.sleep(500);
            currentLine = inputReader.readLine();
        }
        inputReader.close();
        proc.getOutputStream().close();

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
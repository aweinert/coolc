package net.alexweinert.coolc;

import net.alexweinert.coolc.processors.ProcessorBuilder;
import net.alexweinert.pipelines.ProcessorException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class APIMain {
    public static void main(String[] args) {
        SpringApplication.run(APIMain.class, args);
    }

    @ControllerAdvice
    public static class CustomExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
            return new ResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private ResourceLoader loader;

    @PostMapping(value = "/compile", produces = "application/java-archive")
    @ResponseBody
    public ResponseEntity<Resource> compileFile(@RequestParam(name = "file") MultipartFile file) throws IOException, ProcessorException {
            final File outputFile = compileFileInternal(file);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"compiled.jar\"")
                    .body(loader.getResource("file:" + outputFile.getAbsolutePath()));
    }

    private File compileFileInternal(MultipartFile file) throws IOException, ProcessorException {
        final File tempFile = File.createTempFile("tocompile",".cool");
        final File outputFile = File.createTempFile("compiled", ".jar");
        new ProcessorBuilder.FrontendBuilder()
                .inputStreamToReader(file.getInputStream())
                .fileToCool(file.getOriginalFilename())
                .checkCool()
                .coolToBytecode().bytecodeToJbc().jbcToFiles().filesToJar(outputFile.getName(), "CoolMain")
                .fileToHarddrive(outputFile.getParent())
                .compile();

        return outputFile;
    }

    @PostMapping("/execute")
    public String runProgram(@RequestParam(name = "file") MultipartFile file, @RequestParam(name = "input", defaultValue = "") final String input) throws IOException, ProcessorException {
        final File compiledJar = compileFileInternal(file);

        final String[] processOutput = new String[1];
        final Thread runnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Process p = new ProcessBuilder("java", "-jar", compiledJar.getAbsolutePath()).start();
                    p.getOutputStream().write(input.getBytes(StandardCharsets.UTF_8));
                    p.getOutputStream().close();
                    final StringBuilder stringBuilder = new StringBuilder();
                    for(int ch; (ch = p.getInputStream().read()) != -1;) {
                        stringBuilder.append((char)ch);
                    }
                    p.waitFor();
                    processOutput[0] = stringBuilder.toString();
                } catch (IOException | InterruptedException e) {
                }
            }
        });
        runnerThread.start();
        try {
            runnerThread.join(10000);
        } catch (InterruptedException e) {
            return ":(";
        }

        return "<html><body>" + processOutput[0] + "</body></html>";

    }
}

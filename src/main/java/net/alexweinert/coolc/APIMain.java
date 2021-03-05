package net.alexweinert.coolc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
@RestController
public class APIMain {
    public static void main(String[] args) {
        SpringApplication.run(APIMain.class, args);
    }

    @Autowired
    private ResourceLoader loader;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<Resource> compileFile(@RequestParam(name = "file") MultipartFile file) throws IOException {
        final File tempFile = File.createTempFile("tocompile",".cool");
        try (FileOutputStream output = new FileOutputStream(tempFile)) {
            output.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File outputFile = File.createTempFile("compiled", ".jar");
        Main.main(new String[] { tempFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath() });

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"compiled.jar\"")
                .body(loader.getResource("file:" + outputFile.getAbsolutePath()));
    }
}

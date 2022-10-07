package org.dmitrysulman.innopolis.diplomaproject.repositiries;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class FileSystemRepository {
    private final String PRODUCTS_IMAGES_DIR = "products_images";

    public String save(byte[] content, String imageName) throws IOException {
        Path newFile = Paths.get(PRODUCTS_IMAGES_DIR, new Date().getTime() + "-" + imageName);
        Files.createDirectories(newFile.getParent());
        Files.write(newFile, content);

        return newFile.toAbsolutePath().toString();
    }
}

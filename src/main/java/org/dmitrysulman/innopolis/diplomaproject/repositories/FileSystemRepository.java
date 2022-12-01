package org.dmitrysulman.innopolis.diplomaproject.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Repository
public class FileSystemRepository {
    @Value("${application.products_images_dir}")
    private String PRODUCTS_IMAGES_DIR;

    public String save(byte[] content, String imageName) throws IOException {
        Path newFile = Paths.get(PRODUCTS_IMAGES_DIR, Instant.now().toEpochMilli() + "-" + imageName);
        Files.createDirectories(newFile.getParent());
        Files.write(newFile, content);

        return Paths.get(PRODUCTS_IMAGES_DIR).relativize(newFile).toString();
    }

    public FileSystemResource find(String location) {
        return new FileSystemResource(Paths.get(PRODUCTS_IMAGES_DIR, location));
    }
}

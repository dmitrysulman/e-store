package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ProductImage;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

public interface ImageService {
    ProductImage save(byte[] content, Product product, String extension) throws IOException;

    FileSystemResource get(int productId, int index);
}

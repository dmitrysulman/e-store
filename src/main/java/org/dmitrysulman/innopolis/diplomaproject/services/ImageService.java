package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ProductImage;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ImageService {
    ProductImage save(byte[] content, Product product, String extension) throws IOException;

    Resource get(int productId, int index);
}

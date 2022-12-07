package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ProductImage;
import org.dmitrysulman.innopolis.diplomaproject.repositories.FileSystemRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@ConditionalOnProperty(name="application.products_images.bean", havingValue = "filesystem")
public class FileSystemImageService implements ImageService {
    private final FileSystemRepository fileSystemRepository;
    private final ProductImageRepository productImageRepository;

    @Autowired
    public FileSystemImageService(FileSystemRepository fileSystemRepository, ProductImageRepository productImageRepository) {
        this.fileSystemRepository = fileSystemRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    @Transactional
    public ProductImage save(byte[] content, Product product, String extension) throws IOException {
        String location = fileSystemRepository.save(content,
                product.getName().toLowerCase().replace(' ', '-').concat(".").concat(extension));
        int order = (int) productImageRepository.countByProductId(product.getId());
        ProductImage productImage = new ProductImage();
        productImage.setImageLocation(location);
        productImage.setProduct(product);
        productImage.setImageOrder(order);

        return productImageRepository.save(productImage);
    }

    @Override
    public Resource get(int productId, int index) {
        List<ProductImage> productImages = productImageRepository.findByProductIdOrderByImageOrder(productId);
        if (productImages.isEmpty() || productImages.size() < index + 1) {
            return null;
        }
        String location = productImages.get(index).getImageLocation();

        return fileSystemRepository.find(location);
    }
}

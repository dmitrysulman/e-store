package org.dmitrysulman.innopolis.diplomaproject.services;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ProductImage;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

@Service
public class AwsS3ImageServiceImpl implements ImageService {
    private final ResourceLoader resourceLoader;
    private final ProductImageRepository productImageRepository;
    private final AmazonS3Client amazonS3Client;

    @Autowired
    public AwsS3ImageServiceImpl(ResourceLoader resourceLoader, ProductImageRepository productImageRepository, AmazonS3Client amazonS3Client) {
        this.resourceLoader = resourceLoader;
        this.productImageRepository = productImageRepository;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public ProductImage save(byte[] content, Product product, String extension) throws IOException {
        String fileName = Instant.now().toEpochMilli() + "-" + product.getName().toLowerCase()
                .replace(' ', '-')
                .concat(".")
                .concat(extension);
        Resource resource = resourceLoader.getResource("s3://elasticbeanstalk-us-east-1-447528752820/images/" +
                fileName);
        WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            outputStream.write(content);
        }
        amazonS3Client.setObjectAcl("elasticbeanstalk-us-east-1-447528752820",
                "/images/" + fileName,
                CannedAccessControlList.PublicRead);

        ProductImage productImage = new ProductImage();
        productImage.setImageLocation(fileName);
        productImage.setProduct(product);

        return productImageRepository.save(productImage);
    }

    @Override
    public Resource get(int productId, int index) {
        return null;
    }
}

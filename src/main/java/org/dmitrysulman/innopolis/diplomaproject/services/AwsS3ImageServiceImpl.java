package org.dmitrysulman.innopolis.diplomaproject.services;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ProductImage;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;

@Service
@ConditionalOnProperty(name="application.products_images.bean", havingValue = "awss3")
public class AwsS3ImageServiceImpl implements ImageService {
    private final ProductImageRepository productImageRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${application.products_images.aws_bucket}")
    private String bucket;

    @Value("${application.products_images.aws_dir}")
    private String dir;

    @Autowired
    public AwsS3ImageServiceImpl(ProductImageRepository productImageRepository,
                                 AmazonS3Client amazonS3Client) {
        this.productImageRepository = productImageRepository;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public ProductImage save(byte[] content, Product product, String extension) throws IOException {
        String fileName = dir + "/" + Instant.now().toEpochMilli() + "-" + product.getName().toLowerCase()
                .replace(' ', '-')
                .concat(".")
                .concat(extension);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg");
        amazonS3Client.putObject(bucket, fileName, new ByteArrayInputStream(content), metadata);
        amazonS3Client.setObjectAcl(bucket, fileName, CannedAccessControlList.PublicRead);

        ProductImage productImage = new ProductImage();
        int order = (int) productImageRepository.countByProductId(product.getId());
        productImage.setImageLocation(fileName);
        productImage.setProduct(product);
        productImage.setImageOrder(order);

        return productImageRepository.save(productImage);
    }

    @Override
    public Resource get(int productId, int index) {
        return null;
    }
}

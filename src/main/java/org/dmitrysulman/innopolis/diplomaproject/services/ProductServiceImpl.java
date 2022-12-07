package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.ProductImage;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductRepository;
import org.dmitrysulman.innopolis.diplomaproject.util.PageableHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    @Value("${application.products_images.url_prefix}")
    private String IMAGE_URL_PREFIX;

    private final ProductRepository productRepository;
    private final PageableHelper pageableHelper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              PageableHelper pageableHelper) {
        this.productRepository = productRepository;
        this.pageableHelper = pageableHelper;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Integer page, Integer perPage, String direction) {
        return productRepository.findAll(pageableHelper.preparePageable(page, perPage, direction, "price"));
    }

    @Override
    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByIdWithImagesUrls(int id) {
        Optional<Product> product = productRepository.findByIdWithProductImages(id);
        product.ifPresent(prd -> {
            List<ProductImage> productImages = prd.getProductImages();
            List<String> imagesUrls = new ArrayList<>();
            for (ProductImage productImage : productImages) {
//                imagesUrls.add(IMAGE_URL_PREFIX + prd.getId() + "/" + i);
                imagesUrls.add(IMAGE_URL_PREFIX + productImage.getImageLocation());
            }
            prd.setImagesUrls(imagesUrls);
        });

        return product;
    }

    @Override
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Page<Product> findByNameContaining(String name, Integer page, Integer perPage, String direction) {
        return productRepository.findByNameContainingIgnoreCase(name,
                pageableHelper.preparePageable(page, perPage, direction, "price"));
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }


}
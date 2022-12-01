package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductImageRepository;
import org.dmitrysulman.innopolis.diplomaproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Integer page, Integer perPage, String direction) {
        return productRepository.findAll(preparePageable(page, perPage, direction));
    }

    @Override
    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByIdWithImagesUrls(int id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(prd -> {
            int productImagesCount = (int) productImageRepository.countByProductId(id);
            List<String> imagesUrls = new ArrayList<>();
            for (int i = 0; i < productImagesCount; i++) {
                imagesUrls.add("/products_images/" + prd.getId() + "/" + i);
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
        return productRepository.findByNameContainingIgnoreCase(name, preparePageable(page, perPage, direction));
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    private Pageable preparePageable(Integer page, Integer perPage, String direction) {
        if (page == null) {
            page = 0;
        }
        if (perPage == null) {
            perPage = 10;
        }
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (direction != null && direction.equals("DESC")) {
            sortDirection = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(new Sort.Order(sortDirection, "price"));

        return PageRequest.of(page, perPage, sort);
    }
}
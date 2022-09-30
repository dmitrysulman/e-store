package org.dmitrysulman.innopolis.diplomaproject.services;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.repositiries.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
    public Page<Product> findByNameContaining(String name, Integer page, Integer perPage, String direction) {
        return productRepository.findByNameContainingIgnoreCase(name, preparePageable(page, perPage, direction));
    }

    private Pageable preparePageable(Integer page, Integer perPage, String direction) {
        if (page == null) {
            page = 0;
        }
        if (perPage == null) {
            perPage = 10;
        }
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (direction != null && direction.equals("desc")) {
            sortDirection = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(new Sort.Order(sortDirection, "price"));

        return PageRequest.of(page, perPage, sort);
    }
}
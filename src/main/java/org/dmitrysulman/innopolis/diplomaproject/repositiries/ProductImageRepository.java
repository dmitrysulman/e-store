package org.dmitrysulman.innopolis.diplomaproject.repositiries;

import org.dmitrysulman.innopolis.diplomaproject.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductIdOrderByImageOrder(int productId);
}

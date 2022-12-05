package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/products_images")
public class ProductImageController {
    private final ImageService imageService;

    @Autowired
    public ProductImageController(@Qualifier("awsS3ImageServiceImpl") ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{productId}/{imageIndex}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Resource getImage(@PathVariable("productId") int productId, @PathVariable("imageIndex") int imageIndex) {
        Resource image = imageService.get(productId, imageIndex);
        if (image == null || !image.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return image;
    }
}

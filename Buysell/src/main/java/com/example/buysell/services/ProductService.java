package com.example.buysell.services;

import com.example.buysell.models.Category;
import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.CategoryRepository;
import com.example.buysell.repositories.ProductRepository;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> listProducts(String title) {
        if (title != null & title != "") return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public List<Product> sortProductsByPrice (int price1, int price2) {
        List<Product> products = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            if (product.getPrice() > price1 & product.getPrice() < price2) {
                products.add(product);
            }
        }
        return products;
    }

    public void saveProduct(Principal principal, Product product, Set<Category> categories, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        List<Category> all_categories = categoryRepository.findAll();
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        for (Category category : categories) {
            if (!all_categories.contains(category)){
                categoryRepository.save(category);
            }
        }
        product.setCategories(categories);
        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public Boolean deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                for (Category category : product.getCategories()) {
                    if (category.removeProductFromCategory(product)) {
                        categoryRepository.save(category);
                    } else {
                        log.info("Category with id = {} and Product with id = {} contains an error", category.getId(), product.getId());
                        return false;
                    }
                }
                user.getProducts().remove(product);
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
                return false;
            }
        } else {
            log.error("Product with id = {} is not found", id);
            return false;
        }
        return true;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}

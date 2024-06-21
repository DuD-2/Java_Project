package com.example.buysell.services;

import com.example.buysell.models.Category;
import com.example.buysell.models.Product;
import com.example.buysell.repositories.CategoryRepository;
import com.example.buysell.repositories.ProductRepository;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.TypeCategoryTypePattern;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final ProductRepository productRepository;
    private  final CategoryRepository categoryRepository;

    public List<Category> findByName(String name) {return categoryRepository.findByName(name);}

    public Set<String> listCategoriesNames() {
        Set<String> categories_names = new HashSet<>();
        for (Category category : categoryRepository.findAll()) {
            categories_names.add(category.getName());
        }
        return categories_names;
    }

    public List<Category> listCategories(String name) {
        if (name != null & name != "") return categoryRepository.findByName(name);
        return categoryRepository.findAll();
    }

    public void saveCategory(String name, Set<Product> products) {
        Category category = new Category(name);
        category.setProducts(products);
        categoryRepository.save(category);
        log.info("Saving new Category. Name: {}", category.getName());
    }

    public void saveCategory(String name_category) {
        Category category = new Category(name_category);
        categoryRepository.save(category);
        log.info("Saving new empty Category. Name: {}", category.getName());
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
        log.info("Saving new full Category. Name: {}", category.getName());
    }

    public Boolean deleteCategory(Category category) {
        for (Product product : category.getProducts()) {
            if (product.removeCategoryFromProduct(category)) {
                productRepository.save(product);
            } else {
                log.info("Category with id = {} and Product with id = {} contains an error", category.getId(), product.getId());
                return false;
            }
        }
        log.info("Category with id = {} was deleted", category.getId());
        categoryRepository.delete(category);
        return true;
    }
}

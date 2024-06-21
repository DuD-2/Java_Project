package com.example.buysell.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private String city;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
    mappedBy = "product")
    private List<Image> images = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    @ManyToMany(mappedBy = "products")
    private Set<Category> categories = new HashSet<>();
    private Long previewImageId;
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void onCreate() { dateOfCreated = LocalDateTime.now(); }


    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(image);
    }

    public void addCategoryToProduct(Category category) {
        category.getProducts().add(this);
        this.getCategories().add(category);
    }

    public void nullCategoryOnProduct() {
        for (Category category : this.categories) {
            category.removeProductFromCategory(this);
        }
        this.categories = new HashSet<>();
    }

    public void setCategories(Set<Category> categories) {
        nullCategoryOnProduct();
        for (Category category : categories) {
            category.addProductToCategory(this);
        }
        this.categories = categories;
    }

    public Boolean removeCategoryFromProduct(Category category) {
        if (this.categories.contains(category) & category.getProducts().contains(this)) {
            category.getProducts().remove(this);
            this.categories.remove(category);
            return true;
        } else {
            return false;
        }
    }
}

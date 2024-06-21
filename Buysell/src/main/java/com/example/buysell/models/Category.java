package com.example.buysell.models;

import lombok.*;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Categories_Products",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void onCreate() { dateOfCreated = LocalDateTime.now(); }

    public Category(String name) {this.name = name;}

    public void nullProductOnCategory(){
        for (Product product : this.products) {
            product.removeCategoryFromProduct(this);
        }
        this.products = new HashSet<>();
    }

    public void addProductToCategory(Product product) {
        product.getCategories().add(this);
        this.getProducts().add(product);
    }

    public void setProducts(Set<Product> products) {
        this.nullProductOnCategory();
        for (Product product : products) {
            product.addCategoryToProduct(this);
        }
        this.products = products;
    }

    public Boolean removeProductFromCategory(Product product) {
        if (this.products.contains(product) & product.getCategories().contains(this)) {
            product.getCategories().remove(this);
            this.products.remove(product);
            return true;
        } else {
            return false;
        }
    }
}
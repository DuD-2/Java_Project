package com.example.buysell.controllers;

import com.example.buysell.models.Category;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.CategoryService;
import com.example.buysell.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/sortproduct")
    public String sortProducts(@RequestParam(name = "searchWord", required = false) String title, @RequestParam(name = "price1", required = false) String price1, @RequestParam(name = "price2", required = false) String price2, Principal principal, Model model){
        if (price1 == "") {
            if (price2 == "") {
                model.addAttribute("products", productService.listProducts(title));
            }
            else {
                model.addAttribute("products", productService.sortProductsByPrice(0, Integer.parseInt(price2)));
            }
        } else {
            if (price2 == "") {
                model.addAttribute("products", productService.sortProductsByPrice(Integer.parseInt(price1), Integer.MAX_VALUE));
            }
            model.addAttribute("products", productService.sortProductsByPrice(Integer.parseInt(price1), Integer.parseInt(price2)));
        }
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "products";
}

    @GetMapping("/")
    public String products(@RequestParam(name = "searchWord", required = false) String title, Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, @RequestParam("string_categories") String string_categories, Product product, Principal principal) throws IOException {
        Set<Category> categories = new HashSet<>();
        for (String category_name : string_categories.split(" ")) {
            if (categoryService.listCategoriesNames().contains(category_name)) {
                categories.add(categoryService.findByName(category_name).get(0));
            } else {
                Category category = new Category(category_name);
                categoryService.saveCategory(category);
                categories.add(category);
            }
        }
        productService.saveProduct(principal, product, categories, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(productService.getUserByPrincipal(principal), id);
        return "redirect:/my/products";
    }

    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }
}

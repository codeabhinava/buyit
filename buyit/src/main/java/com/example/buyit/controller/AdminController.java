package com.example.buyit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.buyit.model.AdminRegistration;
import com.example.buyit.model.AppUser;
import com.example.buyit.model.Product;
import com.example.buyit.repository.AddressRepository;
import com.example.buyit.repository.AdminRepository;
import com.example.buyit.repository.AppUserRepository;
import com.example.buyit.repository.ConfirmationTokenRepository;
import com.example.buyit.repository.OrderedItemsRepository;
import com.example.buyit.repository.ProductRepository;
import com.example.buyit.service.AdminService;
import com.example.buyit.service.ProductService;

import jakarta.websocket.server.PathParam;

@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private AdminRepository adminRepo;

    private final AdminService adminService;
    private final AppUserRepository appUserRepo;
    private final ProductRepository productRepo;
    private final OrderedItemsRepository orderedItemsRepo;
    private final AddressRepository addressRepo;
    private final ConfirmationTokenRepository confirmationTokenRepo;

    public AdminController(AdminService adminService, AppUserRepository appUserRepo, ProductRepository productRepo, OrderedItemsRepository orderedItemsRepo, AddressRepository addressRepo, ConfirmationTokenRepository confirmationTokenRepo) {
        this.adminService = adminService;
        this.appUserRepo = appUserRepo;
        this.productRepo = productRepo;
        this.orderedItemsRepo = orderedItemsRepo;
        this.addressRepo = addressRepo;
        this.confirmationTokenRepo = confirmationTokenRepo;
    }

    @PostMapping("/addadmin")
    public String addAdmin(@RequestBody AdminRegistration adminReg) {
        adminService.add(adminReg);
        return "New Admin";
    }

    @PostMapping("/newproduct")
    public String addNewProduct() {
        return "New product added";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admindashboard";
    }

    @GetMapping("/manageusers")
    public String controlUsers(Model model) {
        List<AppUser> users = appUserRepo.findallusers();
        model.addAttribute("users", users);
        return "usersmanagement";
    }

    @GetMapping("/user/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", appUserRepo.findById(id).orElseThrow());
        model.addAttribute("address", addressRepo.findByAppUser(appUserRepo.findById(id).orElseThrow()).orElseThrow());
        model.addAttribute("createdat", confirmationTokenRepo.findByAppUserId(id).orElseThrow());
        return "userdetails";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {

        model.addAttribute("user", appUserRepo.findById(id).orElseThrow());
        return "usereditpage";
    }

    @PostMapping("/user/toggle/{id}")
    public String toogleUser(@PathVariable Long id) {
        AppUser olduser = appUserRepo.findById(id).orElseThrow();
        if (olduser.isIsenabled()) {
            appUserRepo.disableAppUser(olduser.getEmail());
        } else {
            appUserRepo.enableAppUser(olduser.getEmail());
        }

        return "redirect:/admin/manageusers";
    }

    @GetMapping("/products")
    public String adminManageProducts(Model model) {
        List<Product> products = productRepo.findAllByOrderByProdIdAsc();
        model.addAttribute("products", products);
        return "manageproducts";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return "addnewproducts";
    }

    @PostMapping("/products/add/save")
    public String newProduct(@ModelAttribute Product product) {
        Product newProduct = new Product(product.getCategory(), product.getImageLink(), product.getName(), product.getPrice(), product.getQuantity());
        productRepo.save(newProduct);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id).orElseThrow();
        model.addAttribute("product", product);
        return "editproduct";
    }

    @PostMapping("/product/hide/{id}")
    public String hideProducts(@PathVariable Long id) {
        productRepo.hideProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/product/show/{id}")
    public String showProducts(@PathVariable Long id) {
        productRepo.showProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/update")
    public String updateProduct(@ModelAttribute Product product) {
        Product existingpProduct = productRepo.findById(product.getProdId()).orElseThrow();
        existingpProduct.setImageLink(product.getImageLink());
        existingpProduct.setName(product.getName());
        existingpProduct.setQuantity(product.getQuantity());
        existingpProduct.setPrice(product.getPrice());
        productRepo.save(existingpProduct);
        return "redirect:/admin/products";
    }

    @GetMapping("/topsellers")
    public String topSelling(Model model) {
        model.addAttribute("products", adminService.topSeller());
        model.addAttribute("categories", productRepo.findDistinctCategories());
        return "topsellers";
    }

    @GetMapping("/topsellers/{category}")
    public String topSellingByCategory(@PathVariable String category, Model model) {
        model.addAttribute("products", adminService.topSellerCategory(category));

        model.addAttribute("categories", productRepo.findDistinctCategories());
        return "topsellers";
    }

    @GetMapping("/bottomsellers")
    public String leastSelling(Model model) {
        model.addAttribute("products", adminService.bottomSeller());
        model.addAttribute("categories", productRepo.findDistinctCategories());

        return "bottomsellers";
    }

    @GetMapping("/bottomsellers/{category}")
    public String leastSellingByCategory(@PathVariable String category, Model model) {
        model.addAttribute("products", adminService.bottomSellerCategory(category));
        model.addAttribute("categories", productRepo.findDistinctCategories());
        return "bottomsellers";

    }

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderedItemsRepo.findAll());
        return "order";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/product/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getByCategory(category);
    }
}

package com.example.buyit.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.buyit.model.Address;
import com.example.buyit.model.AppReg;
import com.example.buyit.model.AppUser;
import com.example.buyit.model.Cart;
import com.example.buyit.model.OrderedItems;
import com.example.buyit.model.Orders;
import com.example.buyit.model.Product;
import com.example.buyit.repository.AddressRepository;
import com.example.buyit.repository.AppUserRepository;
import com.example.buyit.repository.CartRepository;
import com.example.buyit.repository.OrderRepository;
import com.example.buyit.repository.OrderedItemsRepository;
import com.example.buyit.repository.ProductRepository;
import com.example.buyit.service.CartService;
import com.example.buyit.service.RegistrationService;

@CrossOrigin
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderedItemsRepository orderedItemsRepository;

    @Autowired
    AddressRepository addressRepository;

    @PostMapping("/api/register")
    public String registerUser(@RequestBody AppReg appreg) {
        return registrationService.register(appreg);
    }

    @GetMapping("/api/register/confirm")
    public String confirmUser(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @DeleteMapping("/api/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        return registrationService.deleteUser(id);
    }

    @PutMapping("/api/update/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody AppReg appreg) {
        return registrationService.updateUser(id, appreg);
    }

    @GetMapping("/api/all")
    public List<AppUser> allUsers() {
        return registrationService.getAllUsers();
    }

    @GetMapping("/api/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return registrationService.getById(id);
    }

    @GetMapping("/api/token/{token}")
    public AppUser getUserByToken(@PathVariable String token) {
        return registrationService.getByToken(token);
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cartCount", cartRepository.getTotalCartCount(username));
        return "homepage";
    }

    @PostMapping("/home")
    public String addToCart(@RequestParam("prod_id") Long prodId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        cartService.addProductToCart(username, prodId);

        return "redirect:/users/home";

    }

    @GetMapping("/cart")
    public String viewCart(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        AppUser user = appUserRepository
                .findByUsername(username)
                .orElseThrow();

        List<Cart> cartItems = cartRepository.findAllByAppUser(user);

        model.addAttribute("username", username);
        model.addAttribute("cartItems", cartItems);
        int items = 0;
        for (Cart c : cartItems) {
            items += c.getQuantity();
        }
        long total = cartItems.stream()
                .mapToLong(c -> c.getProduct().getPrice() * c.getQuantity())
                .sum();
        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "usercart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        AppUser user = appUserRepository
                .findByUsername(username)
                .orElseThrow();

        List<Cart> cartItems = cartRepository.findAllByAppUser(user);

        Boolean address = addressRepository.findByAppUser(user).isPresent();
        if (address) {

            model.addAttribute("address", addressRepository.findByAppUser(user).orElseThrow());
        }
        model.addAttribute("username", username);
        model.addAttribute("cartItems", cartItems);
        int items = 0;
        for (Cart c : cartItems) {
            items += c.getQuantity();
        }
        long total = cartItems.stream()
                .mapToLong(c -> c.getProduct().getPrice() * c.getQuantity())
                .sum();

        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "checkoutpage";
    }

    @PostMapping("/checkout")
    public String addAddress(@ModelAttribute Address address, @RequestParam(required = false) Boolean saveAddress) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        AppUser user = appUserRepository
                .findByUsername(username)
                .orElseThrow();
        Address userAddress = new Address(
                address.getFullname(),
                address.getAddressLine(),
                address.getCity(),
                address.getState(),
                address.getPinCode(),
                address.getPhoneNumber(),
                user
        );

        addressRepository.save(userAddress);
        return "redirect:/users/checkout";
    }

    @GetMapping("/placeorder")
    public String placeOrder(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        AppUser user = appUserRepository
                .findByUsername(username)
                .orElseThrow();

        String orderId = UUID.randomUUID().toString();
        List<Cart> cartItems = cartRepository.findAllByAppUser(user);
        long total = cartItems.stream()
                .mapToLong(c -> c.getProduct().getPrice() * c.getQuantity())
                .sum();
        Orders order = new Orders(orderId, total, user);
        orderRepository.save(order);
        for (Cart c : cartItems) {
            orderedItemsRepository.save(new OrderedItems(
                    c.getProduct(),
                    c.getQuantity(),
                    order
            ));
        }
        model.addAttribute("orderid", orderId);
        cartRepository.deleteAll(cartItems);

        return "orderplaced";
    }

    @GetMapping("/orders")
    public String viewOrders(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        AppUser user = appUserRepository
                .findByUsername(username)
                .orElseThrow();
        List<OrderedItems> orders = orderedItemsRepository.findAllByAppUser(user);
        Map<String, List<OrderedItems>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(item -> item.getOrder().getOrderId()));
        model.addAttribute("orders", groupedOrders);
        return "previousorders";
    }

}

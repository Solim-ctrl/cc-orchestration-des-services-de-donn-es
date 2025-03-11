package eshop.ms.basket.controller;

import ehs.ms.basket.model.Basket;
import ehs.ms.basket.model.Item;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    // In-memory storage for user baskets
    private final Map<String, Basket> baskets = new ConcurrentHashMap<>();

    // Secret key for validating JWT tokens
    private final String secretKey = "secureKey1234567890";

    // Endpoint to add a product to the user's basket
    @PostMapping("/add")
    public ResponseEntity<?> addToBasket(@RequestHeader("Authorization") String token, @RequestBody Item product) {
        try {
            // Parse the JWT token to extract the username
            String username = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject(); // Extract the subject (username)

            // Retrieve or create the user's basket
            Basket basket = baskets.getOrDefault(username, new Basket(username, 0, "CREATED", new ArrayList<>()));

            // Add the product to the user's basket
            basket.getItems().add(product);
            basket.setTotalAmount(basket.calculateTotalAmount()); // Recalculate total amount

            // Update the basket in the map
            baskets.put(username, basket);

            return ResponseEntity.ok("Product added to basket");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // Endpoint to retrieve the user's basket
    @GetMapping("/view")
    public ResponseEntity<?> viewBasket(@RequestHeader("Authorization") String token) {
        try {
            // Parse the JWT token to extract the username
            String username = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();

            // Retrieve the user's basket
            Basket userBasket = baskets.getOrDefault(username, new Basket(username, 0, "CREATED", new ArrayList<>()));

            return ResponseEntity.ok(userBasket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // Endpoint to clear the user's basket
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearBasket(@RequestHeader("Authorization") String token) {
        try {
            // Parse the JWT token to extract the username
            String username = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();

            // Clear the user's basket
            baskets.remove(username);

            return ResponseEntity.ok("Basket cleared");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }
}

package eshop.ms.membership.controller;

import eshop.ms.membership.model.LoginRequest;
import eshop.ms.membership.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // In-memory storage for users (simulates a database)
    private final Map<String, User> users = new ConcurrentHashMap<>();

    // Secret key used to sign JWT tokens
    private final String secretKey = "secureKey1234567890";
    public UserController() {
        User admin = new User("admin", "admin123", "ADMIN"); // Admin username and password
        users.put(admin.getUsername(), admin); // Add admin to the user map
    }

    // Endpoint for user authentication
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        // Retrieve user from in-memory storage
        User user = users.get(loginRequest.getUsername());

        // Validation of credentials
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            // Generate JWT token for the authenticated user
            String token = Jwts.builder()
                    .setSubject(user.getUsername()) // Set username as the subject
                    .claim("role", user.getRole()) // Add user role as a claim
                    .setIssuedAt(new Date()) // Set token creation time
                    .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // Expiration time (1 hour)
                    .signWith(SignatureAlgorithm.HS256, secretKey) // Sign the token with the secret key
                    .compact();

            // Return the generated token
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }

        // Return error if credentials are invalid
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // Endpoint for creating a new user (only accessible by admins)
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestHeader("Authorization") String token, @RequestBody User newUser) {
        try {
            // Parse the JWT token to extract the role of the requesting user
            String role = Jwts.parser()
                    .setSigningKey(secretKey) // Use the secret key for verification
                    .parseClaimsJws(token.replace("Bearer ", "")) // Remove the "Bearer " prefix
                    .getBody()
                    .get("role", String.class); // Extract the "role" claim

            // Only allow users with ADMIN role to create new users
            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can create users");
            }

            // Chek if the username already exists
            if (users.containsKey(newUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            }

            // Add the new user to  in-memory storage
            users.put(newUser.getUsername(), newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            // Handle of cases where the token is invalid or expired
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }
}
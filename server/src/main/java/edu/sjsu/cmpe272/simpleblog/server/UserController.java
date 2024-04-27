package edu.sjsu.cmpe272.simpleblog.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint to create a new user
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User params) {
        try {
            // Extract public key from the request body and remove unnecessary parts
            String publicKey = params.getPublicKey().replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            // Call the UserService to create the user with the extracted public key
            userService.createUser(params.getUser(), publicKey);

            // Return a success response
            return ResponseEntity.ok(Map.of("message", "User created successfully"));
        } catch (Exception e) {
            // Log any error that occurs during user creation
            log.error("Error occurred while creating user: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint to list usernames
    @PostMapping("/list")
    public ResponseEntity<?> listMessages() {
        try {
            // Get the map of usernames and their public keys from the UserService
            Map<String, String> users = UserService.userPublicKeys;
            List<String> keyList = new ArrayList<>(users.keySet());
            // Return the list of usernames as a response
            return ResponseEntity.ok(keyList);
        } catch (Exception e) {
            // Log any error that occurs while listing usernames
            log.error("Error occurred while listing usernames: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint to get the public key of a specific user
    @GetMapping("/{username}/public-key")
    public ResponseEntity<?> getPublicKey(@PathVariable String username) {
        try {
            // Get the public key of the specified user from the UserService
            String publicKey = userService.getPublicKeyByUsername(username);
            // If the public key is not found, return a bad request response
            if (publicKey == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Public key not found for the user"));
            }
            // Return the public key as a response
            return ResponseEntity.ok(Map.of("public-key", publicKey));
        } catch (Exception e) {
            // Log any error that occurs while getting the public key of the user
            log.error("Error occurred while getting public key: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

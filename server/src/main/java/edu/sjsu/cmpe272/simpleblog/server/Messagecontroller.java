package edu.sjsu.cmpe272.simpleblog.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@Slf4j
public class Messagecontroller {

    @Autowired
    private MessageRepository messageRepository;

    // Endpoint to create a new message
    @PostMapping("/create")
    public ResponseEntity<?> createMessage(@RequestBody Messageinfo message) {
        try {
            // Get public key of the author from the userPublicKeys map
            PublicKey publicKey = UserService.getPublicKeyFromString(UserService.userPublicKeys.get(message.getAuthor()));

            // Verify the signature of the message using the author's public key
            boolean isSignatureValid = Signatureverification.verifySignature(message, publicKey);
            log.info("Is signature valid? {}", isSignatureValid);

            // If the signature is valid, save the message to the database
            if (publicKey != null && isSignatureValid) {
                Messageinfo savedMessage = messageRepository.save(message);
                return ResponseEntity.ok(Map.of("message-id", savedMessage.getMessageId()));
            } else {
                // If the signature is not valid, return an error response
                return ResponseEntity.ok(Map.of("error", "Failed to create message"));
            }
        } catch (Exception e) {
            log.error("Error occurred while creating message: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint to list messages with optional pagination
    @PostMapping("/list")
    public ResponseEntity<?> listMessages(@RequestBody Map<String, Object> params) {
        try {
            // Extract parameters from the request body
            Integer limit = (Integer) params.getOrDefault("limit", 10);
            Integer next = (Integer) params.getOrDefault("next", -1);
            Integer starting_id = (Integer) params.getOrDefault("starting_id", 0);

            // Validate the limit parameter
            if (limit > 20) {
                return ResponseEntity.badRequest().body("Error: Limit cannot be greater than 20");
            }

            // Retrieve messages from the database, ordered by message ID
            List<Messageinfo> messages = messageRepository.findAllByOrderByMessageIdAsc();

            // If 'next' parameter is specified, order messages by descending message ID
            if (next == -1) {
                messages = messageRepository.findAllByOrderByMessageIdDesc();
            }

            // Apply pagination if the number of messages exceeds the limit
            if (messages.size() > limit) {
                messages = messages.subList(starting_id, limit);
            }

            // Return the list of messages as a response
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            log.error("Error occurred while listing messages: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

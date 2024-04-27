package edu.sjsu.cmpe272.simpleblog.server;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Signatureverification {
    // Method to verify the signature of a message using the provided public key
    public static boolean verifySignature(Messageinfo message, PublicKey publicKey) {
        try {
            // Decode the signature from Base64
            String signature = message.getSignature();
            byte[] decodedSignature = Base64.getDecoder().decode(signature);

            // Concatenate the message fields to form the data to be verified
            String data = message.getDate() + message.getAuthor() + message.getMessage() + message.getAttachment();

            // Initialize a Signature object with SHA256withRSA algorithm and the provided public key
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());

            // Verify the signature against the data using the provided public key
            boolean isSignatureValid = sig.verify(decodedSignature);

            // Log whether the signature verification was successful
            log.info("Is signature valid? {}", isSignatureValid);

            return isSignatureValid;
        } catch (Exception e) {
            // Log any error that occurs during signature verification
            log.error("Error occurred while verifying signature: {}", e.getMessage());
            return false;
        }
    }
}



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CreateKeyPair {
    private static final Logger log = LoggerFactory.getLogger(CreateKeyPair.class);

    // Method to generate RSA key pair
    public static Map<String, String> generateRSAKeyPair() {
        try {
            Map<String, String> keys = new HashMap<>();
            // Initialize the Key Pair Generator
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            // Generate the Key Pair
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get the public and private keys from the key pair
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Convert keys to Base64 encoded strings
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            // Log the keys
            log.info("Public Key: {}", publicKeyString);
            log.info("Private Key: {}", privateKeyString);

            keys.put("publicKey", publicKeyString);
            keys.put("privateKey", privateKeyString);
            return keys;

        } catch (NoSuchAlgorithmException e) {
            log.error("Error occurred while generating RSA key pair: {}", e.getMessage());
            return null;
        }
    }

    // Method to convert a String to PrivateKey
    public static PrivateKey convertStringToPrivateKey(String keyStr, String algorithm) {
        try {
            // Base64 decode the data
            byte[] encoded = Base64.getDecoder().decode(keyStr);

            // Create a key factory
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

            // Generate the private key from the decoded bytes
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("Error occurred while converting String to PrivateKey: {}", e.getMessage());
            return null;
        }
    }

    // Method to encode PublicKey to PEM format
    public static String encodePublicKeyToPEM(String base64Encoded) {
        try {
            // Properly format the encoded key in PEM format
            StringBuilder pemFormat = new StringBuilder();
            pemFormat.append("-----BEGIN PUBLIC KEY-----\n");

            // Split the encoded string into multiple lines
            int index = 0;
            while (index < base64Encoded.length()) {
                int endIndex = Math.min(index + 64, base64Encoded.length());
                pemFormat.append(base64Encoded.substring(index, endIndex));
                pemFormat.append("\n");
                index = endIndex;
            }

            pemFormat.append("-----END PUBLIC KEY-----");
            return pemFormat.toString();
        } catch (Exception e) {
            log.error("Error occurred while encoding PublicKey to PEM format: {}", e.getMessage());
            return null;
        }
    }
}


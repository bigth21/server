package spring.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setLenientDateParsing;

public class PasswordEncoderTest {

    @Test
    void encryptor() {
        String salt = KeyGenerators.string().generateKey();
        String password = "secret";
        String valueToEncrypt = "HELLO";

        BytesEncryptor e = Encryptors.standard(password, salt); // Encrypt password default by AES256
        byte[] encrypted = e.encrypt(valueToEncrypt.getBytes());
        byte[] decrypted = e.decrypt(encrypted);
        assertThat(valueToEncrypt.getBytes()).isEqualTo(decrypted);

        BytesEncryptor eStronger = Encryptors.stronger(password, salt);
        encrypted = eStronger.encrypt(valueToEncrypt.getBytes());
        decrypted = eStronger.decrypt(encrypted);
        assertThat(valueToEncrypt.getBytes()).isEqualTo(decrypted);

        TextEncryptor textEncryptor = Encryptors.text(password, salt); // Use standard
        String encryptedStr = textEncryptor.encrypt(valueToEncrypt);
        String decryptedStr = textEncryptor.decrypt(encryptedStr);
        assertThat(decryptedStr).isEqualTo(valueToEncrypt);

        textEncryptor = Encryptors.delux(password, salt); // Use stronger
        encryptedStr = textEncryptor.encrypt(valueToEncrypt);
        decryptedStr = textEncryptor.decrypt(encryptedStr);
        assertThat(decryptedStr).isEqualTo(valueToEncrypt);
    }

    @Test
    void keyGenerator() {
        StringKeyGenerator stringKeyGenerator = KeyGenerators.string();
        String key = stringKeyGenerator.generateKey(); // Create 8 bytes key then encode to hex
        System.out.println("key = " + key);

        BytesKeyGenerator bytesKeyGenerator = KeyGenerators.secureRandom();
        byte[] bytes = bytesKeyGenerator.generateKey();
        System.out.println("bytes = " + bytes); // Create 8 bytes key
        int keyLength = bytesKeyGenerator.getKeyLength();
        System.out.println("keyLength = " + keyLength);

        bytesKeyGenerator = KeyGenerators.secureRandom(16);
        bytes = bytesKeyGenerator.generateKey();
        System.out.println("bytes = " + bytes);
        keyLength = bytesKeyGenerator.getKeyLength();
        System.out.println("keyLength = " + keyLength);

        BytesKeyGenerator shared = KeyGenerators.shared(16);
        assertThat(shared.generateKey()).isEqualTo(shared.generateKey());
        keyLength = shared.getKeyLength();
        System.out.println("keyLength = " + keyLength);
    }

    @Test
    void delegatingPasswordEncoder() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Create delegatingPasswordEncoder having bcrypt as default encoder
        String encoded = passwordEncoder.encode("hihi");
        System.out.println("encoded = " + encoded);

    }

    @Test
    void sha512() {
        var passwordEncoder = new Sha512PasswordEncoder();
        String encoded = passwordEncoder.encode("hihihi");
        System.out.println("encoded = " + encoded);
    }


    static class Sha512PasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return hashWithSHA512(rawPassword.toString());
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }

        private String hashWithSHA512(String input) {
            StringBuilder sb = new StringBuilder();
            try {
                var md = MessageDigest.getInstance("SHA-512");
                byte[] digested = md.digest(input.getBytes());
                for (byte d : digested) {
                    sb.append(Integer.toHexString(0xFF & d));
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            return sb.toString();
        }
    }
}

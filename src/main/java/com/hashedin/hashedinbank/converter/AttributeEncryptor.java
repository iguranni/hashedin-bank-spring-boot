package com.hashedin.hashedinbank.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.hashedin.hashedinbank.constants.AppConstants.AES;
import static com.hashedin.hashedinbank.constants.AppConstants.SECRET;

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor() throws NoSuchPaddingException, NoSuchAlgorithmException {
        key = new SecretKeySpec(SECRET.getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    /**
     * @param ccNumber
     * @return
     */
    @Override
    public String convertToDatabaseColumn(String ccNumber) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(ccNumber.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param ccNumber
     * @return
     */
    @Override
    public String convertToEntityAttribute(String ccNumber) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ccNumber)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
    }
}

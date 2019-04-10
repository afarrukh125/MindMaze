package me.thirtyone.group.mindmaze.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Abdullah on 12/03/2019 23:46
 */

public class RegistrationUtils {

    /**
     * This function aims to secure the password by hashing it according to SHA-512
     *
     * @param pass The password to be hashed
     * @return Returns a string corresponding to the hashed password
     */
    public static String hashPass(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(pass.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

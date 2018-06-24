package rox.main.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.security.MessageDigest;

public class MathUtil {

    private char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?").toCharArray();



    public String computeSHAHex(String string){
        return DigestUtils.shaHex(string);
    }

    public String computeMD5Hex(String string){
        return DigestUtils.md5Hex(string);
    }

    public String computeSHA256(String string) {
        try{
            StringBuilder hexString = new StringBuilder();

            for (byte aHash : MessageDigest.getInstance("SHA-256").digest(string.getBytes("UTF-8"))) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public String getRandomString(int length){
        return RandomStringUtils.random(length, possibleCharacters);
    }

    public char[] getPossibleCharacters() {
        return possibleCharacters;
    }

    public void setPossibleCharacters(char[] possibleCharacters) {
        this.possibleCharacters = possibleCharacters;
    }
}

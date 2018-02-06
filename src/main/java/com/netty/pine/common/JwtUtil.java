package com.netty.pine.common;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JwtUtil {

    public static String getMD5HexString(String data) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        if(digest == null) {
            return null;
        }

        // for salt
        digest.reset();

        // digest
        byte[] encoded = null;
        try {
            encoded = digest.digest(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if(encoded == null) {
            return null;
        }

        //return Base64.getEncoder().encodeToString(encoded);
        return Hex.encodeHexString(encoded);
    }


}

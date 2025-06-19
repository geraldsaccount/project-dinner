package com.geraldsaccount.killinary.utils;

import java.util.Base64;

public class ImageConverter {
    public static String imageAsBase64(byte[] image) {
        if (image != null && image.length > 0) {
            return Base64.getEncoder().encodeToString(image);
        }
        return null;
    }
}

package com.zero.srd.core;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.zero.srd.util.SecurityUtil;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private String[] encryptPropNames = { "jdbc.password", "smtp.password" };

    private boolean isEncryptProp(String propertyName) {
        for (String encryptName : encryptPropNames) {
            if (encryptName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }

    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            String decryptValue = SecurityUtil.decode(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }
}
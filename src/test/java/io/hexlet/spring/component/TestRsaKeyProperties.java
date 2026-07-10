package io.hexlet.spring.component;

import org.springframework.boot.test.context.TestComponent;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@TestComponent
public class TestRsaKeyProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}

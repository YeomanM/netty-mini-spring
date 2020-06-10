package com.yeoman.minispring.handler.ssl;

import com.yeoman.minispring.utils.YamlUtil;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLContentFactory {


    public static SSLContext getContent() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        String passStr = (String) YamlUtil.getOrDefault("ssl.pass", "mjjchou123");
        String jksName = (String) YamlUtil.getOrDefault("ssl.jksName", "local.jks");
        char[] pass = passStr.toCharArray();
        SSLContext context = SSLContext.getInstance("TLSv1");
        KeyStore store = KeyStore.getInstance("JKS");

        InputStream is = SSLContentFactory.class.getClassLoader().getResourceAsStream(jksName);
        store.load(is, pass);
        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(store, pass);
        context.init(factory.getKeyManagers(), null, null);
        is.close();
        return context;
    }

}

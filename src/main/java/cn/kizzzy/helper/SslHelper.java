package cn.kizzzy.helper;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SslHelper {
    
    /**
     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
     */
    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
    
    public static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new MITM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    
    static class MITM implements TrustManager, X509TrustManager {
        
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        
        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }
        
        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }
        
        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
        
        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
    }
}

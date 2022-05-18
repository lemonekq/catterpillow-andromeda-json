package caterpillow;

import java.security.*;
import javax.net.ssl.*;
import java.security.cert.*;

public final class SSLUtilities
{
    private static HostnameVerifier __hostnameVerifier;
    private static TrustManager[] __trustManagers;
    private static HostnameVerifier _hostnameVerifier;
    private static TrustManager[] _trustManagers;
    
    private static void __trustAllHostnames() {
        if (SSLUtilities.__hostnameVerifier == null) {
            SSLUtilities.__hostnameVerifier = new _FakeHostnameVerifier();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(SSLUtilities.__hostnameVerifier);
    }
    
    private static void __trustAllHttpsCertificates() {
        if (SSLUtilities.__trustManagers == null) {
            SSLUtilities.__trustManagers = new TrustManager[] { new _FakeX509TrustManager() };
        }
        SSLContext context;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, SSLUtilities.__trustManagers, new SecureRandom());
        }
        catch (GeneralSecurityException gse) {
            throw new IllegalStateException(gse.getMessage());
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
    
    private static boolean isDeprecatedSSLProtocol() {
        return "internal.www.protocol".equals(System.getProperty("java.protocol.handler.pkgs"));
    }
    
    private static void _trustAllHostnames() {
        if (SSLUtilities._hostnameVerifier == null) {
            SSLUtilities._hostnameVerifier = new FakeHostnameVerifier();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(SSLUtilities._hostnameVerifier);
    }
    
    private static void _trustAllHttpsCertificates() {
        if (SSLUtilities._trustManagers == null) {
            SSLUtilities._trustManagers = new TrustManager[] { new FakeX509TrustManager() };
        }
        SSLContext context;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, SSLUtilities._trustManagers, new SecureRandom());
        }
        catch (GeneralSecurityException gse) {
            throw new IllegalStateException(gse.getMessage());
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
    
    public static void trustAllHostnames() {
        if (isDeprecatedSSLProtocol()) {
            __trustAllHostnames();
        }
        else {
            _trustAllHostnames();
        }
    }
    
    public static void trustAllHttpsCertificates() {
        if (isDeprecatedSSLProtocol()) {
            __trustAllHttpsCertificates();
        }
        else {
            _trustAllHttpsCertificates();
        }
    }
    
    public static class _FakeHostnameVerifier implements HostnameVerifier
    {
        public boolean verify(final String hostname, final String session) {
            return true;
        }
        
        @Override
        public boolean verify(final String arg0, final SSLSession arg1) {
            return false;
        }
    }
    
    public static class _FakeX509TrustManager implements X509TrustManager
    {
        private static final X509Certificate[] _AcceptedIssuers;
        
        public boolean isClientTrusted(final X509Certificate[] chain) {
            return true;
        }
        
        public boolean isServerTrusted(final X509Certificate[] chain) {
            return true;
        }
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return _FakeX509TrustManager._AcceptedIssuers;
        }
        
        @Override
        public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
        }
        
        @Override
        public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
        }
        
        static {
            _AcceptedIssuers = new X509Certificate[0];
        }
    }
    
    public static class FakeHostnameVerifier implements HostnameVerifier
    {
        @Override
        public boolean verify(final String hostname, final SSLSession session) {
            return true;
        }
    }
    
    public static class FakeX509TrustManager implements X509TrustManager
    {
        private static final X509Certificate[] _AcceptedIssuers;
        
        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
        }
        
        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
        }
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return FakeX509TrustManager._AcceptedIssuers;
        }
        
        static {
            _AcceptedIssuers = new X509Certificate[0];
        }
    }
}

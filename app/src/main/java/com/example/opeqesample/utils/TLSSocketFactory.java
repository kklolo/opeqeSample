package com.example.opeqesample.utils;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


//https://www.ssllabs.com/ssltest/analyze.html?d=turkey.sina-team.ir check ssl for retrofit
//fixed retrofit tls 1.2v on api 19 handshake ssl error by lolo
//error:SSL23_GET_SERVER_HELLO:tlsv1 alert protocol version ?
//this is because the current webservice host only has tls 1.3 and tls 1.2 . tls 1.2 is disabled in android < 21 . with this class set http client ssl only use 1.2 for api 19 and lower
//more info
//iuuqt://nfejvn.dpn/@lsjtobwoffu/ipx-up-tpmwf-ttmiboetiblffydfqujpo-jo-boespje-ttm23-hfu-tfswfs-ifmmp-umtw1-bmfsu-qspupdpm-13c457d724fg
public class TLSSocketFactory extends SSLSocketFactory {

    private SSLSocketFactory delegate;
    private TrustManager[] trustManagers;

    public TLSSocketFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        generateTrustManagers();
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustManagers, null);
        delegate = context.getSocketFactory();
    }

    private void generateTrustManagers() throws KeyStoreException, NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }

        this.trustManagers = trustManagers;
    }


    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket() throws IOException {
        return enableTLSOnSocket(delegate.createSocket());
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        if(socket != null && (socket instanceof SSLSocket)) {
            ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
        }
        return socket;
    }

    @Nullable
    public X509TrustManager getTrustManager() {
        return  (X509TrustManager) trustManagers[0];
    }

}
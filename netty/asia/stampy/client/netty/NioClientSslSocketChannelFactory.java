package asia.stampy.client.netty;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.SocketChannel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.ssl.SslHandler;


public class NioClientSslSocketChannelFactory extends NioClientSocketChannelFactory {
  
  private File clientAuthKeyStore;
  private String clientAuthKeyStorePassword;
  private String clientAuthKeyStoreFormat;
  
  private File trustKeyStore;
  private String trustKeyStorePassword;
  private String trustKeyStoreFormat;
  

  public NioClientSslSocketChannelFactory(ExecutorService bossExecutor, ExecutorService workerExecutor) {
    super(bossExecutor, workerExecutor);
  }
  
  @Override
  public SocketChannel newChannel(ChannelPipeline pipeline) {
    
      KeyManager[] keyManagers = null;
      try {
        if(this.clientAuthKeyStore != null) {
          KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
          KeyStore keyStore = KeyStore.getInstance(this.clientAuthKeyStoreFormat);
          
          InputStream keyInput = new FileInputStream(this.clientAuthKeyStore);
          keyStore.load(keyInput, this.clientAuthKeyStorePassword.toCharArray());
          keyInput.close();

          keyManagerFactory.init(keyStore, this.clientAuthKeyStorePassword.toCharArray());
          keyManagers = keyManagerFactory.getKeyManagers();
        }
      } catch (Exception e) {
        throw new RuntimeException("Cannot create client auth key store for channel", e);
      }
      
      TrustManager[] trustManagers = null;
      try {
        if(this.trustKeyStore != null) {
          TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
          KeyStore keyStore = KeyStore.getInstance(this.trustKeyStoreFormat);
          
          InputStream keyInput = new FileInputStream(this.trustKeyStore);
          keyStore.load(keyInput, this.trustKeyStorePassword.toCharArray());
          keyInput.close();

          trustManagerFactory.init(keyStore);
          trustManagers = trustManagerFactory.getTrustManagers();
        }
      } catch (Exception e) {
        throw new RuntimeException("Cannot create trust key store for channel", e);
      }
      
      try {
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(keyManagers, trustManagers, null);
      SSLEngine sslEngine = sslContext.createSSLEngine();
      
      sslEngine.setUseClientMode(true);
      sslEngine.setWantClientAuth(keyManagers != null);
      pipeline.addFirst("ssl", new SslHandler(sslEngine));
      return super.newChannel(pipeline);
    } catch (Exception ex) {
      throw new RuntimeException("Cannot create SSL channel", ex);
    }
  }
  
  public void addClientAuthKeyStore(File keyStoreLocation, String keyStorePassword, String keyStoreFormat) {
    this.clientAuthKeyStore = keyStoreLocation;
    this.clientAuthKeyStorePassword = keyStorePassword;
    this.clientAuthKeyStoreFormat = keyStoreFormat;
  }
  
  public void addTrustKeyStore(File keyStoreLocation, String keyStorePassword, String keyStoreFormat) {
    this.trustKeyStore = keyStoreLocation;
    this.trustKeyStorePassword = keyStorePassword;
    this.trustKeyStoreFormat = keyStoreFormat;
  }
}

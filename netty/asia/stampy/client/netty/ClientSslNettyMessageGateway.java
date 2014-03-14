package asia.stampy.client.netty;

import java.io.File;
import java.util.concurrent.Executors;

public class ClientSslNettyMessageGateway extends ClientNettyMessageGateway {
  
  public ClientSslNettyMessageGateway() {
    this.factory = new NioClientSslSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
  }
  
  public void addClientAuthKeyStore(File keyStoreLocation, String keyStorePassword, String keyStoreFormat) {
    ((NioClientSslSocketChannelFactory)this.factory).addClientAuthKeyStore(keyStoreLocation, keyStorePassword, keyStoreFormat);
  }
  
  public void addTrustKeyStore(File keyStoreLocation, String keyStorePassword, String keyStoreFormat) {
    ((NioClientSslSocketChannelFactory)this.factory).addTrustKeyStore(keyStoreLocation, keyStorePassword, keyStoreFormat);
  }

}

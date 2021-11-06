package fakahedaminequery;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public final class QueryServer extends Thread {
  private final Minequery minequery;
  private final String host;
  private final int port;
  private final ServerSocket listener;
  private final Logger log;
  InetSocketAddress address;	 

  
  public QueryServer(Minequery minequery, String host, int port) throws IOException {
    this.log = Logger.getLogger("Minecraft");
    this.minequery = minequery;
    this.host = host;
    this.port = port;
    if (host.equalsIgnoreCase("ANY")) {
      this.log.info("Starting FakaHedaMinequery server on *:" + Integer.toString(port));
      address = new InetSocketAddress(port);
    } else {
      this.log.info("Starting FakaHedaMinequery server on " + host + ":" + Integer.toString(port));
      address = new InetSocketAddress(host, port);
    } 
    this.listener = new ServerSocket();
    this.listener.bind(address);
  }
  
  public void run() {
    try {
      while (true) {
        Socket socket = getListener().accept();
        (new Thread(new Request(getMinequery(), socket))).start();
      } 
    } catch (IOException ex) {
      this.log.info("Stopping FakaHedaMinequery server");
      return;
    } 
  }
  
  public Minequery getMinequery() { return this.minequery; }
  public String getHost() { return this.host; }
  public int getPort() { return this.port; }
  public ServerSocket getListener() { return this.listener; }
}

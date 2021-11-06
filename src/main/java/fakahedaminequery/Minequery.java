package fakahedaminequery;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minequery extends JavaPlugin {
  public static final String CONFIG_FILE = "server.properties";
  private final Logger log = Logger.getLogger("Minecraft");
  private String serverIP;
  private String motd;
  private int serverPort;
  private int port;
  private String whiteList;
  private String levelName;
  private String serverName;
  private QueryServer server;
  
  public Minequery() {
    try {
      Properties props = new Properties();
      props.load(new FileReader("server.properties"));
      this.whiteList = props.getProperty("white-list", "true");
      this.levelName = props.getProperty("level-name", "World");
      this.serverIP = props.getProperty("server-ip", "ANY");
      this.motd = props.getProperty("motd", "");
      this.serverName = props.getProperty("server-name", "");
      if (this.serverIP.equals(""))
        this.serverIP = "ANY"; 
    } catch (IOException ex) {
      this.log.log(Level.SEVERE, "Error initializing FakaHedaMinequery", ex);
    } 
  }
  
  public void onDisable() {
    try {
      this.server.getListener().close();
    } catch (IOException ex) {
      this.log.log(Level.WARNING, "Unable to close the FakaHedaMinequery listener", ex);
    } 
  }
  
  public void onEnable() {
    try {
      this.serverPort = getServer().getPort();
      this.serverIP = getServer().getIp();
      this.port = this.serverPort + 1000;
      this.server = new QueryServer(this, this.serverIP, this.port);
    } catch (IOException ex) {
      this.log.log(Level.SEVERE, "Error initializing FakaHedaMinequery", ex);
    } 
    if (this.server == null)
      throw new IllegalStateException("Cannot enable - FakaHedaMinequery not initialized"); 
    this.server.start();
  }
  
  public String getWhiteList() { return this.whiteList; }
  public String getLevelName() { return this.levelName; }
  public String getMotd() { return this.motd; }
  public String getServerName() { return this.serverName; }
}

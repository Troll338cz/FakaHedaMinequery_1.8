package fakahedaminequery;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class Request extends Thread {
  private final Minequery minequery;
  
  private final Socket socket;
  
  private final Logger log;
  
  private int i;
  
  public Request(Minequery minequery, Socket socket) {
    this.log = Logger.getLogger("Minecraft");
    this.minequery = minequery;
    this.socket = socket;
  }
  
  public void run() {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      handleRequest(this.socket, reader.readLine());
      this.socket.close();
    } catch (IOException ex) {
      this.log.log(Level.SEVERE, "FakaHedaMinequery server thread shutting down", ex);
    } 
  }
  
  private void handleRequest(Socket socket, String request) throws IOException {
    Minequery m = getMinequery();
    List<Player> p = null;
    try {
      Method method = Bukkit.class.getMethod("getOnlinePlayers", new Class[0]);
      if (!method.getReturnType().isAssignableFrom(java.util.Collection.class)) {
        p = Arrays.asList((Player[])method.invoke(null, new Object[0]));
      } else {
        p = (List)Bukkit.getOnlinePlayers();
      } 
    } catch (Throwable t) {
      System.out.println("ERROR " + t);
      p = Collections.emptyList();
    } 
    if (request == null)
      return; 
    if (request.equalsIgnoreCase("QUERY")) {
      String[] playerList = new String[p.size()];
      int i = 0;
      for (Player element : p) {
        playerList[i] = element.getName();
        i++;
      } 
      StringBuilder resp = new StringBuilder();
      resp.append("SERVERPORT " + m.getServer().getPort() + "\n");
      resp.append("PLAYERCOUNT " + p.size() + "\n");
      resp.append("MAXPLAYERS " + m.getServer().getMaxPlayers() + "\n");
      resp.append("SERVERNAME " + m.getServerName() + "\n");
      resp.append("WHITELIST " + m.getWhiteList() + "\n");
      resp.append("LEVELNAME " + m.getLevelName() + "\n");
      resp.append("TIME " + m.getServer().getWorld(m.getLevelName()).getTime() + "\n");
      resp.append("VERSION " + m.getServer().getVersion() + "\n");
      resp.append("PLAYERLIST " + Arrays.toString(playerList) + "\n");
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      out.writeBytes(resp.toString());
    } 
    if (request.equalsIgnoreCase("FHQUERY")) {
      String[] playerList = new String[p.size()];
      int i = 0;
      for (Player element : p) {
        playerList[i] = element.getName();
        i++;
      } 
      StringBuilder resp = new StringBuilder();
      resp.append("SERVERPORT " + m.getServer().getPort() + "\n");
      resp.append("PLAYERCOUNT " + p.size() + "\n");
      resp.append("MAXPLAYERS " + m.getServer().getMaxPlayers() + "\n");
      resp.append("SERVERNAME " + m.getServerName() + "\n");
      resp.append("MOTD " + m.getMotd() + "\n");
      resp.append("WHITELIST " + m.getWhiteList() + "\n");
      resp.append("LEVELNAME " + m.getLevelName() + "\n");
      resp.append("TIME " + m.getServer().getWorld(m.getLevelName()).getTime() + "\n");
      resp.append("VERSION " + m.getServer().getVersion() + "\n");
      resp.append("PLAYERLIST " + Arrays.toString(playerList) + "\n");
      resp.append("MEMORY_MAX " + Runtime.getRuntime().maxMemory() + "\n");
      resp.append("MEMORY_TOTAL " + Runtime.getRuntime().totalMemory() + "\n");
      resp.append("MEMORY_FREE " + Runtime.getRuntime().freeMemory() + "\n");
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      out.writeBytes(resp.toString());
    } 
    if (request.equalsIgnoreCase("QUERY_JSON")) {
      StringBuilder resp = new StringBuilder();
      resp.append("{");
      resp.append("\"serverPort\":").append(m.getServer().getPort()).append(",");
      resp.append("\"playerCount\":").append(p.size()).append(",");
      resp.append("\"maxPlayers\":").append(m.getServer().getMaxPlayers()).append(",");
      resp.append("\"serverName\":\"").append(m.getServerName()).append("\",");
      resp.append("\"whiteList\":\"").append(m.getWhiteList()).append("\",");
      resp.append("\"levelName\":\"").append(m.getLevelName()).append("\",");
      resp.append("\"time\":").append(m.getServer().getWorld(m.getLevelName()).getTime()).append(",");
      resp.append("\"version\":\"").append(m.getServer().getVersion()).append("\",");
      resp.append("\"playerList\":");
      resp.append("[");
      int count = 0;
      for (Player player : p) {
        resp.append("\"").append(player.getName()).append("\"");
        if (++count < p.size())
          resp.append(","); 
      } 
      resp.append("]");
      resp.append("}\n");
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      out.writeBytes(resp.toString());
    } 
    if (request.equalsIgnoreCase("FHQUERY_JSON")) {
      StringBuilder resp = new StringBuilder();
      resp.append("{");
      resp.append("\"serverPort\":").append(m.getServer().getPort()).append(",");
      resp.append("\"playerCount\":").append(p.size()).append(",");
      resp.append("\"maxPlayers\":").append(m.getServer().getMaxPlayers()).append(",");
      resp.append("\"serverName\":\"").append(m.getServerName()).append("\",");
      resp.append("\"motd\":\"").append(m.getMotd()).append("\",");
      resp.append("\"whiteList\":\"").append(m.getWhiteList()).append("\",");
      resp.append("\"levelName\":\"").append(m.getLevelName()).append("\",");
      resp.append("\"time\":").append(m.getServer().getWorld(m.getLevelName()).getTime()).append(",");
      resp.append("\"version\":\"").append(m.getServer().getVersion()).append("\",");
      resp.append("\"playerList\":");
      resp.append("[");
      int count = 0;
      for (Player player : p) {
        resp.append("\"" + player.getName() + "\"");
        if (++count < p.size())
          resp.append(","); 
      } 
      resp.append("]");
      resp.append("\"memory_max\":\"").append(Runtime.getRuntime().maxMemory()).append("\",");
      resp.append("\"memory_total\":\"").append(Runtime.getRuntime().totalMemory()).append("\",");
      resp.append("\"memory_free\":\"").append(Runtime.getRuntime().freeMemory()).append("\"");
      resp.append("}\n");
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      out.writeBytes(resp.toString());
    } 
  }
  
  public Minequery getMinequery() { return this.minequery; }
}
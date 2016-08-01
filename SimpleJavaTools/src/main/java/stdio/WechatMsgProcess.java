package stdio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class for processing wechat message histories.
 */
public class WechatMsgProcess {

  private static Map<String, List<String>> waterByName = new HashMap<>();

  public static void skipLines(String inFile) {
    try {
      BufferedReader br = Files.newBufferedReader(Paths.get(inFile));
      BufferedWriter bw = Files.newBufferedWriter(Paths.get("wechat-out.txt"),
          StandardOpenOption.CREATE);
      String line;
      boolean isUserLine = true;
      String username = "default blank user";
      bw.write("All watering contents:");
      bw.newLine();
      while ((line = br.readLine()) != null) {
        // skip empty lines and date lines
        if (line.length() == 0 || line.startsWith("—————")) continue;
        if (!isUserLine) {
          if (!waterByName.containsKey(username))
            waterByName.put(username, new ArrayList<String>());
          waterByName.get(username).add(line);
          bw.write(line);
          bw.newLine();
        } else username = line.substring(0, line.indexOf(" "));
        isUserLine = !isUserLine;
      }
      bw.write("\n\n\n");
      bw.write("All watering contents by username:");
      bw.newLine();
      for (String user : waterByName.keySet()) {
        bw.write(user);
        bw.newLine();
        List<String> messages = waterByName.get(user);
        for (String msg : messages) {
          bw.write("  " + msg);
          bw.newLine();
        }
      }
      br.close();
      bw.close();
    } catch (IOException e) {
      System.out.println("Reading Writing file error.");
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    final String USAGE = "usage: stdio.WechatMSgProcess inputFilename";
    if (args.length != 1) System.out.println(USAGE);
    skipLines(args[0]);
  }
}

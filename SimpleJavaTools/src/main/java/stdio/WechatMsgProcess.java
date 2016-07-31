package stdio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * A utility class for processing wechat message histories.
 */
public class WechatMsgProcess {
  public static void skipLines(String inFile) {
    try {
      BufferedReader br = Files.newBufferedReader(Paths.get(inFile));
      BufferedWriter bw = Files.newBufferedWriter(Paths.get("wechat-out.txt"),
          StandardOpenOption.CREATE);
      String line;
      boolean isUserLine = true;
      while ((line = br.readLine()) != null) {
        // skip empty lines and date lines
        if (line.length() == 0 || line.startsWith("—————")) continue;
        if (!isUserLine) {
          bw.write(line);
          bw.newLine();
        }
        isUserLine = !isUserLine;
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

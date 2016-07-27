package stdio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Reads 0 and 1 from command line and write to file as binary data.
 */
public class BinaryStringOut {

  private static void writeBits(String filename) {
    try {
      DataOutputStream out = new DataOutputStream(Files
          .newOutputStream(Paths.get(filename), StandardOpenOption.CREATE));
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String sbuf;

      while ((sbuf = in.readLine()) != null) {
        if (sbuf.length() % 8 != 0) {
          System.out.println("this line is not a multiple of 8 bits: " + sbuf);
          System.exit(-1);
        }
        char[] cbuf = sbuf.toCharArray();
        byte[] bbuf = new byte[cbuf.length / 8];
        for (int i = 0; i < cbuf.length; i++) {
          bbuf[i / 8] <<= 1;
          if (cbuf[i] == '1') bbuf[i / 8] += 1;
        }
        out.write(bbuf);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    final String USAGE = "Usage:\n  java BinaryOut filename";

    if (args.length != 1) System.out.println(USAGE);

    writeBits(args[0]);

  }
}

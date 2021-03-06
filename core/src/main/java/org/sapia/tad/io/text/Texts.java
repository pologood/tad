package org.sapia.tad.io.text;

import org.sapia.tad.Dataset;

import java.io.*;

/**
 * Saves dataset content in text format.
 * 
 * @author yduchesne
 *
 */
public class Texts {

  
  private Texts() {
  }
  
  /**
   * @param ds the {@link Dataset} whose {@link String} represention is to be saved.
   * @param fileName the name of the file to save to.
   * @throws IOException if an IO error occurs while trying to write the representation.
   */
  public static void save(Dataset ds, String fileName) throws IOException {
    
    FileWriter  fw = new FileWriter(new File(fileName));
    try {
      PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
      pw.print(ds.toString());
      pw.flush();
      pw.close();
    } finally {
      fw.close();
    }
    
  }
}

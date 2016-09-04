package org.sapia.tad.io.weka;

import org.sapia.tad.ColumnSet;
import org.sapia.tad.Dataset;
import org.sapia.tad.io.DatasetReader;
import weka.core.Instances;

import java.io.IOException;
import java.io.Reader;

/**
 * Reads ARFF-formatted datasets.
 * 
 * @author yduchesne
 *
 */
public class ArffReader implements DatasetReader {

  /**
   * This method calls {@link #read(Reader)}. The given {@link ColumnSet} is thus ignored
   * (Weka's ARFF file format supports attribute descriptions built in).
   */
  @Override
  public Dataset read(ColumnSet columns, Reader reader, int skipLines)
      throws IOException {
    return read(columns, reader, 0);
  }

  /**
   * This method calls {@link #read(Reader)}. The given {@link ColumnSet} is thus ignored
   * (Weka's ARFF file format supports attribute descriptions built in).
   */
  @Override
  public Dataset read(ColumnSet columns, Reader reader) throws IOException {
    return read(reader);
  }

  /**
   * @param reader a {@link Reader} to read from.
   * @return the {@link Dataset} holding the data provided by the given {@link Reader}.
   * @throws IOException if an IO problem occurs while reading the data.
   */
  public Dataset read(Reader reader) throws IOException {
    Instances data = new Instances(reader);
    return new WekaDatasetAdapter(data);
  }
}

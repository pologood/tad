package org.sapia.tad.matrix.impl;

import org.sapia.tad.Datatype;
import org.sapia.tad.Vector;
import org.sapia.tad.conf.Conf;
import org.sapia.tad.io.table.Row;
import org.sapia.tad.io.table.Table;
import org.sapia.tad.matrix.Matrix;

/**
 * @author yduchesne
 */
public class MatrixImplHelper {

  private MatrixImplHelper() {
    // noop
  }

  public static String toString(Matrix matrix) {

    Table table = Table.obj();
    for (int i = 0; i < matrix.getDimensions().getColumnCount(); i++) {
      table.header("col_" + i, Conf.getCellWidth());
    }

    for (int i = 0; i < matrix.getDimensions().getRowCount(); i++) {
      Row tableRow = table.row();
      Vector row = matrix.getRow(i);
      for (int j = 0; j < matrix.getDimensions().getColumnCount(); j++) {
        tableRow.cell(row.get(j).toString());
      }
    }

    return table.toString();

  }
}

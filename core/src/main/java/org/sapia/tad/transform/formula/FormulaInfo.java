package org.sapia.tad.transform.formula;

import org.sapia.tad.RowResult;
import org.sapia.tad.value.Value;

class FormulaInfo {
  
  private int                 columnIndex;
  private Formula <RowResult> function;
  
  FormulaInfo(int columnIndex, Formula<RowResult> function) {
    this.columnIndex = columnIndex;
    this.function    = function;
  } 
  
  int getColumnIndex() {
    return columnIndex;
  }
  
  Value apply(RowResult row) {
    return function.call(row);
  }
}

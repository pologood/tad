package org.sapia.tad.transform.formula;

import org.sapia.tad.ColumnSet;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultRowResult;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.impl.VectorImpl;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;

import java.util.Iterator;
import java.util.NoSuchElementException;

class FormulaVector extends VectorImpl {
  
  private ColumnSet     columns;
  private Vector        delegate;
  private FormulaInfo[] formulas;
  
  FormulaVector(ColumnSet columns, Vector delegate, FormulaInfo[] formulas) {
    this.columns   = columns;
    this.delegate  = delegate;
    this.formulas = formulas;
  }
  
  @Override
  public Value get(int index) throws IllegalArgumentException {
    Checks.isTrue(
        index < delegate.size() + formulas.length, 
        "Invalid index: %s. Got %s values",  delegate.size() + formulas.length
    );
    if  (index < delegate.size()) {
      return delegate.get(index);
    }
    FormulaInfo f = formulas[index - delegate.size()];
    
    DefaultRowResult result = new DefaultRowResult(columns);
    result.setVector(delegate);
    return f.apply(result);
  }

  @Override
  public int size() {
    return delegate.size() + formulas.length;
  }
  
}

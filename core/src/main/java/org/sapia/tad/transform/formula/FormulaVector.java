package org.sapia.tad.transform.formula;

import org.sapia.tad.ColumnSet;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultRowResult;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;

import java.util.Iterator;
import java.util.NoSuchElementException;

class FormulaVector implements Vector {
  
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
        "Invalid index: %s. Got %s array",  delegate.size() + formulas.length
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
  public Iterator<Value> iterator() {
    return new Iterator<Value>() {
      
      private int index;
      @Override
      public boolean hasNext() {
        return index < delegate.size() + formulas.length;
      }
      
      @Override
      public Value next() {
        if (index >= delegate.size() + formulas.length) {
          throw new NoSuchElementException();
        }
        return get(index++);
      }
      
      @Override
      public void remove() {
      }
    };
  }
  
  @Override
  public int size() {
    return delegate.size() + formulas.length;
  }
  
  @Override
  public Vector subset(int... indices) throws IllegalArgumentException {
    Value[] toReturn = new Value[indices.length];
    for (int i = 0; i < indices.length; i++) {
      toReturn[i] = get(indices[i]);
    }
    return new DefaultVector(toReturn);
  }

  @Override
  public double product(Vector other) {
    return Vectors.product(this, other);
  }

  @Override
  public Vector sum(Vector other) {
    return Vectors.sum(this, other);
  }

  @Override
  public Value[] toArray() {
    Value[] values = new Value[size()];
    for (int i = 0; i < values.length; i++) {
      values[i] = get(i);
    }
    return values;
  }
}

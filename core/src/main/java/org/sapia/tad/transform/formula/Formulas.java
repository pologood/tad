package org.sapia.tad.transform.formula;


import org.sapia.tad.Column;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datatype;
import org.sapia.tad.TadContext;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultColumnSet;
import org.sapia.tad.util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The formulas to hold.
 */
public class Formulas {

  private TadContext context;

  public Formulas(TadContext context) {
    this.context = context;
  }

  /**
   * @param dataset the {@link Dataset} to which to add formulas.
   * @param formulas {@link Tuple} instances holding: a) the {@link Datatype} of the formula's result; 
   * b) the column name of the formula; c) the {@link Formula} itself.
   * 
   * @return a new {@link Dataset}, with new columns added for the given formulas.
   */
  @Doc("returns a dataset that adds the given formulas to the provided dataset")
  public Dataset addFormulas(
      @Doc("a dataset to which to add formulas") Dataset dataset, 
      @Doc("one or more tuples, each holding: " + 
       "a) a formula column name; b) a formula column type; " + 
       "c) the ArgFunction instance corresponding to the formula") Tuple...formulas) {
    return addFormulas(dataset, Arrays.asList(formulas));
  }
  
  /**
   * @param dataset the {@link Dataset} to which to add formulas.
   * @param formulas {@link Tuple} instances holding: a) the {@link Datatype} of the formula's result; 
   * b) the column name of the formula; c) the {@link Formula} itself.
   * 
   * @return a new {@link Dataset}, with new columns added for the given formulas.
   */
  @SuppressWarnings("unchecked")
  @Doc("returns a dataset that adds the given formulas to the provided dataset")
  public Dataset addFormulas(
      @Doc("a dataset to which to add formulas") Dataset dataset, 
      @Doc("one or more tuples, each holding: " + 
          "a) a formula column name; b) a formula column type; " + 
          "c) the ArgFunction instance corresponding to the formula") List<Tuple> formulas) {
    List<Column>      columns = new ArrayList<>();
    List<FormulaInfo> forms   = new ArrayList<>();
    
    columns.addAll(dataset.getColumnSet().getColumns());
    
    for (int i = 0; i < formulas.size(); i++) {
      Tuple f = formulas.get(i);
      forms.add(new FormulaInfo(dataset.getColumnSet().size() + i, f.get(Formula.class)));
      columns.add(new DefaultColumn(dataset.getColumnSet().size() + i, f.getNotNull(Datatype.class), f.getNotNull(String.class)));
    }
    
    DefaultColumnSet cs = new DefaultColumnSet(columns);
    FormulaDataset   ds = new FormulaDataset(dataset, cs, forms);
    return ds;
  }
}

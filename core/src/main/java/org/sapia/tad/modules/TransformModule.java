package org.sapia.tad.modules;

import org.sapia.tad.TadContext;
import org.sapia.tad.help.Doc;
import org.sapia.tad.transform.filter.Filters;
import org.sapia.tad.transform.formula.Formulas;
import org.sapia.tad.transform.index.Indices;
import org.sapia.tad.transform.join.Joins;
import org.sapia.tad.transform.merge.Merges;
import org.sapia.tad.transform.pivot.Pivots;
import org.sapia.tad.transform.range.Ranges;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.transform.sort.Sorts;
import org.sapia.tad.transform.view.Views;

/**
 * Holds the transformation sub-modules.
 *
 * @author yduchesne
 */
@Doc("Groups modules pertaining to dataset transformation")
public class TransformModule {

  public final Filters  filters;
  public final Formulas formulas;
  public final Indices  indexes;
  public final Joins    joins;
  public final Merges   merges;
  public final Pivots   pivots;
  public final Ranges   ranges;
  public final Slices   slices;
  public final Sorts    sorts;
  public final Views    views;

  public TransformModule(TadContext context) {
    filters  = new Filters(context);
    formulas = new Formulas(context);
    indexes  = new Indices(context);
    joins    = new Joins(context);
    merges   = new Merges(context);
    pivots   = new Pivots(context);
    ranges   = new Ranges(context);
    slices   = new Slices(context);
    sorts    = new Sorts(context);
    views    = new Views(context);
  }
}

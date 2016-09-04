package org.sapia.tad.transform.pivot;

import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.conf.Conf;
import org.sapia.tad.func.NoArgFunction;
import org.sapia.tad.impl.IndexDatasetAdapter;
import org.sapia.tad.io.table.Header;
import org.sapia.tad.io.table.Row;
import org.sapia.tad.io.table.Table;
import org.sapia.tad.util.Tree;
import org.sapia.tad.util.Tree.Node;
import org.sapia.tad.value.Value;

import java.util.*;

class PivotDataset extends IndexDatasetAdapter {
  
  PivotDataset(Index index) {
    super(index);
  }
  
  @Override
  public String toString() {
    
    Tree<Key, RowSet> datatree = new Tree<>(new NoArgFunction<Map<Key, Node<Key, RowSet>>>() {
      @Override
      public Map<Key, Node<Key, RowSet>> call() {
        return new TreeMap<PivotDataset.Key, Tree.Node<Key, RowSet>>();
      }
    });
    
    Collection<VectorKey> keys = getKeys();
    for (VectorKey key : keys) {
      List<Key> path = new ArrayList<>(key.size());
      for (int i = 0; i < key.size(); i++) {
        path.add(new Key(key.getColumnSet().get(i), key.get(i)));
      }
      datatree.bind(path, getRowset(key));
    }
    
    final Table table = new Table();
    
    table.fill(getIndexedColumnSet().size(), Conf.getCellWidth());
    for (int i = 0; i < getIndexedColumnSet().size() - 1; i++) {
      Header h = table.getHeader(getIndexedColumnSet().get(i).getIndex());
      h.getStyle().separator(" ").alignLeft();
    }
    table.getHeader(getIndexedColumnSet().get(getIndexedColumnSet().size() - 1).getIndex()).getStyle().alignLeft();
    
    for (Column col : getColumnSet().excludes(getIndexedColumnSet().getColumnNames())) {
      Header h = table.header(col.getName(), Conf.getCellWidth());
      h.getStyle().alignRight();
    }
    
    datatree.acceptBreadthFirst(new Tree.Visitor<PivotDataset.Key, RowSet>() {
      public void visit(Node<Key, RowSet> node) {
        if (node.getLevel() > 0) {
          if (!node.isLeaf()) {
            Column col = node.getKey().column;
            table.row().fill(node.getLevel() - 1).cell(col.getFormat().formatValue(col.getType(), node.getKey().value).trim()).fill();
          } else {
            Row r = table.row().fill(node.getLevel() - 1);
            Column pivotCol = node.getKey().column;
            r.cell(pivotCol.getFormat().formatValue(pivotCol.getType(), node.getKey().value).trim());
            ColumnSet summaryColumns = getColumnSet().excludes(getIndexedColumnSet().getColumnNames());
            Vector sum = RowSets.sum(summaryColumns.get(0).getIndex(), node.getValue());
            
            for (Column col : summaryColumns) {
              r.cell(col.getFormat().formatValue(col.getType(), sum.get(col.getIndex())));
            }
          }
        }
      }
    });
    return table.toString();
  }

  
  private static class Key implements Comparable<Key> {
    
    private Column column;
    private Value  value;
    
    private Key(Column column, Value value) {
      this.column = column;
      this.value  = value;
    }

    @Override
    public String toString() {
      return column.getName() + ":" + value;
    }

    @Override
    public int compareTo(Key other) {
      return column.getType().strategy().compareTo(value, other.value);
    }
    
  }
}

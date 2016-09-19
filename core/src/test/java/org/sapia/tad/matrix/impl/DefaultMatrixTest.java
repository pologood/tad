package org.sapia.tad.matrix.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.sapia.tad.Tad;
import org.sapia.tad.TestTad;
import org.sapia.tad.matrix.Dimensions;
import org.sapia.tad.matrix.Matrix;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.matrix.impl.DefaultMatrix;
import org.sapia.tad.util.Data;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author yduchesne
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultMatrixTest {

  private DefaultMatrix matrix;

  private Tad tad;

  @Before
  public void setUp() throws Exception {
    tad = TestTad.get();
    List<Vector> rows = Data.list(
            Vectors.withNumbers(2, 4, 6),
            Vectors.withNumbers(3, 6, 9),
            Vectors.withNumbers(5, 10, 15)
    );
    matrix = new DefaultMatrix(tad.context, 3, rows);
  }

  @Test
  public void testGetColumn() throws Exception {
    Vector column0 = matrix.getColumn(0);
    Vector column1 = matrix.getColumn(1);
    Vector column2 = matrix.getColumn(2);

    assertThat(column0).isEqualTo(Vectors.withNumbers(2, 3, 5));
    assertThat(column1).isEqualTo(Vectors.withNumbers(4, 6, 10));
    assertThat(column2).isEqualTo(Vectors.withNumbers(6, 9, 15));
  }

  @Test
  public void testGetColumn_vector() throws Exception {
    Vector column0 = matrix.getColumn(0);
    Vector column1 = matrix.getColumn(1);
    Vector column2 = matrix.getColumn(2);

    assertThat(column0.size()).isEqualTo(3);
    assertThat(column0.get(0).get()).isEqualTo(2);
    assertThat(column0.get(1).get()).isEqualTo(3);
    assertThat(column0.get(2).get()).isEqualTo(5);

    assertThat(column1.size()).isEqualTo(3);
    assertThat(column1.get(0).get()).isEqualTo(4);
    assertThat(column1.get(1).get()).isEqualTo(6);
    assertThat(column1.get(2).get()).isEqualTo(10);

    assertThat(column2.size()).isEqualTo(3);
    assertThat(column2.get(0).get()).isEqualTo(6);
    assertThat(column2.get(1).get()).isEqualTo(9);
    assertThat(column2.get(2).get()).isEqualTo(15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetColumn_vector_illegal_value_index() throws Exception {
    matrix.getColumn(0).get(3);
  }

  @Test
  public void testGetColumn_single_column_matrix() {
    List<Vector> rows = Data.list(
            Vectors.withNumbers(2),
            Vectors.withNumbers(3),
            Vectors.withNumbers(5)
    );
    matrix = new DefaultMatrix(tad.context, 1, rows);

    Vector column0 = matrix.getColumn(0);

    assertThat(column0.size()).isEqualTo(3);
    assertThat(column0.get(0).get()).isEqualTo(2);
    assertThat(column0.get(1).get()).isEqualTo(3);
    assertThat(column0.get(2).get()).isEqualTo(5);
  }

  @Test
  public void testGetRow() throws Exception {
    Vector row0 = matrix.getRow(0);
    Vector row1 = matrix.getRow(1);
    Vector row2 = matrix.getRow(2);

    assertThat(row0).isEqualTo(Vectors.withNumbers(2, 4, 6));
    assertThat(row1).isEqualTo(Vectors.withNumbers(3, 6, 9));
    assertThat(row2).isEqualTo(Vectors.withNumbers(5, 10, 15));
  }

  @Test
  public void testGetDimensions_column_count() throws Exception {
    assertThat(matrix.getDimensions().getColumnCount()).isEqualTo(3);
  }

  @Test
  public void testGetDimensions_row_count() throws Exception {
    assertThat(matrix.getDimensions().getRowCount()).isEqualTo(3);
  }
}
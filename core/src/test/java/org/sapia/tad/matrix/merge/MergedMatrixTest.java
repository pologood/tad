package org.sapia.tad.matrix.merge;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Tad;
import org.sapia.tad.TestTad;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.matrix.Dimensions;
import org.sapia.tad.matrix.Matrix;
import org.sapia.tad.matrix.impl.DefaultMatrix;
import org.sapia.tad.util.Data;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @author yduchesne
 */
public class MergedMatrixTest {

  private Matrix matrix;

  private Tad tad;

  @Before
  public void setUp() throws Exception {
    tad = TestTad.get();

    List<Vector> m1Rows = Data.list(
            Vectors.withNumbers(2),
            Vectors.withNumbers(3),
            Vectors.withNumbers(5)
    );
    Matrix m1 = new DefaultMatrix(tad.context, 1, m1Rows);

    List<Vector> m2Rows = Data.list(
            Vectors.withNumbers(4),
            Vectors.withNumbers(6),
            Vectors.withNumbers(10)
    );
    Matrix m2 = new DefaultMatrix(tad.context, 1, m2Rows);

    List<Vector> m3Rows = Data.list(
            Vectors.withNumbers(6),
            Vectors.withNumbers(9),
            Vectors.withNumbers(15)
    );
    Matrix m3 = new DefaultMatrix(tad.context, 1, m3Rows);

    matrix = new MergedMatrix(tad.context, Arrays.asList(m1, m2, m3));
  }

  @Test
  public void testGetDimensions() throws Exception {
    assertThat(matrix.getDimensions()).isEqualTo(new Dimensions(3, 3));
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

  @Test(expected = IllegalArgumentException.class)
  public void testNew_with_invalid_rowcount() {
    List<Vector> m1Rows = Data.list(
            Vectors.withNumbers(2),
            Vectors.withNumbers(3),
            Vectors.withNumbers(5)
    );
    Matrix m1 = new DefaultMatrix(tad.context, 1, m1Rows);

    List<Vector> m2Rows = Data.list(
            Vectors.withNumbers(4),
            Vectors.withNumbers(6)
    );
    Matrix m2 = new DefaultMatrix(tad.context, 1, m2Rows);

    new MergedMatrix(tad.context, Arrays.asList(m1, m2));
  }
}
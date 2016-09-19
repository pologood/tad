package org.sapia.tad.matrix;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.*;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.matrix.impl.DefaultMatrix;
import org.sapia.tad.transform.range.Ranges;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author yduchesne
 */
public class MatricesTest {

  private Tad      tad;
  private Matrices matrices;
  private Matrix   matrix;

  @Before
  public void setUp() {
    tad = TestTad.get();
    matrices = new Matrices(tad.context);

    tad = TestTad.get();
    List<Vector> rows = Data.list(
            Vectors.withNumbers(2, 4, 6),
            Vectors.withNumbers(3, 6, 9),
            Vectors.withNumbers(5, 10, 15)
    );
    matrix = new DefaultMatrix(tad.context, 3, rows);
  }

  @Test
  public void testWithDataset() throws Exception {
    List<Vector> rows = Data.list(
            Vectors.withNumbers(2, 4, 6),
            Vectors.withNumbers(3, 6, 9)
    );

    Dataset dataset = new DefaultDataset(
            Datasets.columns()
                    .column("col0", Datatype.NUMERIC)
                    .column("col1", Datatype.NUMERIC)
                    .column("col2", Datatype.NUMERIC)
                    .build(),
            rows
    );

    Matrix matrix = matrices.withDataset(dataset);

    assertThat(matrix.getDimensions().getRowCount()).isEqualTo(2);
    assertThat(matrix.getDimensions().getColumnCount()).isEqualTo(3);
  }

  @Test
  public void testWithRow() {
    Vector row    = Vectors.withNumbers(0, 1, 2);
    Matrix matrix = matrices.withRow(row);

    assertThat(matrix.getDimensions().getRowCount()).isEqualTo(1);
    assertThat(matrix.getDimensions().getColumnCount()).isEqualTo(3);

    for (int i : Numbers.range(0, 3)) {
      assertThat(matrix.getRow(0).get(i)).isEqualTo(NumericValue.of(i));
    }
  }

  @Test
  public void testWithColumn() {
    Vector row    = Vectors.withNumbers(0, 1, 2);
    Matrix matrix = matrices.withColumn(row);

    assertThat(matrix.getDimensions().getRowCount()).isEqualTo(3);
    assertThat(matrix.getDimensions().getColumnCount()).isEqualTo(1);

    for (int i : Numbers.range(0, 3)) {
      assertThat(matrix.getRow(i).get(0)).isEqualTo(NumericValue.of(i));
    }
  }

  @Test
  public void testProduct_with_value() throws Exception {
    Matrix result = matrix.product(NumericValue.of(2));
    for (int i = 0; i < result.getDimensions().getRowCount(); i++) {
      Vector row = result.getRow(i);
      Vector otherRow = matrix.getRow(i);
      for (int j = 0; j < result.getDimensions().getColumnCount(); j++) {
        Value value = row.get(j);
        Value otherValue = otherRow.get(j);
        assertThat(value.get()).isEqualTo(otherValue.get() * 2);
      }
    }
  }

  @Test
  public void testProduct_with_vector() throws Exception {
    Vector v = Vectors.withNumbers(10, 100, 1000);
    Vector result = matrix.product(v);

    assertThat(result.size()).isEqualTo(3);
    assertThat(result.get(0).get()).isEqualTo(2 * 10 + 4 * 100 + 6 * 1000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProduct_with_vector_invalid() throws Exception {
    Vector v = Vectors.withNumbers(10, 100);
    matrix.product(v);
  }

  @Test
  public void testProduct_with_matrix() throws Exception {
    List<Vector> rows = Data.list(
            Vectors.withNumbers(10),
            Vectors.withNumbers(100),
            Vectors.withNumbers(1000)
    );
    Matrix other = new DefaultMatrix(tad.context, 1, rows);

    Matrix result = matrix.product(other);

    assertThat(result.getDimensions().getColumnCount()).isEqualTo(1);
    assertThat(result.getDimensions().getRowCount()).isEqualTo(3);
    assertThat(result.getColumn(0).get(0).get()).isEqualTo(2 * 10 + 4 * 100 + 6 * 1000);
    assertThat(result.getColumn(0).get(1).get()).isEqualTo(3 * 10 + 6 * 100 + 9 * 1000);
    assertThat(result.getColumn(0).get(2).get()).isEqualTo(5 * 10 + 10 * 100 + 15 * 1000);
  }

  @Test
  public void testSum() throws Exception {
    Matrix result = matrix.sum(matrix);

    Vector row0 = result.getRow(0);
    Vector row1 = result.getRow(1);
    Vector row2 = result.getRow(2);

    assertThat(row0).isEqualTo(Vectors.withNumbers(4, 8, 12));
    assertThat(row1).isEqualTo(Vectors.withNumbers(6, 12, 18));
    assertThat(row2).isEqualTo(Vectors.withNumbers(10, 20, 30));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSum_with_matrix_invalid_row_count() throws Exception {
    Matrix other = mock(Matrix.class);

    Dimensions dims = new Dimensions(10, 3);
    when(other.getDimensions()).thenReturn(dims);

    matrix.sum(other);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSum_with_matrix_invalid_column_count() throws Exception {
    Matrix other = mock(Matrix.class);

    Dimensions dims = new Dimensions(3, 10);
    when(other.getDimensions()).thenReturn(dims);

    matrix.sum(other);
  }

  @Test
  public void testTranspose_get_row() {
    Matrix tp = matrix.transpose();

    Vector row0 = tp.getRow(0);
    Vector row1 = tp.getRow(1);
    Vector row2 = tp.getRow(2);

    assertThat(row0).isEqualTo(Vectors.withNumbers(2, 3, 5));
    assertThat(row1).isEqualTo(Vectors.withNumbers(4, 6, 10));
    assertThat(row2).isEqualTo(Vectors.withNumbers(6, 9, 15));
  }

  @Test
  public void testTranspose_dimensions() {
    List<Vector> rows = Data.list(
            Vectors.withNumbers(2, 4),
            Vectors.withNumbers(3, 6),
            Vectors.withNumbers(5, 10)
    );
    matrix = new DefaultMatrix(tad.context, 2, rows);

    Matrix tp = matrix.transpose();

    assertThat(tp.getDimensions().getColumnCount()).isEqualTo(3);
    assertThat(tp.getDimensions().getRowCount()).isEqualTo(2);
  }
}
package org.sapia.tad.type;

import org.sapia.tad.Datatype;
import org.sapia.tad.DatatypeStrategy;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.Date;

public class NumericDatatypeStrategy implements DatatypeStrategy {

  @Override
  public boolean isAssignableFrom(Object value) {
    Checks.isFalse(value instanceof Value, "This method does not accept Value instances: invoke to isType() method of this instance to perform tests using Value instances");
    return value instanceof Number || NullValue.isNull(value);
  }

  @Override
  public boolean isType(Value value) {
    return value.isNumeric() || value instanceof NumericValue;
  }

  @Override
  public Value getValueFor(Object obj) {
    Checks.isTrue(obj instanceof  Number, "Passed in object is not a %s instance: %s", Number.class, obj.getClass().getName());
    if (NullValue.isNull(obj)) {
      return NullValue.getInstance();
    }
    return new NumericValue(((Number) obj).doubleValue());
  }

  @Override
  public Object add(Object currentValue, Object toAdd) {
    if (NullValue.isNull(toAdd)) {
      return currentValue;
    } else if (NullValue.isNull(currentValue)) {
      if (isAssignableFrom(toAdd)) {
        return ((Number) toAdd).doubleValue();
      } else if (Datatype.STRING.strategy().isAssignableFrom(toAdd)) {
        String toAddStr = (String) toAdd;
        try {
          Double toAddDouble = Double.parseDouble(toAddStr);
          return toAddDouble; 
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException(String.format("Cannot add %s to %s", toAddStr, currentValue));
        }
      } else if (Datatype.DATE.strategy().isAssignableFrom(toAdd)) {
        Date toAddDate = (Date) toAdd;
        return toAddDate.getTime();
      } else {
        throw new IllegalArgumentException(String.format("Cannot convert %s to type %s", toAdd, Datatype.NUMERIC));
      }
    } else if (isAssignableFrom(toAdd)) {
      return new Double(((Number) currentValue).doubleValue() + ((Number) toAdd).doubleValue());
    } else if (Datatype.STRING.strategy().isAssignableFrom(toAdd)) {
      String toAddStr = (String) toAdd;
      try {
        Double toAddDouble = Double.parseDouble(toAddStr);
        return new Double(((Number) currentValue).doubleValue() + toAddDouble);        
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(String.format("Cannot add %s to %s", toAddStr, currentValue));
      }
    } else if (Datatype.DATE.strategy().isAssignableFrom(toAdd)) {
      Date toAddDate = (Date) toAdd;
      return new Double(((Number) currentValue).doubleValue() + toAddDate.getTime());
    } else {
      throw new IllegalArgumentException(String.format("Cannot add %s to %s", toAdd, currentValue));
    }
  }

  @Override
  public int compareTo(Value value, Value operand) {
    Checks.isTrue(isType(value) || NullValue.isNull(value) || value.isNumeric(), "Value not numeric: %s. Cannot be compared with %s", value, operand);
    Checks.isTrue(isType(operand) || NullValue.isNull(operand) || value.isNumeric(), "Value not numeric: %s. Cannot be compared with %s", operand, value);
    if (NullValue.isNull(value) && NullValue.isNull(operand)) {
      return 0;
    } else if (NullValue.isNull(value)) {
      return -1;
    } else if (NullValue.isNull(operand)) {
      return 1;
    } else {
      if (value.get() < operand.get()) {
        return -1;
      } else if (value.get() > operand.get()) {
        return 1;
      } else {
        return 0;
      }
    }
  }
}

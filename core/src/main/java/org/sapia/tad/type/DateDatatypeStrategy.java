package org.sapia.tad.type;

import org.sapia.tad.Datatype;
import org.sapia.tad.DatatypeStrategy;
import org.sapia.tad.parser.DateParser;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.DateValue;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.Value;

import java.util.Date;

public class DateDatatypeStrategy implements DatatypeStrategy {
  
  private static DateParser PARSER = new DateParser();
  
  @Override
  public boolean isAssignableFrom(Object value) {
    Checks.isFalse(value instanceof Value, "This method does not accept Value instances: invoke to isType() method of this instance to perform tests using Value instances");
    return value instanceof Date || NullValue.isNull(value);
  }

  @Override
  public boolean isType(Value value) {
    return value instanceof DateDatatypeStrategy;
  }

  @Override
  public Value getValueFor(Object obj) {
    Checks.isTrue(obj instanceof  Date, "Passed in object is not a %s instance: %s", Date.class, obj.getClass().getName());
    if (NullValue.isNull(obj)) {
      return NullValue.getInstance();
    }
    return new DateValue((Date) obj);
  }

  @Override
  public Object add(Object currentValue, Object toAdd) {
    if (NullValue.isNull(toAdd)) {
      return currentValue;
    } else if (NullValue.isNull(currentValue)) {
      if (isAssignableFrom(toAdd)) {
        return ((Date) toAdd).getTime();
      } else if (Datatype.STRING.strategy().isAssignableFrom(toAdd)) {
        String toAddStr = (String) toAdd;
        Date toAddDate = (Date) PARSER.parse(toAddStr);
        return toAddDate;
      } else if (Datatype.NUMERIC.strategy().isAssignableFrom(toAdd)) {
        Number toAddNumber = (Number) toAdd;
        return toAddNumber;
      } else {
        throw new IllegalArgumentException(String.format("Cannot convert %s to type %s", toAdd, Datatype.NUMERIC));
      }
    } else if (isAssignableFrom(toAdd)) {
      return new Date(((Date) currentValue).getTime() + ((Date) toAdd).getTime());
    } else if (Datatype.STRING.strategy().isAssignableFrom(toAdd)) {
      String toAddStr = (String) toAdd;
      Date toAddDate = (Date) PARSER.parse(toAddStr);
      return new Date(((Date) currentValue).getTime() + toAddDate.getTime());
    } else if (Datatype.NUMERIC.strategy().isAssignableFrom(toAdd)) {
      Number toAddNumber = (Number) toAdd;
      return new Date(((Date) currentValue).getTime() + toAddNumber.longValue());
    } else {
      throw new IllegalArgumentException(String.format("Cannot add %s to %s", toAdd, currentValue));
    }
  }
  
  public int compareTo(Value value, Value operand) {
    Checks.isTrue(isType(operand) || NullValue.isNull(operand), "Date %s cannot be compared with %s", value, operand);
    if (NullValue.isNull(value) && NullValue.isNull(operand)) {
      return 0;
    } else {
      Date dateValue = (Date) value;
      Date dateOperand = (Date) operand;
      return dateValue.compareTo(dateOperand);
    }
  }

}

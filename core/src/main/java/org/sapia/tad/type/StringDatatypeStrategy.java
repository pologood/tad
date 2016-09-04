package org.sapia.tad.type;

import org.sapia.tad.Datatype;
import org.sapia.tad.DatatypeStrategy;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.StringValue;
import org.sapia.tad.value.Value;

import java.util.Date;

public class StringDatatypeStrategy implements DatatypeStrategy {
 
  public StringDatatypeStrategy() {
  }
  
  @Override
  public boolean isAssignableFrom(Object value) {
    Checks.isFalse(value instanceof Value, "This method does not accept Value instances: invoke to isType() method of this instance to perform tests using Value instances");
    return value instanceof String || NullValue.isNull(value);
  }

  @Override
  public boolean isType(Value value) {
    return value instanceof StringValue;
  }

  @Override
  public Value getValueFor(Object obj) {
    Checks.isTrue(obj instanceof String, "Passed in object is not a %s instance: %s", String.class, obj.getClass().getName());
    if (NullValue.isNull(obj)) {
     return NullValue.getInstance();
    }
    return new StringValue((String) obj);
  }

  @Override
  public Object add(Object currentValue, Object toAdd) {
    if (NullValue.isNull(toAdd)) {
      return currentValue;
    } else if (NullValue.isNull(currentValue)) {
      if (isAssignableFrom(toAdd)) {
        return (String) toAdd ;
      } else if (Datatype.NUMERIC.strategy().isAssignableFrom(toAdd)) {
        return ((Number) toAdd).toString() ;
      } else if (Datatype.DATE.strategy().isAssignableFrom(toAdd)) {
        return ((Date) toAdd).toString();
      } else {
        throw new IllegalArgumentException(String.format("Cannot convert %s to type %s", toAdd, Datatype.NUMERIC));
      } 
    } else if (isAssignableFrom(toAdd)) {
      return ((String) currentValue) + ((String) toAdd) ;
    } else if (Datatype.NUMERIC.strategy().isAssignableFrom(toAdd)) {
      return ((String) currentValue) + ((Number) toAdd).toString() ;
    } else if (Datatype.DATE.strategy().isAssignableFrom(toAdd)) {
      String toAddStr = ((Date) toAdd).toString();
      return ((String) currentValue) + toAddStr;
    } else {
      throw new IllegalArgumentException(String.format("Cannot add %s to %s", toAdd, currentValue));
    }
  }

  @Override
  public int compareTo(Value value, Value operand) {
    Checks.isTrue(isType(operand) || NullValue.isNull(operand), "String %s cannot be compared with %s", value, operand);
    if (NullValue.isNull(value) && NullValue.isNull(operand)) {
      return 0;
    } else if (NullValue.isNull(value)) {
      return -1;
    } else if (NullValue.isNull(operand)) {
      return 1;
    } else {
      StringValue strValue =   (StringValue) value;
      StringValue strOperand = (StringValue) operand;
      return strValue.getValue().compareTo(strOperand.getValue());
    } 
  }
}

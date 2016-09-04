package org.sapia.tad.impl;

import org.sapia.tad.Column;
import org.sapia.tad.Datatype;
import org.sapia.tad.Nominal;
import org.sapia.tad.NominalSet;
import org.sapia.tad.format.DefaultFormat;
import org.sapia.tad.format.Format;
import org.sapia.tad.parser.Parser;
import org.sapia.tad.parser.Parsers;
import org.sapia.tad.util.Objects;

/**
 * Default implementation of the {@link Column} interface.
 * 
 * @author yduchesne
 *
 */
public class DefaultColumn implements Column {
 
  private NominalSet  nominalValues;
  private Parser      parser;
  private Format      format = DefaultFormat.getInstance();
  private int         index;
  private Datatype    type;
  private String      name;
 
  public DefaultColumn(NominalSet nominalValues, int index, Datatype type, String name) {
    this.nominalValues = nominalValues;
    this.index         = index;
    this.name          = name;
    this.type          = type;
    this.parser        = Parsers.getParserFor(type);
  }
  
  public DefaultColumn(int index, Datatype type, String name) {
    this(NominalSet.newInstance(new Nominal[0]), index, type, name);
  }
  
  @Override
  public NominalSet getNominalValues() {
    return nominalValues;
  }
  
  @Override
  public Parser getParser() {
    return parser;
  }
  
  @Override
  public void setParser(Parser parser) {
    this.parser = parser;
  }
  
  @Override
  public Format getFormat() {
    return format;
  }
  
  @Override
  public void setFormat(Format format) {
    this.format = format;
  }
  
  @Override
  public int getIndex() {
    return index;
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public Datatype getType() {
    return type;
  }
  
  @Override
  public Column copy(int newIndex) {
    DefaultColumn copy = new DefaultColumn(newIndex, type, name);
    copy.setFormat(format);
    copy.setParser(parser);
    return copy;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Column) {
      Column other = (Column) obj;
      return 
          Objects.safeEquals(name, other.getName()) 
          && Objects.safeEquals(type, other.getType());
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.safeHashCode(name, type);
  }

}

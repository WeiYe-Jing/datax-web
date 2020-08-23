package com.wugui.datatx.core.sql;


import com.wugui.datatx.core.enums.DataType;
import com.wugui.datatx.core.enums.Direct;

import java.io.Serializable;
import java.util.Objects;

public class Property implements Serializable {
  /**
   * key
   */
  private String prop;

  /**
   * input/output
   */
  private Direct direct;

  /**
   * data type
   */
  private DataType type;

  /**
   * value
   */
  private String value;

  public Property() {
  }

  public Property(String prop, Direct direct, DataType type, String value) {
    this.prop = prop;
    this.direct = direct;
    this.type = type;
    this.value = value;
  }

  /**
   * getter method
   *
   * @return the prop
   * @see Property#prop
   */
  public String getProp() {
    return prop;
  }

  /**
   * setter method
   *
   * @param prop the prop to set
   * @see Property#prop
   */
  public void setProp(String prop) {
    this.prop = prop;
  }

  /**
   * getter method
   *
   * @return the value
   * @see Property#value
   */
  public String getValue() {
    return value;
  }

  /**
   * setter method
   *
   * @param value the value to set
   * @see Property#value
   */
  public void setValue(String value) {
    this.value = value;
  }


  public Direct getDirect() {
    return direct;
  }

  public void setDirect(Direct direct) {
    this.direct = direct;
  }

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    Property property = (Property) o;
    return Objects.equals(prop, property.prop) &&
            Objects.equals(value, property.value);
  }


  @Override
  public int hashCode() {
    return Objects.hash(prop, value);
  }

  @Override
  public String toString() {
    return "Property{" +
            "prop='" + prop + '\'' +
            ", direct=" + direct +
            ", type=" + type +
            ", value='" + value + '\'' +
            '}';
  }


}

package com.wugui.datatx.core.sql;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * job params related class
 */
public abstract class AbstractParameters implements IParameters {

  @Override
  public abstract boolean checkParameters();


  /**
   * local parameters
   */
  public List<Property> localParams;

  /**
   * get local parameters list
   * @return Property list
   */
  public List<Property> getLocalParams() {
    return localParams;
  }

  public void setLocalParams(List<Property> localParams) {
    this.localParams = localParams;
  }

  /**
   * get local parameters map
   * @return parameters map
   */
  public Map<String,Property> getLocalParametersMap() {
      if (localParams != null) {
        Map<String,Property> localParametersMaps = new LinkedHashMap<>();

        for (Property property : localParams) {
          localParametersMaps.put(property.getProp(),property);
        }
        return localParametersMaps;
      }
      return null;
  }

}

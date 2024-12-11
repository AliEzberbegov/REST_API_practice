package org.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDataPojo {

    private String name;
    private String type;
    private Boolean exotic;

    public ProductDataPojo() {}

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Boolean getExotic() {
        return exotic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExotic(Boolean exotic) {
        this.exotic = exotic;
    }
}

package com.example.pfm.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String type; // INCOME or EXPENSE

    private boolean isCustom;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, String type, boolean isCustom) {
        this.name = name;
        this.type = type;
        this.isCustom = isCustom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }
}

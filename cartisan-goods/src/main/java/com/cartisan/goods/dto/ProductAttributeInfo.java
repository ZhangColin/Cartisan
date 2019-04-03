package com.cartisan.goods.dto;

import lombok.Data;

/**
 * @author colin
 */
@Data
public class ProductAttributeInfo {
    private Long attributeCategoryId;
    private String attributeCategoryName;
    private Long attributeId;
    private String attributeName;
}
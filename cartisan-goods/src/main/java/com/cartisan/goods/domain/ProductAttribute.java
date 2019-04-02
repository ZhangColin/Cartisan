package com.cartisan.goods.domain;

import com.cartisan.common.domains.AbstractEntity;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @author colin
 */
@Entity
@Table(name = "goods_product_attributes")
@Where(clause = "active=1 and deleted=0")
@Data
public class ProductAttribute extends AbstractEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name")
    private String name;
    @Column(name = "select_type")
    private Integer selectType;
    @Column(name = "input_type")
    private Integer inputType;
    @Column(name = "input_list")
    private String inputList;
    @Column(name = "filter_type")
    private Integer filterType;
    @Column(name = "search_type")
    private Integer searchType;
    @Column(name = "related")
    private Boolean related;
    @Column(name = "hand_add")
    private Boolean handAdd;
    @Column(name = "type")
    private Integer type;
    @Column(name = "sort")
    private Integer sort;
}

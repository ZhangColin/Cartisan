package com.cartisan.goods.service;

import com.cartisan.common.dto.PageResult;
import com.cartisan.goods.domain.AttributeType;
import com.cartisan.goods.domain.ProductAttribute;
import com.cartisan.goods.dto.ProductAttributeDto;
import com.cartisan.goods.repository.ProductAttributeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author colin
 */
@Service
public class ProductAttributeService {
    @Autowired
    private ProductAttributeRepository repository;
    @Autowired
    private ProductAttributeCategoryService categoryService;

    public ProductAttributeDto getProductAttribute(Long id) {
        return ProductAttributeDto.convertFrom(repository.findById(id).get());
    }

    public PageResult<ProductAttributeDto> searchProductAttributes(Long categoryId, Integer type, Integer currentPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize,
                new Sort(Sort.Direction.DESC, "sort"));

        final Page<ProductAttribute> searchResult = repository.findAll((Specification<ProductAttribute>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            predicateList.add(criteriaBuilder.equal(root.get("categoryId"), categoryId));
            predicateList.add(criteriaBuilder.equal(root.get("type"), type));

            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageRequest);

        return new PageResult<>(searchResult.getTotalElements(),searchResult.getTotalPages(),
                searchResult.map(ProductAttributeDto::convertFrom).getContent());
    }

    @Transactional(rollbackOn = Exception.class)
    public void addProductAttribute(ProductAttributeDto productAttributeDto) {
        final ProductAttribute productAttribute = new ProductAttribute();
        BeanUtils.copyProperties(productAttributeDto, productAttribute);

        repository.save(productAttribute);

        categoryService.attributeIncrement(productAttribute.getCategoryId(),
                AttributeType.valueOf(productAttribute.getType()));

    }

    @Transactional(rollbackOn = Exception.class)
    public void editProductAttribute(Long id, ProductAttributeDto productAttributeDto) {
        final ProductAttribute productAttribute = repository.findById(id).get();
        BeanUtils.copyProperties(productAttributeDto, productAttribute);
        productAttribute.setId(id);

        repository.save(productAttribute);
    }

    @Transactional(rollbackOn = Exception.class)
    public void removeProductAttribute(long id) {
        final ProductAttribute productAttribute = repository.getOne(id);
        repository.delete(productAttribute);

        categoryService.attributeDecrement(productAttribute.getCategoryId(), AttributeType.valueOf(productAttribute.getType()));
    }


}

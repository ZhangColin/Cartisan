package com.cartisan.goods.controller;

import com.cartisan.common.dto.PageResult;
import com.cartisan.common.response.GenericResponse;
import com.cartisan.goods.dto.ProductAttributeCategoryDto;
import com.cartisan.goods.dto.ProductAttributeCategoryInfo;
import com.cartisan.goods.query.ProductAttributeQuery;
import com.cartisan.goods.service.ProductAttributeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cartisan.common.response.ResponseUtils.success;

/**
 * @author colin
 */
@Api(tags = "ProductAttributeCategoryController", description = "商品属性分类管理")
@RestController
@RequestMapping("/productAttributeCategories")
public class ProductAttributeCategoryController {
    @Autowired
    private ProductAttributeCategoryService service;
    @Autowired
    private ProductAttributeQuery query;

    @ApiOperation(value = "获取所有商品属性分类")
    @GetMapping
    public GenericResponse<List<ProductAttributeCategoryDto>> getAllProductAttributeCategories() {
        return success(service.getAllProductAttributeCategories());
    }

    @ApiOperation(value = "获取所有商品属性分类的参数设置")
    @GetMapping("/params")
    public GenericResponse<List<ProductAttributeCategoryInfo>> findAllParams() {
        return success(query.findAllParams());
    }

    @ApiOperation(value = "获取商品属性分类")
    @GetMapping("/{id}")
    public GenericResponse<ProductAttributeCategoryDto> getProductAttributeCategory(
            @ApiParam(value = "分类Id", required = true) @PathVariable Long id) {
        return success(service.getProductAttributeCategory(id));
    }

    @ApiOperation(value = "分页获取所有商品属性分类")
    @GetMapping("/search/{currentPage}/{pageSize}")
    public GenericResponse<PageResult<ProductAttributeCategoryDto>> searchProductAttributeCategories(
            @ApiParam(value = "页码", required = true) @PathVariable Integer currentPage,
            @ApiParam(value = "每页记录数", required = true) @PathVariable Integer pageSize) {
        return success(service.searchProductAttributeCategories(currentPage, pageSize));
    }

    @ApiOperation(value = "添加商品属性分类")
    @PostMapping
    public GenericResponse addProductAttributeCategory(
            @ApiParam(value = "分类名称", required = true) @RequestParam String name) {
        service.addProductAttributeCategory(name);

        return success();
    }

    @ApiOperation(value = "修改商品属性分类")
    @PutMapping("/{id}")
    public GenericResponse editProductAttributeCategory(
            @ApiParam(value = "分类Id", required = true) @PathVariable Long id,
            @ApiParam(value = "分类名称", required = true) @RequestParam String name) {
        service.editProductAttributeCategory(id, name);

        return success();
    }

    @ApiOperation(value = "删除商品属性分类")
    @DeleteMapping("/{id}")
    public GenericResponse removeProductAttributeCategory(
            @ApiParam(value = "分类Id", required = true) @PathVariable long id) {
        service.removeProductAttributeCategory(id);

        return success();
    }
}
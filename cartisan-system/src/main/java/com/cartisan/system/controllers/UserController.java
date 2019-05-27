package com.cartisan.system.controllers;

import com.cartisan.common.dtos.PageResult;
import com.cartisan.common.responses.GenericResponse;
import com.cartisan.system.dtos.UserDto;
import com.cartisan.system.params.UserParam;
import com.cartisan.system.params.UserSearchParam;
import com.cartisan.system.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.cartisan.common.responses.GenericResponse.success;

/**
 * @author colin
 */
@Api(tags = "UserController")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @ApiOperation(value = "获取用户")
    @GetMapping("/{id}")
    public GenericResponse<UserDto> getUser(
            @ApiParam(value = "用户Id", required = true) @PathVariable Long id) {
        return success(service.getUser(id));
    }

    @ApiOperation(value = "搜索用户")
    @PostMapping("/search/{currentPage}/{pageSize}")
    public GenericResponse<PageResult<UserDto>> searchUsers(
            @ApiParam(value = "查询参数") @RequestBody(required = false) UserSearchParam userSearchParam,
            @ApiParam(value = "页码", required = true) @PathVariable Integer currentPage,
            @ApiParam(value = "每页记录数", required = true) @PathVariable Integer pageSize) {
        return success(service.searchUsers(userSearchParam, currentPage, pageSize));
    }

    @ApiOperation(value = "添加用户")
    @PostMapping
    public GenericResponse addUser(
            @ApiParam(value = "用户信息", required = true) @Validated @RequestBody UserParam userParam) {
        service.addUser(userParam);

        return success();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("/{id}")
    public GenericResponse editUser(
            @ApiParam(value = "用户Id", required = true) @PathVariable Long id,
            @ApiParam(value = "用户信息", required = true) @Validated @RequestBody UserParam userParam) {
        service.editUser(id, userParam);

        return success();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id}")
    public GenericResponse removeUser(
            @ApiParam(value = "用户Id", required = true) @PathVariable long id) {
        service.removeUser(id);

        return success();
    }

    @ApiOperation(value = "冻结用户")
    @PutMapping("/{id}/frozen")
    public GenericResponse frozenUser(
            @ApiParam(value = "用户Id", required = true) @PathVariable Long id) {
        service.frozen(id);

        return success();
    }

    @ApiOperation(value = "解冻用户")
    @PutMapping("/{id}/unFrozen")
    public GenericResponse unFrozenUser(
            @ApiParam(value = "用户Id", required = true) @PathVariable Long id) {
        service.unFrozen(id);

        return success();
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/{id}/password")
    public GenericResponse changePassword(
            @ApiParam(value = "用户Id", required = true) @PathVariable Long id,
            @ApiParam(value = "密码", required = true) @Validated @RequestParam String password) {
        service.changePassword(id, password);

        return success();
    }
}
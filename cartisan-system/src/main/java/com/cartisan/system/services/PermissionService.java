package com.cartisan.system.services;

import com.cartisan.common.exceptions.CartisanException;
import com.cartisan.common.utils.IdWorker;
import com.cartisan.system.domains.Permission;
import com.cartisan.system.dtos.PermissionDto;
import com.cartisan.system.params.PermissionParam;
import com.cartisan.system.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author colin
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionRepository repository;

    @Autowired
    private IdWorker idWorker;

    public List<PermissionDto> getPermissionList() {
        final List<Permission> permissions = repository.findAll(new Sort(Sort.Direction.ASC, "sort"));

        return PermissionDto.buildPermissionTreeList(permissions);
    }


    @Transactional(rollbackOn = Exception.class)
    public void addPermission(PermissionParam permissionParam) {
        if (repository.existsByParentIdAndName(permissionParam.getParentId(), permissionParam.getName())) {
            throw new CartisanException("同一层级下存在相同名称的权限");
        }

        final Permission permission = new Permission(idWorker.nextId(), permissionParam.getParentId(),
                permissionParam.getName(), permissionParam.getCode());

        permission.setDescription(permissionParam.getDescription());
        permission.setSort(permissionParam.getSort());


        permission.setOperator("system");
        permission.setOperateIp("127.0.0.1");

        repository.save(permission);
    }

    @Transactional(rollbackOn = Exception.class)
    public void editPermission(Long id, PermissionParam permissionParam) {
        if (repository.existsByParentIdAndNameAndIdNot(permissionParam.getParentId(), permissionParam.getName(), id)) {
            throw new CartisanException("同一层级下存在相同名称的权限");
        }

        final Optional<Permission> permissionOptional = repository.findById(id);
        if (!permissionOptional.isPresent()) {
            throw new CartisanException("待更新的权限不存在");
        }

        final Permission permission = permissionOptional.get();
        permission.setParentId(permissionParam.getParentId());
        permission.setName(permissionParam.getName());
        permission.setCode(permissionParam.getCode());

        permission.setDescription(permissionParam.getDescription());
        permission.setSort(permissionParam.getSort());

        repository.save(permission);
    }

    @Transactional(rollbackOn = Exception.class)
    public void removePermission(long id) {
        final Optional<Permission> permissionOptional = repository.findById(id);
        if (!permissionOptional.isPresent()) {
            throw new CartisanException("待删除的权限不存在");
        }

        if (repository.existsByParentId(id)) {
            throw new CartisanException("当前权限下面有子权限，无法删除");
        }

//        if (userService.existsUserInDepartment(id)) {
//            throw new CartisanException("当前菜单/权限下面有用户，无法删除");
//        }

        repository.deleteById(id);
    }
}

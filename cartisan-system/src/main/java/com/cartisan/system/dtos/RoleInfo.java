package com.cartisan.system.dtos;

import com.cartisan.system.domains.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author colin
 */
@Data
@AllArgsConstructor
public class RoleInfo {
    private String code;

    private String name;

    public static RoleInfo convertFrom(Role role) {
        return new RoleInfo(role.getCode(), role.getName());
    }
}

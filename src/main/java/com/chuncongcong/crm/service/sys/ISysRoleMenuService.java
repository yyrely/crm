package com.chuncongcong.crm.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chuncongcong.crm.model.po.sys.SysRoleMenuPo;

/**
 * @author HU
 * @date 2022/1/21 11:34
 */

public interface ISysRoleMenuService extends IService<SysRoleMenuPo> {

    /**
     * 更新角色菜单权限
     * @param roleId
     * @param menuIds
     */
    void updateRoleMenus(Long roleId, List<Long> menuIds);
}

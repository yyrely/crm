package com.chuncongcong.crm.service.sys.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuncongcong.crm.mapper.sys.SysRoleMenuMapper;
import com.chuncongcong.crm.model.po.sys.SysRoleMenuPo;
import com.chuncongcong.crm.service.sys.ISysRoleMenuService;

import cn.hutool.core.collection.CollUtil;

/**
 * @author HU
 * @date 2022/1/21 11:35
 */

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuPo> implements ISysRoleMenuService {

    @Override
    public void updateRoleMenus(Long roleId, List<Long> menuIds) {
        // 删除角色原来的菜单
        baseMapper.delete(Wrappers.<SysRoleMenuPo>query().lambda().eq(SysRoleMenuPo::getRoleId, roleId));
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }

        // 批量插入角色新的菜单
        List<SysRoleMenuPo> roleMenuList = menuIds.stream().map(menuId -> {
            SysRoleMenuPo roleMenu = new SysRoleMenuPo();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            return roleMenu;
        }).collect(Collectors.toList());
        if (!roleMenuList.isEmpty()){
            this.saveBatch(roleMenuList);
        }
    }
}

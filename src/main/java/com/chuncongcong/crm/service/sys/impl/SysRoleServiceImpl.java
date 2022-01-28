package com.chuncongcong.crm.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuncongcong.crm.mapper.sys.SysRoleMapper;
import com.chuncongcong.crm.model.po.sys.SysRoleMenuPo;
import com.chuncongcong.crm.model.po.sys.SysRolePo;
import com.chuncongcong.crm.model.vo.sys.SysRoleVo;
import com.chuncongcong.crm.service.sys.ISysRoleMenuService;
import com.chuncongcong.crm.service.sys.ISysRoleService;

/**
 * @author Hu
 * @since 2022-01-20
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRolePo> implements ISysRoleService {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleById(Long id) {
        // 删除角色菜单关系
        sysRoleMenuService.remove(Wrappers.<SysRoleMenuPo>update().lambda().eq(SysRoleMenuPo::getRoleId, id));
        // 删除角色
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenus(SysRoleVo sysRoleVo) {
        // 更新角色菜单权限
        sysRoleMenuService.updateRoleMenus(sysRoleVo.getId(), sysRoleVo.getMenuIds());
    }

    @Override
    public List<SysRoleMenuPo> listMenuIdsByRoleId(Long roleId) {
        // 根据角色id获取菜单列表
        return sysRoleMenuService.list(Wrappers.lambdaQuery(SysRoleMenuPo.class).eq(SysRoleMenuPo::getRoleId, roleId));
    }
}

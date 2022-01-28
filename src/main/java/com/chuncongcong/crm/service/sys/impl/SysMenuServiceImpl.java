package com.chuncongcong.crm.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuncongcong.crm.common.constant.CommonConstants;
import com.chuncongcong.crm.common.exception.ServiceException;
import com.chuncongcong.crm.mapper.sys.SysMenuMapper;
import com.chuncongcong.crm.model.dto.sys.SysMenuDto;
import com.chuncongcong.crm.model.po.sys.SysMenuPo;
import com.chuncongcong.crm.model.po.sys.SysRoleMenuPo;
import com.chuncongcong.crm.service.sys.ISysMenuService;
import com.chuncongcong.crm.service.sys.ISysRoleMenuService;
import com.chuncongcong.crm.utils.TreeUtils;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hu
 * @since 2022-01-20
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuPo> implements ISysMenuService {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Override
    public void removeMenuById(Integer id) {
        // 判断是否存在子菜单
        List<SysMenuPo> menuList = this.list(Wrappers.<SysMenuPo>query().lambda().eq(SysMenuPo::getParentId, id));
        if (CollUtil.isNotEmpty(menuList)) {
            throw new ServiceException("菜单含有下级不能删除");
        }
        // 删除角色菜单关系
        sysRoleMenuService.remove(Wrappers.<SysRoleMenuPo>query().lambda().eq(SysRoleMenuPo::getMenuId, id));
        // 删除菜单
        this.removeById(id);
    }

    @Override
    public List<SysMenuDto> findMenuByRoleIds(List<Long> roles) {
        // 查询所有角色对应的菜单
        return baseMapper.findMenuByRoleIds(roles);
    }

    @Override
    public List<SysMenuDto> treeMenu(boolean lazy, Long parentId) {
        // 懒加载（加载全部）
        if (!lazy) {
            List<SysMenuDto> sysMenuDtos = baseMapper.selectByParentId(null);
            return TreeUtils.build(sysMenuDtos, CommonConstants.MENU_TREE_ROOT_ID);
        }

        // 按父节点加载
        Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
        List<SysMenuDto> sysMenuDtos = baseMapper.selectByParentId(parent);
        return TreeUtils.build(sysMenuDtos, parent);
    }
}

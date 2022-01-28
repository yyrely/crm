package com.chuncongcong.crm.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chuncongcong.crm.model.dto.sys.SysMenuDto;
import com.chuncongcong.crm.model.po.sys.SysMenuPo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hu
 * @since 2022-01-20
 */
public interface ISysMenuService extends IService<SysMenuPo> {

    /**
     * 删除菜单
     * @param id
     */
    void removeMenuById(Integer id);

    /**
     * 根据角色获取菜单
     * @param roles
     * @return
     */
    List<SysMenuDto> findMenuByRoleIds(List<Long> roles);

    /**
     * 懒加载返回全部，非则返回父节点下的子集
     * @param lazy
     * @param parentId
     * @return
     */
    List<SysMenuDto> treeMenu(boolean lazy, Long parentId);
}

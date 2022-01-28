package com.chuncongcong.crm.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chuncongcong.crm.model.po.sys.SysRoleMenuPo;
import com.chuncongcong.crm.model.po.sys.SysRolePo;
import com.chuncongcong.crm.model.vo.sys.SysRoleVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hu
 * @since 2022-01-20
 */
public interface ISysRoleService extends IService<SysRolePo> {

    /**
     * 删除角色
     * @param id
     */
    void removeRoleById(Long id);

    /**
     * 更新角色菜单权限
     * @param sysRoleVo
     */
    void updateRoleMenus(SysRoleVo sysRoleVo);

    /**
     * 根据角色id获取菜单列表
     * @param roleId
     */
    List<SysRoleMenuPo> listMenuIdsByRoleId(Long roleId);


}

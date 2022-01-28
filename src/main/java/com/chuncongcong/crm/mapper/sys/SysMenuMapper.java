package com.chuncongcong.crm.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chuncongcong.crm.model.dto.sys.SysMenuDto;
import com.chuncongcong.crm.model.po.sys.SysMenuPo;

/**
 * @author Hu
 * @since 2022-01-20
 */

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuPo> {

    /**
     * 根据角色获取菜单
     * @param roles
     * @return
     */
    List<SysMenuDto> findMenuByRoleIds(@Param("roles") List<Long> roles);

    /**
     * 根据父节点获取菜单
     * @param parentId
     * @return
     */
    List<SysMenuDto> selectByParentId(@Param("parentId") Long parentId);
}

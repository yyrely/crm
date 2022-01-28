package com.chuncongcong.crm.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chuncongcong.crm.model.po.sys.SysUserPo;
import com.chuncongcong.crm.model.vo.sys.SysUserVo;

/**
 * @author HU
 * @date 2022/1/19 15:53
 */

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserPo> {

    /**
     * 分页查询用户信息
     * @param page
     * @param sysUserVo
     * @param deptIds
     */
    IPage<SysUserVo> pageUser(Page<SysUserPo> page, @Param("userQuery") SysUserVo sysUserVo, @Param("deptIds") List<Long> deptIds);
}

package com.chuncongcong.crm.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chuncongcong.crm.model.dto.sys.SysDeptDto;
import com.chuncongcong.crm.model.po.sys.SysDeptPo;

/**
 * @author Hu
 * @since 2022-01-21
 */

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDeptPo> {

    List<SysDeptDto> selectByParentId(@Param("parentId") Long parentId);
}

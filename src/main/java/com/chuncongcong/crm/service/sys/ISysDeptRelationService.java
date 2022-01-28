package com.chuncongcong.crm.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chuncongcong.crm.model.po.sys.SysDeptPo;
import com.chuncongcong.crm.model.po.sys.SysDeptRelationPo;

/**
 * @author HU
 * @date 2022/1/21 17:47
 */

public interface ISysDeptRelationService extends IService<SysDeptRelationPo> {

    /**
     * 插入部门关系表
     * @param sysDept
     */
    void insertDeptRelation(SysDeptPo sysDept);

    /**
     * 通过ID删除部门关系
     * @param descendantIds
     */
    void deleteAllDeptRelation(List<Long> descendantIds);


    /**
     * 根据部门id查询所有子部门信息
     * @param deptId
     * @return
     */
    List<SysDeptRelationPo> getDescendantList(Long deptId);
}

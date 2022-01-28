package com.chuncongcong.crm.service.sys.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuncongcong.crm.mapper.sys.SysDeptRelationMapper;
import com.chuncongcong.crm.model.po.sys.SysDeptPo;
import com.chuncongcong.crm.model.po.sys.SysDeptRelationPo;
import com.chuncongcong.crm.service.sys.ISysDeptRelationService;

import cn.hutool.core.collection.CollUtil;

/**
 * @author HU
 * @date 2022/1/21 17:48
 */

@Service
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelationPo>
    implements ISysDeptRelationService {

    @Override
    public void insertDeptRelation(SysDeptPo sysDeptPo) {
        // 查询部门父节点的所有祖先节点，并维护祖先节点和当前节点关系
        List<SysDeptRelationPo> relationList = baseMapper
            .selectList(Wrappers.<SysDeptRelationPo>query().lambda().eq(SysDeptRelationPo::getDescendant,
                sysDeptPo.getParentId()))
            .stream().peek(relation -> relation.setDescendant(sysDeptPo.getId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(relationList)) {
            this.saveBatch(relationList);
        }

        // 自己也要维护到关系表中
        SysDeptRelationPo own = new SysDeptRelationPo();
        own.setDescendant(sysDeptPo.getId());
        own.setAncestor(sysDeptPo.getId());
        baseMapper.insert(own);
    }

    @Override
    public void deleteAllDeptRelation(List<Long> descendantIds) {
        // 查询所有的部门的祖先节点并删除
        List<Long> ids = baseMapper
            .selectList(
                Wrappers.<SysDeptRelationPo>query().lambda().in(SysDeptRelationPo::getDescendant, descendantIds))
            .stream().map(SysDeptRelationPo::getId).collect(Collectors.toList());

        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<SysDeptRelationPo> getDescendantList(Long deptId) {
        // 获取所有子孙部门
        return baseMapper.selectList(Wrappers.<SysDeptRelationPo>lambdaQuery().eq(SysDeptRelationPo::getAncestor, deptId));
    }
}

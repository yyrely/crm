package com.chuncongcong.crm.service.sys.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuncongcong.crm.common.constant.CommonConstants;
import com.chuncongcong.crm.mapper.sys.SysDeptMapper;
import com.chuncongcong.crm.model.dto.sys.SysDeptDto;
import com.chuncongcong.crm.model.po.sys.SysDeptPo;
import com.chuncongcong.crm.model.po.sys.SysDeptRelationPo;
import com.chuncongcong.crm.model.vo.sys.SysDeptVo;
import com.chuncongcong.crm.service.sys.ISysDeptRelationService;
import com.chuncongcong.crm.service.sys.ISysDeptService;
import com.chuncongcong.crm.utils.TreeUtils;

import cn.hutool.core.collection.CollUtil;

/**
 * @author Hu
 * @since 2022-01-21
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptPo> implements ISysDeptService {

    @Autowired
    private ISysDeptRelationService sysDeptRelationService;

    @Override
    public void saveDept(SysDeptVo sysDeptVo) {
        // 保存部门
        SysDeptPo sysDept = new SysDeptPo();
        BeanUtils.copyProperties(sysDeptVo, sysDept);
        this.save(sysDept);
        // 维护部门父子节点关系
        sysDeptRelationService.insertDeptRelation(sysDept);
    }

    @Override
    public void removeDeptById(Integer id) {
        // 级联删除部门
        List<Long> idList = sysDeptRelationService
            .list(Wrappers.<SysDeptRelationPo>query().lambda().eq(SysDeptRelationPo::getAncestor, id)).stream()
            .map(SysDeptRelationPo::getDescendant).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }
        // 删除部门级联关系
        sysDeptRelationService.deleteAllDeptRelation(idList);
    }

    @Override
    public List<SysDeptDto> treeDept(boolean lazy, Long parentId) {
        // 懒加载（加载全部）
        if (!lazy) {
            List<SysDeptDto> sysDeptDtos = baseMapper.selectByParentId(null);
            return TreeUtils.build(sysDeptDtos, CommonConstants.DEPT_TREE_ROOT_ID);
        }

        // 按父节点加载
        Long parent = parentId == null ? CommonConstants.DEPT_TREE_ROOT_ID : parentId;
        List<SysDeptDto> sysDeptDtos = baseMapper.selectByParentId(parent);
        return TreeUtils.build(sysDeptDtos, parent);
    }
}

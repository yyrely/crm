package com.chuncongcong.crm.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chuncongcong.crm.model.dto.sys.SysDeptDto;
import com.chuncongcong.crm.model.po.sys.SysDeptPo;
import com.chuncongcong.crm.model.vo.sys.SysDeptVo;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author Hu
 * @since 2022-01-21
 */
public interface ISysDeptService extends IService<SysDeptPo> {

    /**
     * 保存部门
     * @param sysDeptVo
     */
    void saveDept(SysDeptVo sysDeptVo);

    /**
     * 删除部门
     * @param id
     */
    void removeDeptById(Integer id);

    /**
     * 查询按树状结构返回
     * @param lazy
     * @param parentId
     * @return
     */
    List<SysDeptDto> treeDept(boolean lazy, Long parentId);
}

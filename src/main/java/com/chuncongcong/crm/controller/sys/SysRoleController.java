package com.chuncongcong.crm.controller.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chuncongcong.crm.common.config.authorization.SecurityUtil;
import com.chuncongcong.crm.common.constant.CommonConstants;
import com.chuncongcong.crm.common.constant.sys.SysMenuTypeConstants;
import com.chuncongcong.crm.common.exception.ServiceException;
import com.chuncongcong.crm.model.dto.sys.SysMenuDto;
import com.chuncongcong.crm.model.po.sys.SysRoleMenuPo;
import com.chuncongcong.crm.model.po.sys.SysRolePo;
import com.chuncongcong.crm.model.vo.page.PagingObject;
import com.chuncongcong.crm.model.vo.page.SimplePagingObject;
import com.chuncongcong.crm.model.vo.sys.SysRoleVo;
import com.chuncongcong.crm.service.sys.ISysMenuService;
import com.chuncongcong.crm.service.sys.ISysRoleService;
import com.chuncongcong.crm.utils.TreeUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Hu
 * @since 2022-01-20
 */
@RestController
@RequestMapping("/api/sys/role")
@Api(value = "role", tags = "角色管理模块")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 通过ID查询角色信息
     * 
     * @param id ID
     * @return 角色信息
     */
    @ApiOperation("通过ID查询角色信息")
    @GetMapping("/info/{id}")
    public SysRolePo getById(@PathVariable Integer id) {
        return sysRoleService.getById(id);
    }

    /**
     * 添加角色
     * 
     * @param sysRoleVo 角色信息
     * @return
     */
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public void save(@Valid @RequestBody SysRoleVo sysRoleVo) {
        SysRolePo existRole = sysRoleService
            .getOne(Wrappers.lambdaQuery(SysRolePo.class).eq(SysRolePo::getRoleName, sysRoleVo.getRoleName()));
        if (Objects.nonNull(existRole)) {
            throw new ServiceException("角色已存在");
        }
        sysRoleService.save(BeanUtil.copyProperties(sysRoleVo, SysRolePo.class));
    }

    /**
     * 更新角色
     * 
     * @param sysRoleVo 角色信息
     * @return
     */
    @ApiOperation("更新角色")
    @PostMapping("/update")
    public void update(@Valid @RequestBody SysRoleVo sysRoleVo) {
        sysRoleService.updateById(BeanUtil.copyProperties(sysRoleVo, SysRolePo.class));
    }

    /**
     * 删除角色
     * 
     * @param id ID
     * @return
     */
    @ApiOperation("删除角色")
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        sysRoleService.removeRoleById(id);
    }

    /**
     * 分页查询
     * 
     * @param page
     * @param sysRoleVo
     * @return
     */
    @ApiOperation("分页查询角色列表")
    @GetMapping("/page")
    public PagingObject<SysRolePo> page(Page<SysRolePo> page, SysRoleVo sysRoleVo) {
        Page<SysRolePo> sysRolePoPage =
            sysRoleService.page(page, Wrappers.query(BeanUtil.copyProperties(sysRoleVo, SysRolePo.class)));
        return new SimplePagingObject<>(sysRolePoPage.getRecords(), page.getCurrent(), page.getSize(),
            sysRolePoPage.getTotal());
    }

    /**
     * 列表查询
     *
     * @param sysRoleVo
     * @return
     */
    @ApiOperation("查询角色列表")
    @GetMapping("/list")
    public List<SysRolePo> list(SysRoleVo sysRoleVo) {
        return sysRoleService.list(Wrappers.query(BeanUtil.copyProperties(sysRoleVo, SysRolePo.class)));
    }

    /**
     * 更新角色菜单权限
     * 
     * @param sysRoleVo
     * @return
     */
    @ApiOperation("更新角色菜单权限")
    @PostMapping("/update/menu")
    public void updateRoleMenus(@RequestBody SysRoleVo sysRoleVo) {
        sysRoleService.updateRoleMenus(sysRoleVo);
    }

    /**
     * 返回当前用户的树形菜单集合
     *
     * @param parentId 父节点ID
     * @return 当前用户的树形菜单
     */
    @ApiOperation("返回当前用户的树形菜单集合")
    @GetMapping("/menu/tree")
    public List<SysMenuDto> getUserMenu(Long parentId) {
        List<Long> roles = SecurityUtil.getRoles();
        if (CollectionUtil.isEmpty(roles)) {
            return new ArrayList<>();
        }

        List<SysMenuDto> sysMenuDtos = sysMenuService.findMenuByRoleIds(roles);
        // 排除按钮
        List<SysMenuDto> menuDtos =
            sysMenuDtos.stream().filter(sysMenuDto -> SysMenuTypeConstants.LEFT_MENU.equals(sysMenuDto.getType()))
                .collect(Collectors.toList());
        // 构建树结构
        Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
        return TreeUtils.build(menuDtos, parent);
    }

    /**
     * 根据角色id获取菜单列表
     * 
     * @param roleId 角色id
     * @return
     */
    @ApiOperation("根据角色id获取菜单列表")
    @GetMapping("/menu/{roleId}")
    public List<SysRoleMenuPo> listMenuIdsByRoleId(@PathVariable("roleId") Long roleId) {
        return sysRoleService.listMenuIdsByRoleId(roleId);
    }

}

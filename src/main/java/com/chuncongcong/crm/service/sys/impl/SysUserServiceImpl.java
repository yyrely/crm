package com.chuncongcong.crm.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuncongcong.crm.common.config.authorization.AuthUser;
import com.chuncongcong.crm.common.config.datascope.DataScopeHandle;
import com.chuncongcong.crm.common.exception.ServiceException;
import com.chuncongcong.crm.mapper.sys.SysUserMapper;
import com.chuncongcong.crm.model.dto.sys.SysMenuDto;
import com.chuncongcong.crm.model.po.sys.SysDeptRelationPo;
import com.chuncongcong.crm.model.po.sys.SysUserPo;
import com.chuncongcong.crm.model.po.sys.SysUserRolePo;
import com.chuncongcong.crm.model.vo.sys.SysUserVo;
import com.chuncongcong.crm.service.sys.ISysDeptRelationService;
import com.chuncongcong.crm.service.sys.ISysMenuService;
import com.chuncongcong.crm.service.sys.ISysUserRoleService;
import com.chuncongcong.crm.service.sys.ISysUserService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author HU
 * @date 2022/1/19 14:56
 */

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPo>
    implements ISysUserService, UserDetailsService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysDeptRelationService deptRelationService;

    @Autowired
    private DataScopeHandle dataScopeHandle;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        SysUserPo sysUserPo = this.getOne(Wrappers.lambdaQuery(SysUserPo.class).eq(SysUserPo::getUsername, username));
        if (Objects.isNull(sysUserPo)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 查询用户对应角色
        List<SysUserRolePo> sysUserRolePoList = sysUserRoleService
            .list(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getUserId, sysUserPo.getId()));
        List<Long> roleIds = sysUserRolePoList.stream().map(SysUserRolePo::getRoleId).collect(Collectors.toList());

        // 查询角色对应菜单权限
        Collection<? extends GrantedAuthority> authorities = null;
        if (CollectionUtil.isNotEmpty(roleIds)) {
            List<SysMenuDto> sysMenuDtos = sysMenuService.findMenuByRoleIds(roleIds);
            List<String> permissions = new ArrayList<>();
            for (SysMenuDto sysMenuDto : sysMenuDtos) {
                if (StrUtil.isNotEmpty(sysMenuDto.getPermission())) {
                    permissions.add(sysMenuDto.getPermission());
                }
            }
            authorities = AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));
        }

        // 查询可查看的部门范围
        List<Long> deptScope = new ArrayList<>();
        Boolean isAllDeptScope = dataScopeHandle.calcScope(roleIds, sysUserPo.getDeptId(), deptScope);

        return new AuthUser(sysUserPo.getId(), sysUserPo.getUsername(), sysUserPo.getPassword(), sysUserPo.getDeptId(),
            roleIds, deptScope, isAllDeptScope, authorities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(SysUserVo sysUserVo) {
        // 用户名不能相同
        SysUserPo existUser = baseMapper
            .selectOne(Wrappers.lambdaQuery(SysUserPo.class).eq(SysUserPo::getUsername, sysUserVo.getUsername()));
        if (Objects.nonNull(existUser)) {
            throw new ServiceException("用户已存在");
        }
        // 新增用户
        SysUserPo sysUserPo = BeanUtil.copyProperties(sysUserVo, SysUserPo.class);
        sysUserPo.setPassword(bCryptPasswordEncoder.encode(sysUserPo.getPassword()));
        baseMapper.insert(sysUserPo);

        // 维护用户角色关系
        if (CollectionUtil.isEmpty(sysUserVo.getRoleIds())) {
            return;
        }
        batchAddUserRole(sysUserVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserById(Long id) {
        // 删除用户
        this.removeById(id);
        // 删除用户角色关系
        sysUserRoleService.remove(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getUserId, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserVo sysUserVo) {
        // 更新用户
        SysUserPo sysUserPo = BeanUtil.copyProperties(sysUserVo, SysUserPo.class);
        sysUserPo.setPassword(bCryptPasswordEncoder.encode(sysUserPo.getPassword()));
        baseMapper.updateById(sysUserPo);

        // 维护用户角色关系
        if (CollectionUtil.isEmpty(sysUserVo.getRoleIds())) {
            return;
        }
        // 删除原来的用户角色关系
        sysUserRoleService
            .remove(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getUserId, sysUserVo.getId()));
        batchAddUserRole(sysUserVo);
    }

    @Override
    public IPage<SysUserVo> pageUser(Page<SysUserPo> page, SysUserVo sysUserVo) {
        // 查询部门本级及以下的用户
        List<Long> descendantIds = null;
        if(Objects.nonNull(sysUserVo.getDeptId())) {
            List<SysDeptRelationPo> descendants = deptRelationService.getDescendantList(sysUserVo.getDeptId());
            descendantIds = descendants.stream().map(SysDeptRelationPo::getDescendant).collect(Collectors.toList());
        }
        return baseMapper.pageUser(page, sysUserVo, descendantIds);
    }

    private void batchAddUserRole(SysUserVo sysUserVo) {
        // 批量新增用户角色关系
        List<SysUserRolePo> sysUserRolePos = sysUserVo.getRoleIds().stream().map(roleId -> {
            SysUserRolePo sysUserRolePo = new SysUserRolePo();
            sysUserRolePo.setUserId(sysUserVo.getId());
            sysUserRolePo.setRoleId(roleId);
            return sysUserRolePo;
        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(sysUserRolePos)) {
            sysUserRoleService.saveBatch(sysUserRolePos);
        }
    }
}

package com.chuncongcong.crm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.chuncongcong.crm.model.dto.common.TreeNode;

/**
 * @author HU
 * @date 2022/1/21 10:05
 */

public class TreeUtils {

    /**
     * 构建树结构（事先按parentId, sort排序）
     * @param treeNodes
     * @param parentId
     * @return
     */
    public static <T extends TreeNode> List<T> build(List<T> treeNodes, Long parentId) {
        List<T> result = new ArrayList<>();
        Map<Long, T> treeNodeMap = treeNodes.stream().collect(Collectors.toMap(T::getId, Function.identity()));
        for (T treeNode : treeNodes) {
            if(treeNode.getParentId().equals(parentId)) {
                result.add(treeNodeMap.get(treeNode.getId()));
            } else if (treeNode.getParentId() > parentId) {
                T parentTreeNode = treeNodeMap.get(treeNode.getParentId());
                parentTreeNode.getChildren().add(treeNodeMap.get(treeNode.getId()));
            }
        }
        return result;
    }
}




















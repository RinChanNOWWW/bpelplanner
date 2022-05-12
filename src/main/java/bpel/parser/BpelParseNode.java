package bpel.parser;


import bpel.BpelInfo;

import java.util.List;


/**
 * BPEL 分析树节点
 */
abstract public class BpelParseNode {
    public enum ParseNodeType {
        ROOT, SEQUENCE, FLOW, OPERATION;
    }
    /**
     * 获取节点类型
     *
     * @return 节点类型
     */
    abstract public ParseNodeType getType();

    /**
     * 获取子节点
     *
     * @return 子节点
     */
    abstract public List<BpelParseNode> getChildren();

    /**
     * 添加子节点
     *
     * @param child 待添加子节点
     */
    abstract public void addChild(BpelParseNode child);

    /**
     * 获取节点附加信息
     *
     * @return 节点额外信息，只有叶子节点携带
     */
    abstract public BpelInfo getInfo();
}


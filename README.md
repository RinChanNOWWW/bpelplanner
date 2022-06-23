# BPEL PLANNER

北京邮电大学研究生课程《服务计算原理与技术》作业：搜索BPEL流程的各种可能的执行路径。

## 运行

本项目基于 Maven 与 Java 15 构建。

建议使用 Intellij IDEA 运行。

## 算法概览

1. 解析 XML，建立语法分析树。
2. 访问语法分析树，生成有向无环图。
3. 搜索有向无环图的所有拓扑排序序列。
4. 一条拓扑排序序列即为一条可能的执行路径。

## 代码结构

- Main.java：程序入口。简单起见目标文件为硬编码，没有实现命令行参数。
- bpel
  - parser/*: 语法分析树节点。
  - BpelInfo.java: BPEL XML 中建立 DAG 所需要的信息。
  - BpelParser.java: BPEL 解析器类。负责 XML 分析与 DAG 构建。
- dag
  - Dag.java: 有向无环图类。
  - DagNode.java: 有向无环图节点类。
- utils/TopologicalSort.java: 拓扑排序执行类。
- examples/*: 实验用的 BPEL 文件。

## 局限

1. 仅支持 sequence, flow, receive, invoke, reply 元素。
2. 为简单起见，每一个执行单元的名称由其子元素 documentation 决定。如：

```xml
<receive partnerLink="purchasing" portType="lns:purchaseOrderPT"
         operation="sendPurchaseOrder" variable="PO"
         createInstance="yes">
    <documentation>Receive Purchase Order</documentation>
</receive>
```
3. 依赖关系只考虑了 `inputVariable` 与 `outputVariable`(`variable`)。
4. 没有考虑循环依赖等异常情况。输入需要能够正确建立有向无环图。
5. 输入为硬编码，程序未封装为 CLI 软件。

## 需求描述

任务描述：

设计并实现一个程序，搜索BPEL流程的各种可能的执行路径。

1. 结果：

- ppt讲解你的方法/算法(中英文均可)
- 运行程序演示结果 （输入使用参考文献中的业务流程例子或者其它别的例子，输出这个例子中的所有可能的执行路径）
- 程序用Java开发

2. 说明：

- 可以使用已有方法/算法，或者自己设计
- 不一定要原创，如果能找到现成的代码或工具，可以借助或者修改它们来完成
- 可以只处理BPEL程序的简单控制逻辑
- 说明你的方法/算法的局限性（哪些控制逻辑还处理不了）
- 参考文献中有4个已经写好的BPEL流程：purchaseOrderProcess, ShippingService, Loan Approval Service, Auction Service。可以用来做实验，最后结果用它来演示，但也可以用别的例子来演示。
- 独立完成，发挥想象力。

3. 参考文献

- [Web Services Business Process Execution Language Version 2.0](http://docs.oasis-open.org/wsbpel/2.0/OS/wsbpel-v2.0-OS.html#_Toc164738480)

## 实验效果

```
Plan 01: Receive Purchase Order -> Decide On Shipper -> Arrange Logistics -> Initial Price Calculation -> Complete Price Calculation -> Initiate Production Scheduling -> Complete Production Scheduling -> Invoice Processing
Plan 02: Receive Purchase Order -> Decide On Shipper -> Arrange Logistics -> Initial Price Calculation -> Initiate Production Scheduling -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 03: Receive Purchase Order -> Decide On Shipper -> Arrange Logistics -> Initial Price Calculation -> Initiate Production Scheduling -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 04: Receive Purchase Order -> Decide On Shipper -> Arrange Logistics -> Initiate Production Scheduling -> Initial Price Calculation -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 05: Receive Purchase Order -> Decide On Shipper -> Arrange Logistics -> Initiate Production Scheduling -> Initial Price Calculation -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 06: Receive Purchase Order -> Decide On Shipper -> Arrange Logistics -> Initiate Production Scheduling -> Complete Production Scheduling -> Initial Price Calculation -> Complete Price Calculation -> Invoice Processing
Plan 07: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Arrange Logistics -> Complete Price Calculation -> Initiate Production Scheduling -> Complete Production Scheduling -> Invoice Processing
Plan 08: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Arrange Logistics -> Initiate Production Scheduling -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 09: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Arrange Logistics -> Initiate Production Scheduling -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 10: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Complete Price Calculation -> Arrange Logistics -> Initiate Production Scheduling -> Complete Production Scheduling -> Invoice Processing
Plan 11: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Complete Price Calculation -> Initiate Production Scheduling -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
Plan 12: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Initiate Production Scheduling -> Arrange Logistics -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 13: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Initiate Production Scheduling -> Arrange Logistics -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 14: Receive Purchase Order -> Decide On Shipper -> Initial Price Calculation -> Initiate Production Scheduling -> Complete Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
Plan 15: Receive Purchase Order -> Decide On Shipper -> Initiate Production Scheduling -> Arrange Logistics -> Initial Price Calculation -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 16: Receive Purchase Order -> Decide On Shipper -> Initiate Production Scheduling -> Arrange Logistics -> Initial Price Calculation -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 17: Receive Purchase Order -> Decide On Shipper -> Initiate Production Scheduling -> Arrange Logistics -> Complete Production Scheduling -> Initial Price Calculation -> Complete Price Calculation -> Invoice Processing
Plan 18: Receive Purchase Order -> Decide On Shipper -> Initiate Production Scheduling -> Initial Price Calculation -> Arrange Logistics -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 19: Receive Purchase Order -> Decide On Shipper -> Initiate Production Scheduling -> Initial Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 20: Receive Purchase Order -> Decide On Shipper -> Initiate Production Scheduling -> Initial Price Calculation -> Complete Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
Plan 21: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Arrange Logistics -> Complete Price Calculation -> Initiate Production Scheduling -> Complete Production Scheduling -> Invoice Processing
Plan 22: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Arrange Logistics -> Initiate Production Scheduling -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 23: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Arrange Logistics -> Initiate Production Scheduling -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 24: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Complete Price Calculation -> Arrange Logistics -> Initiate Production Scheduling -> Complete Production Scheduling -> Invoice Processing
Plan 25: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Complete Price Calculation -> Initiate Production Scheduling -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
Plan 26: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Initiate Production Scheduling -> Arrange Logistics -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 27: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Initiate Production Scheduling -> Arrange Logistics -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 28: Receive Purchase Order -> Initial Price Calculation -> Decide On Shipper -> Initiate Production Scheduling -> Complete Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
Plan 29: Receive Purchase Order -> Initial Price Calculation -> Initiate Production Scheduling -> Decide On Shipper -> Arrange Logistics -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 30: Receive Purchase Order -> Initial Price Calculation -> Initiate Production Scheduling -> Decide On Shipper -> Arrange Logistics -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 31: Receive Purchase Order -> Initial Price Calculation -> Initiate Production Scheduling -> Decide On Shipper -> Complete Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
Plan 32: Receive Purchase Order -> Initiate Production Scheduling -> Decide On Shipper -> Arrange Logistics -> Initial Price Calculation -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 33: Receive Purchase Order -> Initiate Production Scheduling -> Decide On Shipper -> Arrange Logistics -> Initial Price Calculation -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 34: Receive Purchase Order -> Initiate Production Scheduling -> Decide On Shipper -> Arrange Logistics -> Complete Production Scheduling -> Initial Price Calculation -> Complete Price Calculation -> Invoice Processing
Plan 35: Receive Purchase Order -> Initiate Production Scheduling -> Decide On Shipper -> Initial Price Calculation -> Arrange Logistics -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 36: Receive Purchase Order -> Initiate Production Scheduling -> Decide On Shipper -> Initial Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 37: Receive Purchase Order -> Initiate Production Scheduling -> Decide On Shipper -> Initial Price Calculation -> Complete Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
Plan 38: Receive Purchase Order -> Initiate Production Scheduling -> Initial Price Calculation -> Decide On Shipper -> Arrange Logistics -> Complete Price Calculation -> Complete Production Scheduling -> Invoice Processing
Plan 39: Receive Purchase Order -> Initiate Production Scheduling -> Initial Price Calculation -> Decide On Shipper -> Arrange Logistics -> Complete Production Scheduling -> Complete Price Calculation -> Invoice Processing
Plan 40: Receive Purchase Order -> Initiate Production Scheduling -> Initial Price Calculation -> Decide On Shipper -> Complete Price Calculation -> Arrange Logistics -> Complete Production Scheduling -> Invoice Processing
```

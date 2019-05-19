<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>

<style type="text/css">
    table {
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        width: 100%;
        border-collapse: collapse;
    }

    td, th {
        font-size: 1em;
        border: 1px solid #5B4A42;
        padding: 3px 7px 2px 7px;
    }

    th {
        font-size: 1.1em;
        text-align: center;
        padding-top: 5px;
        padding-bottom: 4px;
        background-color: #24A9E1;
        color: #ffffff;
    }
</style>
<body>
<div>
    <h2>每日报表</h2>
    <table id="customers">
        <tr>
            <th>产品分类</th>
            <th>订单ID</th>
            <th>命中时间</th>
            <th>订单状态</th>
            <th>命中规则</th>
            <th>操作员</th>
            <th>分配时间</th>
            <th>出结论时候</th>
            <th>结论</th>
            <th>欺诈类型</th>
        </tr>
        <#list pos! as po>
            <tr>
                <td>
                    <#if po.sourceCode == "a">
                        a
                    <#elseif po.sourceCode == "b">
                        b
                    <#else>
                        c
                    </#if>
                </td>
                <td>${(po.orderId)!""}</td>
                <td>
                    <#if (po.hitTime?string("yyyy-MM-dd HH:mm:ss")) == "1970-01-01 00:00:00">
                        --
                    <#else>
                        ${(po.hitTime?string("yyyy-MM-dd HH:mm:ss"))}
                    </#if>
                </td>
                <td>
                    <#if po.orderStatus == 1>
                        未审核
                    <#elseif po.orderStatus == 2>
                        等待
                    <#elseif po.orderStatus == 3>
                        待编辑
                    <#elseif po.orderStatus == 4>
                        拒绝
                    <#elseif po.orderStatus == 5>
                        通过
                    <#else>
                        未知
                    </#if>
                </td>
                <td>${(po.hitMsg)!""}</td>
                <td>${(po.operatorName)!""}</td>
                <td>
                    <#if (po.distributionTime?string("yyyy-MM-dd HH:mm:ss")) == "1970-01-01 00:00:00">
                        --
                    <#else>
                        ${(po.distributionTime?string("yyyy-MM-dd HH:mm:ss"))}
                    </#if>
                </td>
                <td>
                    <#if (po.resultTime?string("yyyy-MM-dd HH:mm:ss")) == "1970-01-01 00:00:00">
                        --
                    <#else>
                        ${(po.resultTime?string("yyyy-MM-dd HH:mm:ss"))}
                    </#if>
                </td>
                <td>${(po.ruleResult)!""}</td>
                <td>${(po.cheatType)!""}</td>
            </tr>
        </#list>
    </table>
</div>
</body>
</html>
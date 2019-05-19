package com.rickiyang.learn.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author: rickiyang
 * @date: 2019/03/16
 * @description:
 */
@Data
@ToString
@NoArgsConstructor
public class StatisticsEmailPO {
    private Integer orderId;
    private String sourceCode;
    private Integer orderStatus;
    private String operatorName;
    private String cheatType;
    private String ruleResult;
    private Date hitTime;
    private Date distributionTime;
    private Date resultTime;
    private String hitMsg;

    private List<HitRulePO> hitRulePOs;

    public void createHitMsg(){
        if(!CollectionUtils.isEmpty(hitRulePOs)){
            StringBuilder sb = new StringBuilder();

            int index = 1;
            for (HitRulePO po : hitRulePOs){
                sb.append(index++).append("、").append(po.getRuleHitMsg()).append("</br>");
            }

            this.hitMsg = sb.toString();
        }
    }
}

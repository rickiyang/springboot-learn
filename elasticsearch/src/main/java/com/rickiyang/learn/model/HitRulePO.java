package com.rickiyang.learn.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author: rickiyang
 * @date: 2019/03/16
 * @description:
 */

@Data
@ToString
@NoArgsConstructor
public class HitRulePO {
    private Long id;
    private Long orderProcessId;
    private String ruleId;
    private String ruleName;
    private String ruleHitMsg;
    private Integer isDeleted;
    private Date createdAt;
    private Date updatedAt;
}

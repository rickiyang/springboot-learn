package com.rickiyang.learn.service.freemarker;

import com.alibaba.dubbo.config.annotation.Service;
import com.rickiyang.learn.model.StatisticsEmailPO;
import freemarker.template.Template;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: rickiyang
 * @date: 2019/5/19
 * @description: freeMarker 模板demo
 */
@Service
public class DemoFreeMarkerService {

    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;


    /**
     * 封装freeMarker模板
     *
     * @return
     */
    public String setTemplate() {
        String emailBody = StringUtils.EMPTY;
        Map<String, Object> model = new HashMap<>(2);
        List<StatisticsEmailPO> statisticsEmailPOs = Lists.newArrayList();
        model.put("pos", statisticsEmailPOs);
        try {
            Template template = freemarkerConfig.getConfiguration().getTemplate("cheatStatisticsEmail.ftl");
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailBody;
    }
}

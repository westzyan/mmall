package com.mmall.task;

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Author: Zhang Yan
 * Date: 19-6-18 下午8:07
 * Content:
 */

@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

    @Scheduled(cron = "0 */1 * * * ?")//每一分钟
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务启动");
        int hour  = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }

}

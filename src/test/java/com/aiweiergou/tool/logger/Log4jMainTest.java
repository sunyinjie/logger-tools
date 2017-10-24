package com.aiweiergou.tool.logger;

import com.aiweiergou.tool.logger.api.ChangeLogLevelProcess;
import com.aiweiergou.tool.logger.bean.LoggerBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.util.ArrayList;

/**
 * Created by sunyinjie on 2017/10/24.
 */
public class Log4jMainTest {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(AbstractLoggerProcess.class);
        Logger logger2 = LoggerFactory.getLogger(ChangeLogLevelProcess.class);
        logger.debug("test");
        logger.debug("{} & {}", 1, 2);
        logger.debug("type:{}", StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr());
        AbstractLoggerProcess process = new ChangeLogLevelProcess();
        logger.debug("frame:{}", process.logFrameworkType);
        logger.debug("loggerInfoList:{}", process.loggerInfosList);


        ArrayList<LoggerBean> loggerBeans = new ArrayList<>();
        LoggerBean info = new LoggerBean("com.aiweiergou.tool.logger.AbstractLoggerProcess", "INFO");
        LoggerBean info2 = new LoggerBean("com.aiweiergou.tool.logger.api.ChangeLogLevelProcess", "DEBUG");
        loggerBeans.add(info);
        loggerBeans.add(info2);

        logger.debug("我现在是debug");
        logger2.debug("我现在是debug");


        process.process(loggerBeans);

        logger.debug("我打印出来了吗");
        logger.info("看上一条日志有没有就知道了，如果上条日志没有 那我就是被改变了，现在应该是info了");

        logger2.debug("我还是debug");

        process.process("DEBUG");
        logger.debug("改回debug");
        logger2.debug("改回debug");
    }
}

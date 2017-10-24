package com.aiweiergou.tool.logger.constant;

/**
 * 日志框架工厂bean
 * @author sunyinjie
 * @date 2017/10/24
 */
public class LogConstant {
    public static final String LOG4J_LOGGER_FACTORY = "org.slf4j.impl.Log4jLoggerFactory";
    public static final String LOG4J2_LOGGER_FACTORY = "org.apache.logging.slf4j.Log4jLoggerFactory";
    public static final String LOGBACK_LOGGER_FACTORY = "ch.qos.logback.classic.util.ContextSelectorStaticBinder";

    public static final String DEFAULT_KEY = "root";
    public static final String LOGGER_INFO_KEY_LOGGER_NAME = "loggerName";
    public static final String LOGGER_INFO_KEY_LOGGER_LEVEL = "loggerLevel";
    public static final String DEFAULT_LOGGER_INFO_VALUE_LOGGER_LEVEL = "Logger的类型未知,无法处理!";

    public static final String LOGGER_LIST_INFO_KEY_LOG_FRAMEWORK = "logFramework";
    public static final String LOGGER_LIST_INFO_KEY_LOGGER_LIST = "loggerList";

}

package com.aiweiergou.tool.logger;

import com.aiweiergou.tool.logger.constant.LogLevelConstant;
import com.aiweiergou.tool.logger.enums.LogFrameworkType;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.aiweiergou.tool.logger.constant.LogConstant.*;


/**
 * 抽象日志处理器
 * 支持log4j、log4j2和logback
 * @author sunyinjie
 * @date 2017/10/24
 */
public abstract class AbstractLoggerProcess {
    private Logger log = LoggerFactory.getLogger(AbstractLoggerProcess.class);

    protected String defaultLevel = LogLevelConstant.INFO;

    protected final LogFrameworkType logFrameworkType;
    protected final ConcurrentHashMap<String, Object> loggerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> loggerInfos = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, Object> loggerInfosList = new ConcurrentHashMap<>();

    public AbstractLoggerProcess() {
        log.info("[Logger工具]开始初始化框架");
        String type = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        if (log.isDebugEnabled()) {
            log.debug("[Logger工具]当前使用的Log框架工厂为{}", type);
        }
        if (LOG4J_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOG4J;
            Enumeration enumeration = org.apache.log4j.LogManager.getCurrentLoggers();
            while (enumeration.hasMoreElements()) {
                org.apache.log4j.Logger logger = (org.apache.log4j.Logger) enumeration.nextElement();
                if (logger.getLevel() != null) {
                    loggerMap.put(logger.getName(), logger);
                }
            }
            org.apache.log4j.Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
            loggerMap.put(rootLogger.getName(), rootLogger);
        } else if (LOGBACK_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOGBACK;
            ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.getLoggerList().stream().filter(logger -> logger.getLevel() != null).forEach(logger -> {
                loggerMap.put(logger.getName(), logger);
            });
            ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            loggerMap.put(rootLogger.getName(), rootLogger);
        } else if (LOG4J2_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOG4J2;
            org.apache.logging.log4j.core.LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
            Map<String, LoggerConfig> map = loggerContext.getConfiguration().getLoggers();
            for (org.apache.logging.log4j.core.config.LoggerConfig loggerConfig : map.values()) {
                String key = loggerConfig.getName();
                if (StringUtils.isBlank(key)) {
                    key = DEFAULT_KEY;
                }
                loggerMap.put(key, loggerConfig);
            }
        } else {
            logFrameworkType = LogFrameworkType.UNKNOWN;
            log.error("[Logger工具]Log框架无法识别:{}", type);
            return;
        }
        log.info("[Logger工具]框架初始化结束,logger映射表:{}", loggerMap);
        this.getLoggerInfos();
    }

    private Map<String, Object> getLoggerInfos() {
        loggerInfosList.put(LOGGER_LIST_INFO_KEY_LOG_FRAMEWORK, logFrameworkType);
        ArrayList<ConcurrentHashMap<String, Object>> loggerList = new ArrayList<>();
        for (ConcurrentMap.Entry<String, Object> entry : loggerMap.entrySet()) {
            loggerInfos.put(LOGGER_INFO_KEY_LOGGER_NAME, entry.getKey());
            String loggerLevel;
            if (logFrameworkType == LogFrameworkType.LOG4J) {
                org.apache.log4j.Logger targetLogger = (org.apache.log4j.Logger) entry.getValue();
                loggerLevel = targetLogger.getLevel().toString();
            } else if (logFrameworkType == LogFrameworkType.LOGBACK) {
                ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) entry.getValue();
                loggerLevel = targetLogger.getLevel().toString();
            } else if (logFrameworkType == LogFrameworkType.LOG4J2) {
                org.apache.logging.log4j.core.config.LoggerConfig targetLogger = (org.apache.logging.log4j.core.config.LoggerConfig) entry.getValue();
                loggerLevel = targetLogger.getLevel().toString();
            } else {
                log.warn("[Logger工具]未知Log level,无法处理!!!");
                loggerLevel = DEFAULT_LOGGER_INFO_VALUE_LOGGER_LEVEL;
            }
            loggerInfos.put(LOGGER_INFO_KEY_LOGGER_LEVEL, loggerLevel);
            loggerList.add(loggerInfos);
        }
        loggerInfosList.put(LOGGER_LIST_INFO_KEY_LOGGER_LIST, loggerList);
        log.info("[Logger工具]获取logger infos list:{}", loggerInfosList);
        return loggerInfosList;
    }

    /**
     * 处理器核心逻辑
     * 子类实现
     * @param o 可自由实现的入参
     */
    public abstract void process(Object o);

    public void setDefaultLevel(String defaultLevel) {
        this.defaultLevel = defaultLevel;
    }
}

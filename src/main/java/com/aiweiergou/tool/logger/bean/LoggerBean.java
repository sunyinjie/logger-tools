package com.aiweiergou.tool.logger.bean;

import java.io.Serializable;

/**
 *
 * @author sunyinjie
 * @date 2017/10/24
 */
public class LoggerBean implements Serializable {
    private String name;
    private String level;

    public LoggerBean() {
    }

    public LoggerBean(String name, String level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public String toString() {
        return "{\"LoggerBean\":{"
                + "\"name\":\"" + name + "\""
                + ",\"level\":\"" + level + "\""
                + "}}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

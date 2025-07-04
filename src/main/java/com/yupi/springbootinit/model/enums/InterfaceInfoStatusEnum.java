package com.yupi.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum InterfaceInfoStatusEnum {

    ONLINE("online", 1),
    OFFLINE("offline", 0);

    private final String text;

    private final int value;

    InterfaceInfoStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据枚举值获取对应的枚举对象
     *
     * @param value 枚举值
     * @return 对应的枚举对象，若未找到则返回 null
     */
    public static InterfaceInfoStatusEnum getEnumByValue(int value) {
        // 直接遍历枚举值进行比较
        for (InterfaceInfoStatusEnum status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        // 未找到匹配值时返回null
        return null; 
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}

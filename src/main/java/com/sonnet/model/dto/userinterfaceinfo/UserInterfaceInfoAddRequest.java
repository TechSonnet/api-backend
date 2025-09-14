package com.sonnet.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 *  创建请求接口
 *
 *  保留主要的字段，方便前端对接接口
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

}

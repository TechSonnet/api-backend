package com.yupi.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 *  创建请求接口
 *
 *  保留主要的字段，方便前端对接接口
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

}

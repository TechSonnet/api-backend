package com.sonnet.model.dto.interfaceinfo;

import lombok.Data;

/**
 * 查询请求
 */
@Data
public class InterfaceInfoQueryRequest {

    /**
     * 主键
     * 查询时主键不可更改
     */
    private Long id;

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
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 页面大小
     */
    private int pageSize;

    /**
     * 当前页
     */
    private int current;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 请求参数
     */
    private String requestParams;

}

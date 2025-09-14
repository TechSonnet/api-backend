package com.sonnet.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 */
@Data
public class InterfaceInfoInvokeRequest  implements Serializable {

    /**
     * 主键
     * 查询时主键不可更改
     */
    private Long id;


    /**
     * 请求参数
     */
    private String requestParams;

    private static final long serialVersionUID = 1L;

}

package com.sonnet.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 * 更新请求
 */
@Data
public class UserInterfaceInfoUpdateRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

}

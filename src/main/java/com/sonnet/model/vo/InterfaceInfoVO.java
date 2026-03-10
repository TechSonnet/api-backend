package com.sonnet.model.vo;

import com.sonnet.apicommon.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * top 接口信息
 *
 */
@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {

    private String interfaceName;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}
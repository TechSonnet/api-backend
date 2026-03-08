package com.sonnet.mapper;

import com.sonnet.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author chang
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2025-08-20 11:53:14
* @Entity com.sonnet.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    /**
     * 更新调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    int updateInvokeCount(@Param("interfaceInfoId") long interfaceInfoId, @Param("userId") long userId);

}





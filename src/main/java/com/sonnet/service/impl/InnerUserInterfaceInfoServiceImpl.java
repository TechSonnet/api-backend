package com.sonnet.service.impl;

import com.sonnet.apicommon.service.InnerUserInterfaceInfoService;
import org.springframework.stereotype.Service;

@Service
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return false;
    }
}

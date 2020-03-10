package com.wfmyzyz.user.user.service.impl;

import com.wfmyzyz.user.user.domain.Action;
import com.wfmyzyz.user.user.mapper.ActionMapper;
import com.wfmyzyz.user.user.service.IActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-02-28
 */
@Service
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements IActionService {

}

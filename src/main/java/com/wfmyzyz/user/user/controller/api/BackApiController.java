package com.wfmyzyz.user.user.controller.api;

import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.user.vo.AuthorityTreeVo;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @author admin
 */
@Api(tags="API模块-后台管理")
@RestController
@RequestMapping("api/back")
public class BackApiController {
    

}

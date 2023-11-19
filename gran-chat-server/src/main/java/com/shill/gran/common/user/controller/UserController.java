package com.shill.gran.common.user.controller;

import com.shill.gran.common.framworkEntiry.vo.response.ApiResult;
import com.shill.gran.common.user.domain.vo.response.user.UserInfoResp;
import com.shill.gran.common.user.service.UserService;
import com.shill.gran.common.user.vo.ModifyNameReq;
import com.shill.gran.common.utils.RequestHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/capi/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户详情")
    public ApiResult<UserInfoResp> getUserInfo(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @GetMapping("/name")
    @ApiOperation("获取用户详情")
    public ApiResult<Void> name(@RequestBody ModifyNameReq modifyNameReq){
        userService.modifyName(RequestHolder.get().getUid(),modifyNameReq.getName());
        return ApiResult.success();
    }

}

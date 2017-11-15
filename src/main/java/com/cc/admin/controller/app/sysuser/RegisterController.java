package com.cc.admin.controller.app.sysuser;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cc.admin.entity.Msg;
import com.cc.admin.util.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cc.admin.controller.base.BaseController;
import com.cc.admin.service.system.user.UserManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author CC
 * 用户注册控制器
 * 相关参数协议
 * 00 请求失败
 * 01请求成功
 * 02返回空值
 * 03请求协议参数不完整
 * 04用户名或密码错误
 *
 */
@Controller
@RequestMapping(value = "/appSysUser")
public class RegisterController extends BaseController{

    @Resource(name = "userService")
    private UserManager userService;

    /**
     * 系统用户注册接口
     */


    @RequestMapping(value = "/registerSysUser",method = RequestMethod.POST)
    @ResponseBody
    public Msg registerSysUser(){
        System.out.println("开始注册");
        Map<String,Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String result = "01";
        try {
            Session session = Jurisdiction.getSession();
            //判断验证码代写
            pd.put("USER_ID", this.get32UUID());
            pd.put("ROLE_ID", "3");                    //角色ID 3 为注册用户
            pd.put("NUMBER", "");                    //编号
            pd.put("PHONE", "");                    //手机号
            pd.put("BZ", "注册用户");                //备注
            pd.put("LAST_LOGIN", "");                //最后登录时间
            pd.put("IP", "");                        //IP
            pd.put("STATUS", "0");                    //状态
            pd.put("SKIN", "default");
            pd.put("RIGHTS", "");
            pd.put("PASSWORD", pd.get("PASSWORD"));
            if (null == userService.findByUsername(pd)) {
                userService.saveU(pd);
            }else {
                result = "04";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            map.put("result",result);
        }
        System.out.println("register result:" + map.get("result"));
        return Msg.success().add("result",result);
    }

}

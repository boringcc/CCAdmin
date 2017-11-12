package com.cc.admin.controller.system.login;

import com.cc.admin.controller.base.BaseController;
import com.cc.admin.entity.Msg;
import com.cc.admin.service.system.user.UserManager;
import com.cc.admin.service.system.user.impl.UserService;
import com.cc.admin.util.AppUtil;
import com.cc.admin.util.PageData;
import com.cc.admin.util.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.mail.Session;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController extends BaseController{

    @Resource(name = "userService")
    private UserManager userService;

    @RequestMapping(value = "/login_toLogin")
    public ModelAndView toLogin(){
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("system/index/login");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value = "/login_login",produces="application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public Object login() throws Exception {
        Map<String,String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo= "";
        String KEYDAYA[] = pd.getString("KEYDATA").split(",");
        if(null != KEYDAYA ){
            //验证码代写
            //判断用户名和密码
            String USERNAME = KEYDAYA[0];
            String PASSWORD = KEYDAYA[1];
            pd.put("USERNAME",USERNAME);
            pd.put("PASSWORD",PASSWORD);
            System.out.println("username: "+USERNAME +"  password: " + PASSWORD);
            pd = userService.getUserByNameAndPwd(pd);

            if(pd == null){
                errInfo = "usererror";
                logBefore(logger,USERNAME+"用户名或密码错误");
                return Msg.fail().add("result",errInfo);
            }
            if(Tools.isEmpty(errInfo)){
                errInfo = "success";
                logBefore(logger,USERNAME+"登录系统");
            }
        }
        System.out.println("33333");

        System.out.println("map.result: " + map.get("result"));
        return Msg.success().add("result",errInfo);
    }

}

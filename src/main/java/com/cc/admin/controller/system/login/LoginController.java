package com.cc.admin.controller.system.login;

import com.cc.admin.controller.base.BaseController;
import com.cc.admin.entity.Msg;
import com.cc.admin.entity.Page;
import com.cc.admin.entity.system.Menu;
import com.cc.admin.entity.system.Role;
import com.cc.admin.entity.system.User;
import com.cc.admin.service.system.menu.MenuManager;
import com.cc.admin.service.system.user.UserManager;
import com.cc.admin.service.system.user.impl.UserService;
import com.cc.admin.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import javax.naming.Name;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController extends BaseController{

    @Resource(name = "userService")
    private UserManager userService;

    @Resource(name = "menuService")
    private MenuManager menuService;


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
           Session  session= Jurisdiction.getSession();
            //验证码代写
            //判断用户名和密码
            String USERNAME = KEYDAYA[0];
            String PASSWORD = KEYDAYA[1];
            pd.put("USERNAME",USERNAME);
            pd.put("PASSWORD",PASSWORD);
            pd = userService.getUserByNameAndPwd(pd);


            if(pd != null) {
                this.removeSession(USERNAME);
                pd.put("LAST_LOGIN", DateUtil.getTime().toString());
                userService.updateLastLogin(pd);
                User user = new User();
                user.setUSER_ID(pd.getString("USER_ID"));
                user.setUSERNAME(pd.getString("USERNAME"));
                user.setPASSWORD(pd.getString("PASSWORD"));
                user.setNAME(pd.getString("NAME"));
                user.setRIGHTS(pd.getString("RIGHTS"));
                user.setROLE_ID(pd.getString("ROLE_ID"));
                user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
                user.setIP(pd.getString("IP"));
                user.setSTATUS(pd.getString("STATUS"));
                session.setAttribute(Const.SESSION_USER, user);            //把用户信息放session中
                //代写:验证码的session
                //shiro认证代写
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(USERNAME,PASSWORD);
                try {
                    subject.login(token);
                }catch (AuthenticationException e){
                    errInfo = "身份验证失败！";
                }
            }
            else{
                errInfo = "usererror";
                logBefore(logger,USERNAME+"用户名或密码错误");
                return Msg.fail().add("result",errInfo);
            }
            if(Tools.isEmpty(errInfo)){
                errInfo = "success";
                logBefore(logger,USERNAME+"登录系统");
            }
        }
        return Msg.success().add("result",errInfo);
    }

    /**
     * 访问系统的首页
     */
    @RequestMapping(value = "/main/{changeMenu}")
    public ModelAndView login_index(@PathVariable("changeMenu") String  changeMenu){
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            Session session = Jurisdiction.getSession();
            User user = (User)session.getAttribute(Const.SESSION_USER);
            if(user != null){
                //这里主要是要得到带角色信息的User
                User userRol = (User) session.getAttribute(Const.SESSION_USERROL);
                if(null == userRol){
                    user = userService.getUserAndRoleById(user.getUSER_ID());
                    session.setAttribute(Const.SESSION_USERROL, user);
                }else{
                    user = userRol;
                }
                String USERNAME = user.getUSERNAME();
                Role role = user.getRole();
                String roleRighs = role!=null ? role.getRIGHTS() : "";
                session.setAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS , roleRighs);
                session.setAttribute(Const.SESSION_USERNAME, USERNAME);
                //用户机构权限以后写this.setAttributeToAllDEPARTMENT_ID(session, USERNAME);
                List<Menu> allMenuList = new ArrayList<Menu>();

            }

        }catch (Exception e){

        }


        return  mv;
    }

    /**
     * 菜单缓存
     */
    @SuppressWarnings("unchecked")
    public List<Menu> getAttributeMenu(Session session,String USERNAME, String roleRights){
        List<Menu> allMenuList = new ArrayList<Menu>();
        if(null == session.getAttribute(USERNAME+ Const.SESSION_allmenuList)){
            //allMenuList =
        }

        return allMenuList;
    }

    /**
     * 清理session
     */
    public void removeSession(String USERNAME){
        Session session = Jurisdiction.getSession();	//以下清除session缓存
        session.removeAttribute(Const.SESSION_USER);
        session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
        session.removeAttribute(USERNAME + Const.SESSION_allmenuList);
        session.removeAttribute(USERNAME + Const.SESSION_menuList);
        session.removeAttribute(USERNAME + Const.SESSION_QX);
        session.removeAttribute(Const.SESSION_userpds);
        session.removeAttribute(Const.SESSION_USERNAME);
        session.removeAttribute(Const.SESSION_USERROL);
        session.removeAttribute("changeMenu");
        session.removeAttribute("DEPARTMENT_IDS");
        session.removeAttribute("DEPARTMENT_ID");
    }
}

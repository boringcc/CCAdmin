package com.cc.admin.controller.system.login;

import com.cc.admin.controller.base.BaseController;
import com.cc.admin.entity.Msg;
import com.cc.admin.entity.Page;
import com.cc.admin.entity.system.Menu;
import com.cc.admin.entity.system.Role;
import com.cc.admin.entity.system.User;
import com.cc.admin.service.system.buttonrights.ButtonrightsManager;
import com.cc.admin.service.system.fhbutton.FhbuttonManager;
import com.cc.admin.service.system.menu.MenuManager;
import com.cc.admin.service.system.role.RoleManager;
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
import javax.jws.soap.SOAPBinding;
import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;


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
    @Resource(name = "roleService")
    private RoleManager roleService;
    @Resource(name = "buttonrightsService")
    private ButtonrightsManager buttonrightsService;
    @Resource(name = "fhbuttonService")
    private FhbuttonManager fhbuttonService;



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
                allMenuList = this.getAttributeMenu(session,USERNAME,roleRighs);
                List<Menu> menuList = new ArrayList<Menu>();
                menuList = this.changeMenuF(allMenuList, session, USERNAME , changeMenu);
                if(null == session.getAttribute(USERNAME + Const.SESSION_QX)){
                    session.setAttribute(USERNAME + Const.SESSION_QX, this.getUQX(USERNAME));
                }
                this.getRemortIP(USERNAME);
                mv.setViewName("system/index/main");
                mv.addObject("user", user);
                mv.addObject("menuList",menuList);
            }else {
                mv.setViewName("system/index/login");
            }

        }catch (Exception e){
            e.printStackTrace();
            mv.setViewName("system/index/login");
        }
        pd.put("SYSNAME",Tools.readTxtFile(Const.SYSNAME));
        mv.addObject("pd",pd);
        return  mv;
    }

    /**
     * 菜单缓存
     */
    @SuppressWarnings("unchecked")
    public List<Menu> getAttributeMenu(Session session,String USERNAME, String roleRights) throws Exception {
        List<Menu> allMenuList = new ArrayList<Menu>();
        if(null == session.getAttribute(USERNAME+ Const.SESSION_allmenuList)){
            //MENU_ID=0 代表所有的按钮
            allMenuList = menuService.listAllMenu("0");
            //权限判断先放一边，先做出大概的效果
        //    if(Tools.notEmpty(roleRights)){
        //        allMenuList = this.readMenu(allMenuList,roleRights);
        //    }
            session.setAttribute(USERNAME + Const.SESSION_allmenuList,allMenuList);
        }else {
            allMenuList = (List<Menu>) session.getAttribute(USERNAME+Const.SESSION_allmenuList);
        }
        return allMenuList;
    }

    /**切换菜单处理
     * @param allmenuList
     * @param session
     * @param USERNAME
     * @param changeMenu
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Menu> changeMenuF(List<Menu> allmenuList,Session session,String USERNAME, String changeMenu){
        List<Menu> menuList = new ArrayList<Menu>();
        if(null == session.getAttribute(USERNAME + Const.SESSION_menuList)|| ("yes".equals(changeMenu))) {
            List<Menu> menuList1 = new ArrayList<Menu>();
            List<Menu> menuList2 = new ArrayList<Menu>();
            for (int i = 0;i < allmenuList.size();i++){
                Menu menu =allmenuList.get(i);
                //1是系统类型  2 是业务类型
                if("1".equals(menu.getMENU_TYPE())){
                    menuList1.add(menu);
                }else {
                    menuList2.add(menu);
                }
            }
            session.removeAttribute(USERNAME + Const.SESSION_menuList);
            if("2".equals(session.getAttribute("changeMenu"))){
                session.setAttribute(USERNAME + Const.SESSION_menuList,menuList1);
                session.removeAttribute("changeMenu");
                session.setAttribute("changeMenu","1");
                menuList = menuList1;
            }else {
                session.setAttribute(USERNAME + Const.SESSION_menuList,menuList2);
                session.removeAttribute("changeMenu");
                session.setAttribute("changeMenu","2");
                menuList = menuList2;
            }
        }else {
            menuList = (List<Menu>) session.getAttribute(USERNAME + Const.SESSION_menuList);
        }
        return menuList;
    }

    @RequestMapping(value = "/tab")
    public String tab(){
        return "system/index/tab";
    }


    /**
    * 进入首页后的默认页面
    * @return
    * @throws Exception
    */
    @RequestMapping(value = "/login_default")
    public ModelAndView defaultPage() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd.put("userCount", Integer.parseInt(userService.getUserCount("").get("userCount").toString())-1);
        //会员暂时放一边，做个虚假的人数
        pd.put("appUserCount", 10);
        mv.addObject("pd",pd);
        mv.setViewName("system/index/default");
        return mv;
    }

    /**用户注销
     *
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/logout")
    public ModelAndView logout() throws Exception{
        ModelAndView mv =  this.getModelAndView();
        String USERNAME = Jurisdiction.getUsername();
        PageData pd = new PageData();
        //清除缓存
        this.removeSession(USERNAME);
        //shiro销毁登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        pd = this.getPageData();
        pd.put("msg", pd.getString("msg"));
        //pd = this.setLoginPd(pd); //设置登录页面的参数配置
        mv.setViewName("system/index/login");
        mv.addObject("pd",pd);
        return mv;
    }

    /**
     *设置登录页面的配置参数
     * @param pd
     * @return
     */
    public PageData setLoginPd(PageData pd){
        pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME));
        String strLOGINEDIT = Tools.readTxtFile(Const.LOGINEDIT);
        if(null != strLOGINEDIT && !"".equals(strLOGINEDIT)){
            String strLo[] = strLOGINEDIT.split("fh");
            if(strLo.length == 2){
                pd.put("isZhuce", strLo[0]);
                pd.put("isMusic", strLo[1]);
            }
        }
        return pd;
    }

    /**
     * 获取用户权限
     * @param USERNAME
     * @return
     */


    public Map<String,String> getUQX(String USERNAME){
        PageData pd = new PageData();
        Map<String,String> map = new HashMap<String, String>();
        try{
            pd.put(Const.SESSION_USERNAME, USERNAME);
            pd.put("ROLE_ID",userService.findByUsername(pd).get("ROLE_ID").toString());
            pd = roleService.findObjectById(pd);
            map.put("adds",pd.getString("ADD_QX"));    //增
            map.put("dels", pd.getString("DEL_QX"));	//删
            map.put("edits", pd.getString("EDIT_QX"));	//改
            map.put("chas", pd.getString("CHA_QX"));	//查
            List<PageData> buttonQXnamelist = new ArrayList<PageData>();
            if("admin".equals(USERNAME)){
                buttonQXnamelist = fhbuttonService.listAll(pd);
            }else {
                buttonQXnamelist = buttonrightsService.listAllBrAndQxname(pd);
            }
            for(int i = 0;i < buttonQXnamelist.size(); i++){
                map.put(buttonQXnamelist.get(i).getString("QX_NAME"),"1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /** 更新登录用户的IP
     * @param USERNAME
     * @throws Exception
     */
    public void getRemortIP(String USERNAME) throws Exception {
        PageData pd = new PageData();
        HttpServletRequest request = this.getRequest();
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader("x-forwarded-for");
        }
        pd.put("USERNAME", USERNAME);
        pd.put("IP", ip);
        userService.saveIP(pd);
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

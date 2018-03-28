package com.cc.admin.controller.system.user;

import com.cc.admin.controller.base.BaseController;
import com.cc.admin.entity.Page;
import com.cc.admin.entity.system.Role;
import com.cc.admin.service.system.fhlog.FHlogManager;
import com.cc.admin.service.system.menu.MenuManager;
import com.cc.admin.service.system.role.RoleManager;
import com.cc.admin.service.system.user.UserManager;
import com.cc.admin.util.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.fileupload.FileUpload;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类名:UserController
 * 用户的一些基础操作
 * 创建人:cc
 * 更新时间:2018-03-27
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController{
       String menuUrl = "user/listUser.do";
    @Resource(name = "userService")
       private UserManager userService;
    @Resource(name = "roleService")
       private RoleManager roleService;
       @Resource(name = "menuService")
       private MenuManager menuService;

    /**
     * 用户列表查询
      * @param page
     * @return
     */
    @RequestMapping(value = "/listUsers")
    public ModelAndView listUsers(Page page) throws Exception {

        System.out.println("page :" + page.getCurrentPage());
        System.out.println("page :" + page.getTotalPage());
        System.out.println("page :" + page.getCurrentResult());
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");				//关键词检索条件
        if(null != keywords && !"".equals(keywords)){
            pd.put("keywords", keywords.trim());
        }
        String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
        String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
        if(lastLoginStart != null && !"".equals(lastLoginStart)){
            pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
        }
        if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
            pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
        }
        page.setPd(pd);
        List<PageData>	userList = userService.listUsers(page);	//列出用户列表
        System.out.println("userList :" + userList.toString());
        pd.put("ROLE_ID", "1");
        List<Role> roleList = roleService.listAllRolesByPId(pd);//列出所有系统用户角色
        System.out.println("roleList :" + roleList.toString());
        mv.setViewName("system/user/user_list");
        mv.addObject("userList", userList);
        mv.addObject("roleList", roleList);
        mv.addObject("pd", pd);
           //权限的以后再搞
           return mv;
    }

    /**显示用户列表(弹窗选择用)
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/listUsersForWindow")
    public ModelAndView listUsersForWindow(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");				//关键词检索条件
        if(null != keywords && !"".equals(keywords)){
            pd.put("keywords", keywords.trim());
        }
        String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
        String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
        if(lastLoginStart != null && !"".equals(lastLoginStart)){
            pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
        }
        if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
            pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
        }
        page.setPd(pd);
        List<PageData>	userList = userService.listUsersBystaff(page);	//列出用户列表(弹窗选择用)
        pd.put("ROLE_ID", "1");
        List<Role> roleList = roleService.listAllRolesByPId(pd);//列出所有系统用户角色
        mv.setViewName("system/user/window_user_list");
        mv.addObject("userList", userList);
        mv.addObject("roleList", roleList);
        mv.addObject("pd", pd);
        mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
        return mv;
    }


    /**
     * 修改用户
     */
    @RequestMapping(value = "/editU")
    public ModelAndView editU()throws Exception{
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        //判断是不是本用户修改自己的数据
        if(!Jurisdiction.getUsername().equals(pd.getString("USERNAME"))) {
            if ("admin".equals(pd.getString("USERNAME")) && !"admin".equals(Jurisdiction.getUsername())) {
                return null;
            }
        }
        else {
            //如果当前登录用户修改用户资料提交的用户名是本人，则不能修改本人的角色ID
            pd.put("ROLE_ID",userService.findByUsername(pd).getString("ROLE_ID"));//对角色ID还原本人角色ID
        }
        if(pd.getString("PASSWORD")!=null && !"".equals(pd.getString("PASSWORD"))){
            pd.put("PASSWORD",new SimpleHash("SHA-1", pd.getString("USERNAME"),pd.getString("PASSWORD")).toString());
        }
        userService.editU(pd);
        mv.addObject("msg","success");
        mv.setViewName("sava_result");
        return mv;
    }

    /**
     * 去修改用户界面(个人信息修改)，传的是个人信息
     * @return
     */
    @RequestMapping(value = "/goEditMyU")
    public ModelAndView goEditMyU() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        mv.addObject("fx", "head");
        pd.put("ROLE_ID","1");
        List<Role> roleList = roleService.listAllRolesByPId(pd);
        pd.put("USERNAME", Jurisdiction.getUsername());
        pd = userService.findByUsername(pd);
        mv.setViewName("system/user/user_edit");
        mv.addObject("msg","editU");
        mv.addObject("pd",pd);
        mv.addObject("roleList",roleList);
        return mv;
    }

    /**
     * 该操作为系统管理员修改所以传的数据为所有角色的
     * 去修改页面 msg为editU，告诉前端是要进行修改操作的
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goEditU")
    public ModelAndView goEditU() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if("1".equals(pd.getString("USER_ID"))) {return null;};
        pd.put("ROLE_ID","1");
        List<Role> roleList = roleService.listAllRolesByPId(pd);
        mv.addObject("fx","head");
        mv.setViewName("system/user/user_edit");
        mv.addObject("msg","editU");
        mv.addObject("pd",pd);
        mv.addObject("roleList",roleList);
        return mv;
    }


    /**去修改用户页面(在线管理页面打开)
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/goEditUfromOnline")
    public ModelAndView goEditUfromOnline() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if("admin".equals(pd.getString("USERNAME"))){return null;}	//不能查看admin用户
        pd.put("ROLE_ID", "1");
        List<Role> roleList = roleService.listAllRolesByPId(pd);	//列出所有系统用户角色
        pd = userService.findByUsername(pd);						//根据ID读取
        mv.setViewName("system/user/user_edit");
        mv.addObject("msg", "editU");
        mv.addObject("pd", pd);
        mv.addObject("roleList", roleList);
        return mv;
    }

    /**
     * 保存用户
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveU")
    public ModelAndView saveU() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("USER_ID", this.get32UUID());
        pd.put("LAST_LOGIN", "");
        pd.put("IP","");
        pd.put("STATUS","0");
        pd.put("SKIN","default");
        pd.put("RIGHTS","");
        //利用shiro的SimpleHash对密码进行加密
        pd.put("PASSWORD", new SimpleHash("SHA-1",pd.getString("USERNAME"),pd.getString("PASSWORD")).toString());
        if(null == userService.findByUsername(pd)){//判断用户名是否存在
            userService.saveU(pd);
            mv.addObject("msg","success");
        }else {
            mv.addObject("msg","failed");
        }
        mv.setViewName("save_result");
        return mv;
    }



    /**
     * 删除用户
     * @throws Exception
     */
    @RequestMapping(value = "/deleteU")
    public void deleteU(PrintWriter out) throws Exception{
        logBefore(logger,Jurisdiction.getUsername()+ "删除user");
        PageData pd = new PageData();
        pd = this.getPageData();
        userService.deleteU(pd);
        out.write("success");//这里要先前端传输一个data这样刷新页面才能生效
        out.close();
    }

    /**
     * 批量删除
     */
    @RequestMapping(value = "/deleteAllU")
    @ResponseBody
    public Object deleteAll() throws Exception {
        logBefore(logger,Jurisdiction.getUsername()+"批量删除user");
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String,Object> map = new HashMap<String, Object>();
        List<PageData> pdList = new ArrayList<PageData>();
        String USER_IDS = pd.getString("USER_IDS");
        if(null!=USER_IDS&&!"".equals(USER_IDS)){
            String ArrayUSER_IDS [] = USER_IDS.split(",");
            userService.deleteAllU(ArrayUSER_IDS);
            pd.put("msg","ok");
        }else {
            pd.put("msg","no");
        }
        pdList.add(pd);
        map.put("list",pdList);
        return AppUtil.returnObject(pd,map);
    }


    /**
     * 去新增用户界面 msg为savaU告诉前端要进行保存操作
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goAddU.do")
    public ModelAndView goAddU() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("ROLE_ID","1");
        List<Role> roleList = roleService.listAllRolesByPId(pd);
        mv.setViewName("system/user/user_edit");
        mv.addObject("msg","savaU");
        mv.addObject("pd",pd);
        mv.addObject("roleList",roleList);
        return mv;
    }



    /**
     * 查看用户
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/view")
    public ModelAndView view()throws Exception{
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        //不能查看admin的
        if("admin".equals(pd.getString("USERNAME"))){return null;}
        //将ROLE_ID设为1
        pd.put("ROLE_ID","1");
        //通过id列出所有系统用户角色
        List<Role> roleList = roleService.listAllRolesByPId(pd);
        //通过username查找出来存入pd
        pd = userService.findByUsername(pd);
        //设置
        mv.setViewName("system/user/user_view");
        mv.addObject("msg","editU");
        mv.addObject("pd",pd);
        mv.addObject("roleList",roleList);
        return mv;
    }



    /**导出用户信息到EXCEL,这个暂时不写
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() throws Exception{
        ModelAndView mv = this.getModelAndView();
        return mv;
    }

    /**打开上传EXCEL页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/goUploadExcel")
    public ModelAndView goUploadExcel()throws Exception{
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("system/user/uploadexcel");
        return mv;
    }

    /**下载模版
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/downExcel")
    public void downExcel(HttpServletResponse response)throws Exception{
    }

    /**从EXCEL导入到数据库
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/readExcel")
    public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
        ModelAndView mv = this.getModelAndView();
        return mv;
    }

    /**
     * initbinder解决类型转换问题
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
    }

    /**
     * 判断用户名是否已经存在
     * @return
     */
    @RequestMapping(value = "/hasU")
    @ResponseBody
    public Object hasU(){
        Map<String,String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
        pd= this.getPageData();
        //用户名已经存在
        if(userService.findByUsername(pd) != null) {
            errInfo = "error";
            }
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
        map.put("result",errInfo);
        return AppUtil.returnObject(new PageData(),map);
    }



    /**
     * 判断该用户是否存在
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hasN")
    @ResponseBody
    public Object hasN() throws Exception{
        Map<String,String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd= this.getPageData();
            //
            if(userService.findByUN(pd) != null) {
                errInfo = "error";
            }
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
        map.put("result",errInfo);
        return AppUtil.returnObject(new PageData(),map);
    }

    /**
     * 判断邮箱是否存在
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hasE")
    @ResponseBody
    public Object hasE() throws Exception{
        Map<String,String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd= this.getPageData();
            //
            if(userService.findByUE(pd) != null) {
                errInfo = "error";
            }
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
        map.put("result",errInfo);
        return AppUtil.returnObject(new PageData(),map);

    }


    //发站内信<%=basePath%>fhsms/goAdd.do?username='+username
    //去发邮件<%=basePath%>head/goSendEmail.do?EMAIL='+EMAIL
    //去发短信页面<%=basePath%>head/goSendSms.do?PHONE='+phone+'&msg=appuser';
    //批量删除<%=basePath%>user/deleteAllU.do?tm='+new Date().getTime(),
    //view user <%=basePath%>user/view.do?USERNAME='+USERNAME;
    //导出EXCEL表<%=basePath%>user/excel.do?keywords='+keywords+'&lastLoginStart='+lastLoginStart+'&lastLoginEnd='+lastLoginEnd+'&ROLE_ID='+ROLE_ID;








}

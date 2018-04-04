package com.cc.admin.controller.jiaowu;

import com.cc.admin.util.JiaoWuUtil;
import com.cc.admin.controller.base.BaseController;
import com.cc.admin.entity.Page;
import com.cc.admin.util.Jurisdiction;
import com.cc.admin.util.PageData;
import org.apache.http.HttpRequest;
import org.apache.shiro.session.Session;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/jiaowu")
public class JiaowuLoginController extends BaseController{

    @RequestMapping(value = "/toLoginPage")
    public ModelAndView toLoginPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jiaoWu/index/testlogin");
        return mv;
    }

    @RequestMapping(value = "/login")
    public ModelAndView login() throws Exception {
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String KEYDAYA[] = pd.getString("KEYDATA").split(",");
        if(null != KEYDAYA ) {
            Session session = Jurisdiction.getSession();
            //判断用户名和密码
            String USERNAME = KEYDAYA[0];
            String PASSWORD = KEYDAYA[1];
            pd.put("USERNAME", USERNAME);
            pd.put("PASSWORD", PASSWORD);
            //System.out.println(JiaoWuUtil.loginJWXT(USERNAME,PASSWORD));
        }
        return mv;
    }

    @RequestMapping(value = "/test")
    public void test() throws Exception {
        JiaoWuUtil.loginJWXT();
    }




}

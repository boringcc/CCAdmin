package com.cc.admin.controller.jiaowu;

import com.cc.admin.entity.ScorePage;
import com.cc.admin.util.*;
import com.cc.admin.controller.base.BaseController;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cc.admin.util.JiaoWuUtil.*;

@Controller
@RequestMapping(value = "/jiaowu")
public class JiaowuLoginController extends BaseController{

    @RequestMapping(value = "/toLoginPage")
    public ModelAndView toLoginPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jiaoWu/index/jwLogin");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String,String> login() throws Exception {
        Map<String,String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String result = "";
        String KEYDAYA[] = pd.getString("KEYDATA").split(",");
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(null != KEYDAYA ) {
            Session session = Jurisdiction.getSession();
            //判断用户名和密码
            String USERNAME = KEYDAYA[0];
            String PASSWORD = KEYDAYA[1];
            pd.put("USERNAME", USERNAME);
            pd.put("PASSWORD", PASSWORD);
            resultMap =  JiaoWuUtil.loginJWXT(pd);
            //获取成绩
            //List<ScorePage> scoreInfo =  getScoreInfo((CloseableHttpClient)resultMap.get("httpClient"),(String) resultMap.get("Cookie"));
        }
        if("用户名或密码错误,请联系本院教务老师!" == resultMap.get("error")){
            result = "usererror";
        }else if("验证码错误!!" == resultMap.get("error")){
            result = "codeerror";
        }else {
            result = "success";
        }
        map.put("result",result);
        return map;
    }

    @RequestMapping(value = "/index")
    public ModelAndView toIndexPage() throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jiaoWu/index/main");
        PageData pd = new PageData();
        pd = this.getPageData();
        List<String> info = getIndexInfo(httpClient,cookie);
        String sName = info.get(0);
        String xueHao = info.get(1);
        pd.put("sName",sName);
        pd.put("xueHao",xueHao);
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value = "/chaScore")
    public ModelAndView chaAllScore() throws IOException {
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd  = new PageData();
        pd = this.getPageData();
        String chaAll = "http://jwxt.xtu.edu.cn/jsxsd/kscj/cjcx_list?xq=null";
        List<ScorePage> scoreList =getScoreInfo(httpClient,cookie,chaAll);
        pd.put("scoreList",scoreList);
        mv.addObject("pd",pd);
        mv.setViewName("jiaoWu/score/score_list");
        return mv;
    }

    @RequestMapping(value = "/login_default")
    public ModelAndView defaultPage(){
        ModelAndView mv = new ModelAndView();
        mv= this.getModelAndView();
        PageData pd = new PageData();
        pd =this.getPageData();
        //名字，一卡通余额，图书馆欠费，图书借阅，今日课程

        mv.setViewName("jiaoWu/score/default");
        return mv;
    }


}

package com.cc.admin.controller.fhoa.fhfile;

import com.cc.admin.controller.base.BaseController;
import com.cc.admin.entity.Page;
import com.cc.admin.service.fhod.fhfile.CcfileManager;
import com.cc.admin.util.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/fhfile")
public class CcfileController extends BaseController{

    String menuUrl = "fhfile/list.do";//菜单地址（权限用）现在不用

    @Resource(name = "ccfileService")
    private CcfileManager ccfileService;

    /**
     * 得到文件列表
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception{
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");
        if(null != keywords && !"".equals("keywords")){
            pd.put("keywords",keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = ccfileService.list(page);  //列出file列表
        List<PageData> nvarList = new ArrayList<PageData>();
        for(int i=0;i < varList.size();i++){
            PageData npd = new PageData();
            String FILEPATH = varList.get(i).getString("FILEPATH");
            String Extension_name = FILEPATH.substring(20,FILEPATH.length());
            String fileType = "file";
            int zindex1 = "java,php,jsp,html,css,txt,asp".indexOf(Extension_name);
            if(zindex1 != -1){
                fileType = "wenben";	//文本类型
            }
            int zindex2 = "jpg,gif,bmp,png".indexOf(Extension_name);
            if(zindex2 != -1){
                fileType = "tupian";	//图片文件类型
            }
            int zindex3 = "rar,zip,rar5".indexOf(Extension_name);
            if(zindex3 != -1){
                fileType = "yasuo";		//压缩文件类型
            }
            int zindex4 = "doc,docx".indexOf(Extension_name);
            if(zindex4 != -1){
                fileType = "doc";		//doc文件类型
            }
            int zindex5 = "xls,xlsx".indexOf(Extension_name);
            if(zindex5 != -1){
                fileType = "xls";		//xls文件类型
            }
            int zindex6 = "ppt,pptx".indexOf(Extension_name);
            if(zindex6 != -1){
                fileType = "ppt";		//ppt文件类型
            }
            int zindex7 = "pdf".indexOf(Extension_name);
            if(zindex7 != -1){
                fileType = "pdf";		//ppt文件类型
            }
            npd.put("fileType", fileType);								 	//用于文件图标
            npd.put("FHFILE_ID", varList.get(i).getString("FHFILE_ID"));	//唯一ID
            npd.put("NAME", varList.get(i).getString("NAME"));				//文件名
            npd.put("FILEPATH", FILEPATH);									//文件名+扩展名
            npd.put("CTIME", varList.get(i).getString("CTIME"));			//上传时间
            npd.put("USERNAME", varList.get(i).getString("USERNAME"));		//用户名
            npd.put("FILESIZE", varList.get(i).getString("FILESIZE"));		//文件大小
            npd.put("BZ", varList.get(i).getString("BZ"));					//备注
            nvarList.add(npd);
        }
        mv.setViewName("fhoa/fhfile/fhfile_list");
        mv.addObject("varList", nvarList);
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 去新增页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goAdd")
    public ModelAndView goAdd()throws Exception {
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("fhoa/fhfile/fhfile_edit");
        mv.addObject("msg","save");
        mv.addObject("pd",pd);
        return mv;
    }

    /**
     * 保存
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("FHFILE_ID", this.get32UUID());
        pd.put("CTIME" , Tools.date2Str(new Date())); //上传时间
        pd.put("USERNAME" , Jurisdiction.getUsername());
        pd.put("FILESIZE", FileUtil.getFilesize(PathUtil.getClasspath() + Const.FILEPATHFILEOA + pd.getString("FILEPATH")));
        ccfileService.save(pd);
        mv.addObject("msg","success");
        mv.setViewName("save_result");
        return mv;

    }

    /**
     * 删除
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) throws Exception{
        logBefore(logger,Jurisdiction.getUsername()+"删除file文件");
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = ccfileService.findById(pd);
        ccfileService.delete(pd);    //这个是删除数据库的，下面是删除硬盘中的
        DelAllFile.delAllFile(PathUtil.getClasspath()+ Const.FILEPATHFILEOA + pd.getString("FILEPATH"));
        out.write("success");
        out.close();
    }

    /**
     * 去预览txt,java,php等文本文件
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goViewTxt")
    public ModelAndView goViewTxt() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String encoding = pd.getString("encoding");
        pd = ccfileService.findById(pd);
        String code = Tools.readTxtFileAll(Const.FILEPATHFILEOA+pd.getString("FILEPATH"),encoding);
        pd.put("code",code);
        mv.setViewName("fhoa/fhfile/fhfile_view_txt");
        mv.addObject("pd",pd);
        return mv;
    }

    /**去预览pdf文件页面
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/goViewPdf")
    public ModelAndView goViewPdf()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = ccfileService.findById(pd);
        mv.setViewName("fhoa/fhfile/fhfile_view_pdf");
        mv.addObject("pd", pd);
        return mv;
    }

    public Object deleteAll() throws Exception{
        logBefore(logger,Jurisdiction.getUsername() + "批量删除Fhfile");
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String,Object> map = new HashMap<String,Object>();
        String DATA_IDS = pd.getString("DATA_IDS");
        List<PageData> pdList = new ArrayList<PageData>();
        if(null != DATA_IDS && !"".equals(DATA_IDS)){
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            PageData fpd = new PageData();
            for(int i=0;i<ArrayDATA_IDS.length;i++){
                fpd.put("FHFILE_ID", ArrayDATA_IDS[i]);
                fpd = ccfileService.findById(fpd);
                DelAllFile.delFolder(PathUtil.getClasspath()+ Const.FILEPATHFILEOA + fpd.getString("FILEPATH")); //删除物理文件
            }
            ccfileService.deleteAll(ArrayDATA_IDS);		//删除数据库记录
            pd.put("msg", "ok");
        }else{
            pd.put("msg", "no");
        }
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }

    /**下载
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/download")
    public void downExcel(HttpServletResponse response)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = ccfileService.findById(pd);
        String fileName = pd.getString("FILEPATH");
        FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILEOA + fileName, pd.getString("NAME")+fileName.substring(19, fileName.length()));
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
    }

}













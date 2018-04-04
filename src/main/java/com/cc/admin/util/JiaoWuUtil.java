package com.cc.admin.util;

import com.cc.admin.entity.ScorePage;
import com.cc.admin.test.MyCodeUtil;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import com.cc.admin.util.CommonUtil;
import javax.sound.midi.Soundbank;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.cc.admin.util.CommonUtil.downloadImage;


public class JiaoWuUtil {

    //String username, String password



    public static void  loginJWXT() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String data = null;
        String cookie = getCookie("http://jwxt.xtu.edu.cn/jsxsd",httpClient);
        HttpPost httpPost = new HttpPost("http://jwxt.xtu.edu.cn/jsxsd/xk/LoginToXk?flag=sess");
        httpPost.addHeader(new BasicHeader("Cookie",cookie));
        try {
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed:" + response.getStatusLine());
            } else {
                String resultStr = EntityUtils.toString(response.getEntity());
                JSONObject resultJSON = JSONObject.fromObject(resultStr);
                data = resultJSON.getString("data");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        //下载验证码图片
        getYZCode(httpClient,cookie);
        //模拟登陆
        //验证码识别
        String yzmcode = MyCodeUtil.getCode("img/code/code.jsp");
        String encoded = getEncoded(data);
        HttpPost httpPost2 = new HttpPost("http://jwxt.xtu.edu.cn/jsxsd/xk/LoginToXk");
        httpPost2.addHeader(new BasicHeader("Cookie",cookie));
        ArrayList<NameValuePair> par = new ArrayList<NameValuePair>();
        par.add(new BasicNameValuePair("USERNAME","2015551514"));
        par.add(new BasicNameValuePair("PASSWORD","134679852"));
        par.add(new BasicNameValuePair("encoded",encoded));
        par.add(new BasicNameValuePair("RANDOMCODE",yzmcode));
        httpPost2.setEntity(new UrlEncodedFormEntity(par));
        String resultStr = "";
        try {
            HttpResponse response = httpClient.execute(httpPost2);
            resultStr = EntityUtils.toString(response.getEntity());
        }catch (IOException e) {
            e.printStackTrace();
        }
        String errorPattern = "<font color=\"red\">(.*?)</font>";
        Pattern errorP = Pattern.compile(errorPattern);
        Matcher errorM = errorP.matcher(resultStr);
        if(errorM.find()){
            System.out.println(errorM.group(1));
           //return errorM.group(1);
        }else {
            System.out.println("success");
            //return "success";
        }
    }


    public static void getInfo(CloseableHttpClient httpClient,String cookie) throws IOException {
        HttpGet acchttpGet = new HttpGet("http://jwxt.xtu.edu.cn/jsxsd/kscj/cjcx_list?xq=2016-2017-2");
        acchttpGet.addHeader(new BasicHeader("Cookie",cookie));
        HttpResponse accresponse = httpClient.execute(acchttpGet);
        String resultStr = EntityUtils.toString(accresponse.getEntity());
        String pattern = "(<tr>\\s+<td>.*?</td>\\s+<td>.*?</td>\\s+<td align=\"left\">.*?</td>\\s+<td style=\" \"><a href=\"javascript:JsMod.*?\">.*?</a></td>\\s+<td>.*?</td>\\s+<td>.*?</td>\\s+<td>.*?</td>\\s+<td>.*?</td>\\s+<td>.*?</td>\\s+</tr>)";
        //1:序号 2:学期 3:课程名字 4：成绩 5:学分 6：总学时 7:考核方式 8:课程属性 9:课程性质
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(resultStr);
        List<String> result = new ArrayList<String>();
        while(m.find()){
            result.add(m.group());
        }
        String resultpart1 = result.get(0);
        String pattern2 =  "<tr>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td align=\"left\">(.*?)</td>\\s+<td style=\" \"><a href=\"javascript:JsMod.*?\">(.*?)</a></td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+</tr>";
        String pattern3 = "<td align=\"left\">(.*?)</td>";
        for(int i = 0;i < 1;i++) {
            Pattern r1 = Pattern.compile(pattern2);
            Matcher m1 = r1.matcher(resultpart1);
            List<String> result1 = new ArrayList<String>();
            m1.find();
            for(int j = 1;j <= 9 ;j++){
                result1.add(m1.group(j));
            }
            //for(int j = 1;j <= 9 ;j++){
            //    result1.add(m1.group(j));
            //}
            ScorePage scorePage = new ScorePage(result1.get(0),result1.get(1),result1.get(2),result1.get(3),result1.get(4),result1.get(5),result1.get(6),result1.get(7),result1.get(8));
            System.out.println("score: " + scorePage.toString());
        }
    }

    //下载验证码图片
    public  static void getYZCode(CloseableHttpClient httpClient,String cookie) throws IOException {
        final HttpGet httpGet = new HttpGet("http://jwxt.xtu.edu.cn/jsxsd/verifycode.servlet");
        httpGet.addHeader(new BasicHeader("Cookie",cookie));
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
        // 请求http
        final HttpResponse codeResponse = httpClient.execute(httpGet);
        if (codeResponse.getStatusLine().getStatusCode() != org.apache.http.HttpStatus.SC_OK) {
            System.err.println("Method failed: " + codeResponse.getStatusLine());
        }
        String picName = "img/code";
        final File f = new File(picName);
        f.mkdirs();
        picName += "/code.jpg";
        final InputStream inputStream = codeResponse.getEntity().getContent();
        final OutputStream outStream = new FileOutputStream(picName);
        IOUtils.copy(inputStream, outStream);
        inputStream.close();
        outStream.close();
        httpGet.releaseConnection();
    }

    public static String getCookie(String url, CloseableHttpClient httpClient){
        //HttpPost httpPost = null;
        HttpGet httpGet = null;
        String result = null;
        try {
            CookieStore cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            httpGet = new HttpGet(url);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            httpClient.execute(httpGet);
            List<Cookie> cookies = cookieStore.getCookies();
            result = cookies.get(0).getValue().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getEncoded(String data){
        String encoded = "";
        String scode = data.split("#")[0];
        String sxh = data.split("#")[1];
        //document.getElementById("Form1").USERNAME.value+'%%%'+document.getElementById("Form1").PASSWORD.value;
        String code="2015551514" + "%%%" + "134679852";

        for(int i=0;i<code.length();i++){
            if(i<20){
                encoded = encoded+code.substring(i,i+1)+scode.substring(0,Integer.parseInt(sxh.substring(i,i+1)));
                scode = scode.substring(Integer.parseInt(sxh.substring(i,i+1)),scode.length());
            }else{
                encoded=encoded+code.substring(i,code.length());
                i=code.length();
            }
        }
        System.out.println("encoded :" + encoded);
        return encoded;
    }
}

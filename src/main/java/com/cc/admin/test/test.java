package com.cc.admin.test;

import com.cc.admin.entity.ScorePage;
import com.cc.admin.util.MyCodeUtil;
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.cc.admin.util.CommonUtil.downloadImage;


public class test {

    @Test
    public void testZhenZe(){
        String test = "<tr>\n" +
                "            <td>2</td>\n" +
                "            <td>2016-2017-1</td>\n" +
                "            <td align=\"left\">计算机组成原理Ⅰ</td>\n" +
                "            \n" +
                "            <td style=\" \"><a href=\"javascript:JsMod('/jsxsd/kscj/pscj_list.do?xs0101id=2015551514&jx0404id=20160200000090810000462701&zcj=74',700,500)\">74</a></td>\n" +
                "            \n" +
                "            \n" +
                "            <td>4</td>\n" +
                "            <td>64</td>\n" +
                "            <td>考试</td>\n" +
                "            <td>必修</td>\n" +
                "            <td>专业主干课</td>\n" +
                "        </tr>\n" +
                "        \n" +
                "        <tr>\n" +
                "            <td>3</td>\n" +
                "            <td>2016-2017-1</td>\n" +
                "            <td align=\"left\">计算机组成原理实验</td>\n" +
                "            \n" +
                "            <td style=\" \"><a href=\"javascript:JsMod('/jsxsd/kscj/pscj_list.do?xs0101id=2015551514&jx0404id=20160200000090810000462719&zcj=77',700,500)\">77</a></td>\n" +
                "            \n" +
                "            \n" +
                "            <td>1</td>\n" +
                "            <td>16</td>\n" +
                "            <td>考查</td>\n" +
                "            <td>必修</td>\n" +
                "            <td>专业主干课</td>\n" +
                "        </tr>";
        //得到科目名称
        String pattern = "<tr>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td align=\"left\">(.*?)</td>\\s+<td style=\" \"><a href=\"javascript:JsMod.*?\">(.*?)</a></td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+<td>(.*?)</td>\\s+</tr>";
       //1:序号 2:学期 3:课程名字 4：成绩 5:学分 6：总学时 7:考核方式 8:课程属性 9:课程性质
        //result:[1, 2016-2017-1, 计算机组成原理实验, 79, 1, 48, 考试, 必修, 大类共同实践环节]
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(test);
        List<String> result = new ArrayList<String>();
        while(m.find()){
            result.add(m.group(9));
        }
        for(String s1:result){
            System.out.println(s1);
        }
    }



    @Test
    public void test() throws Exception {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            // 创建httpget.
            HttpPost httpPost = new HttpPost("http://jwxt.xtu.edu.cn/jsxsd/xk/LoginToXk");
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpPost);
            ArrayList<NameValuePair> par = new ArrayList<NameValuePair>();
            String yzmcode = MyCodeUtil.getCode("img/code/code.jsp");
            par.add(new BasicNameValuePair("RANDOMCODE",yzmcode));
            httpPost.setEntity(new UrlEncodedFormEntity(par));
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    System.out.println("Response content: " + EntityUtils.toString(entity));

                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }

    }

    @Test
    public  void test2() throws Exception {
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
                System.out.println("resultJSON:" + resultJSON);
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
        par.add(new BasicNameValuePair("encoded","123"));
        par.add(new BasicNameValuePair("RANDOMCODE",yzmcode));
        httpPost2.setEntity(new UrlEncodedFormEntity(par));
        try {
            HttpResponse response = httpClient.execute(httpPost2);
                String resultStr = EntityUtils.toString(response.getEntity());
        }catch (IOException e) {
            e.printStackTrace();
        }

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

        //ScorePage scorePage = new ScorePage(result.get(0),result.get(1),result.get(2),result.get(3),result.get(4),result.get(5),result.get(6),result.get(7),result.get(8));
        //base加密
        //HttpGet httpGet = new HttpGet("http://jwxt.xtu.edu.cn/jsxsd/verifycode.servlet");
        //HttpResponse response2 = httpClient.execute(httpGet);
        //System.out.println("type : " + response2.getEntity().getContentType());
        //String resultStr = EntityUtils.toString(response2.getEntity());
        //BASE64Encoder encoder = new BASE64Encoder();
        //byte[] bytes = resultStr.getBytes();
        //String encode = new String(Base64.encodeBase64(bytes));
        //System.out.println("------->"+encode);


        //接口测试
        //HttpPost httpPost1 = new HttpPost("https://api.sky31.com/idcode/confirm4.php");
        //ArrayList<NameValuePair> par = new ArrayList<NameValuePair>();
        //par.add(new BasicNameValuePair("data","data:image/jpeg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAWAD4DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDtdD8O+FbL4YaJq9x4MstTn/sy1eRLbS4priZmRATgjLHJyTn1NTeHbXwHrPhCx8QX3hbw3pcV2zqEnt4NoZXZcbii5J2E9P5VreHNYsNB+FGganqc/kWcOlWfmSbGbGY0UcKCepHauO8BeJPC4+D50/VpkmSzV0u7Uo24tJLI0arnALEDI2n5cZJGMhX1M5z5evQ7d/CngOO/jsX0Dw2t5Iu5LdrOASOvPIXGSOD+RqnFpHwynmSGHT/CMksjBURIbYsxPAAAHJqp4JM2m6zNYeJpd/iaa3jaGWWQOXtgoxGrZ6qyuWHVj82Wxmsfw9q3gzU/E1vfyx2VnMJhDpunw2RUqxYASSsq7WcnGBkqg9+QXClPnjdnWp4X8ASfadmheGm+y5+0bbSA+TjOd/Hy9D19DUtt4O8EXlutxa+G/D08L52yRWMLKcHBwQuOorn/ABQ8d9rd5JpO57eziX/hIPIk2efEGB8vqMuFWTJGOMrn+Gu5sbq1utIhuNJMMlsYv9HCHYmAMBeB8oGMYxxjpxiojPmk0aGX/wAIJ4P/AOhU0P8A8F0P/wATXlfx98NaDo3gWxuNL0TTbGdtTjRpLW1SJivlSnBKgHGQDj2Fex/2xFB8uoxSWTD7zyAmH6+aPlAJ4G7aenAyK8w/aO/5J5p//YVj/wDRUtaAZfhr4++FdG8K6Rpdxp+stPZWUNvI0cMRUsiBSRmQHGR6CtT/AIaO8H/9A3XP+/EP/wAdoooAP+GjvB//AEDdc/78Q/8Ax2j/AIaO8H/9A3XP+/EP/wAdoooAP+GjvB//AEDdc/78Q/8Ax2j/AIaO8H/9A3XP+/EP/wAdoooAWP8AaG8M3d3a29rp2rh5Z0jIlgjAwxx1EnBGQehzjHGcjz74pfFHw34x8H2ei6Hp19ZtBercbZoI44woSQEDY55y4PT1oooA/9k="));
        //httpPost1.setEntity(new UrlEncodedFormEntity(par));
        //HttpResponse response3 = httpClient.execute(httpPost1);
        //System.out.println(EntityUtils.toString(response3.getEntity()));


        //}

    }

    @Test
    public void codetest(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String cookie = getCookie("http://jwxt.xtu.edu.cn/jsxsd",httpClient);
        System.out.println("cookie :" + cookie);
    }


    @Test
    public void testDownload(){
        //下载30张验证码
        String url = "http://jwxt.xtu.edu.cn/jsxsd/verifycode.servlet";
        String category = "jiaowu";
        downloadImage(url,category);

    }

    //下载验证码图片
    public  static void getYZCode(CloseableHttpClient httpClient,String cookie) throws IOException {
        System.out.println("下载图片的Cookie: " + cookie);
        final HttpGet httpGet = new HttpGet("http://jwxt.xtu.edu.cn/jsxsd/verifycode.servlet");
        httpGet.addHeader(new BasicHeader("Cookie",cookie));
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
        // 请求http
        final HttpResponse codeResponse = httpClient.execute(httpGet);
        if (codeResponse.getStatusLine().getStatusCode() != org.apache.http.HttpStatus.SC_OK) {
            System.err.println("Method failed: " + codeResponse.getStatusLine());
        }
        // save img
        String picName = "img/code";
        final File f = new File(picName);
        f.mkdirs();
        picName += "/code.jpg";
        final InputStream inputStream = codeResponse.getEntity().getContent();
        final OutputStream outStream = new FileOutputStream(picName);
        IOUtils.copy(inputStream, outStream);
        outStream.close();
        httpGet.releaseConnection();
    }

    public static String getCookie(String url,CloseableHttpClient httpClient){
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

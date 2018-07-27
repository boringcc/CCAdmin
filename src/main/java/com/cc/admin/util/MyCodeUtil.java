package com.cc.admin.util;

import com.cc.admin.test.test;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.cc.admin.util.PathUtil;

public class MyCodeUtil {
    private static String clazz = MyCodeUtil.class.getSimpleName();
    private static int whiteThreshold = 500;
    private static boolean useSvm = true;

    public static String getCode(String filePath) throws Exception {
        BufferedImage img = myRemoveBackgroud("D:\\img\\code.jpg");
        ImageIO.write(img,"JPG",new File("D:\\img\\code1.jpg"));
        String code = setMod("D:\\img\\code1.jpg");
        return code;
    }

    public static BufferedImage myRemoveBackgroud(String picFile) throws Exception{
        BufferedImage img = ImageIO.read(new File(picFile));
        img = img.getSubimage(4,4,38,12);
        final int width = img.getWidth();
        final int height = img.getHeight();
        for (int i=0;i<4;i++){
            final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (int x = 0; x < width;++x) {
                for (int y = 0; y < height; ++y) {
                    if(CommonUtil.isWhite2(img.getRGB(x,y),160) == 1){
                        img.setRGB(x,y,0xFFFFFF);
                    }else {
                        img.setRGB(x,y,0x000000);
                    }
                }
            }
        }
        return img;
    }

    public static String setMod(String picFile) throws IOException {
        BufferedImage img = ImageIO.read(new File(picFile));
        BufferedImage img1 = img.getSubimage(0, 0, 8, 12);
        BufferedImage img2 = img.getSubimage(10, 0, 8, 12);
        BufferedImage img3 = img.getSubimage(20, 0, 8, 12);
        BufferedImage img4 = img.getSubimage(30, 0, 8, 12);
        ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
        imgs.add(0,img1);
        imgs.add(1,img2);
        imgs.add(2,img3);
        imgs.add(3,img4);
        String yzcode = "";
        for(int x = 0; x < 4;x++){
            if(CommonUtil.isBlack2(imgs.get(x).getRGB(4,0),100) == 1 && CommonUtil.isBlack2(imgs.get(x).getRGB(5,0),100) == 1){
                if(CommonUtil.isBlack2(imgs.get(x).getRGB(1,11),100) == 0){
                    yzcode+="1";
                }else {
                    if(CommonUtil.isBlack2(imgs.get(x).getRGB(7,11),100) == 1){
                        yzcode+="2";
                    }else {
                        yzcode+="3";
                    }
                }
            }else {
                //先判断b,b(0,1)有
                if (CommonUtil.isBlack2(imgs.get(x).getRGB(0, 1), 100) == 1) {
                    yzcode+="b";
                } else {
                    //这是判断z和c，剩下的只有z和c没有（7,11）
                    if (CommonUtil.isBlack2(imgs.get(x).getRGB(7, 11), 100) == 0) {
                        if (CommonUtil.isBlack2(imgs.get(x).getRGB(4, 5), 100) == 0) {
                            if (CommonUtil.isBlack2(imgs.get(x).getRGB(7, 3), 100) == 1) {
                                yzcode+="v";
                            } else {
                                yzcode+="c";
                            }
                        } else {
                            yzcode+="z";
                        }
                    } else {
                        //这里先判断xx中间有
                        if (CommonUtil.isBlack2(imgs.get(x).getRGB(3, 7), 100) == 1) {
                            yzcode+="x";
                        } else {
                            if (CommonUtil.isBlack2(imgs.get(x).getRGB(7, 3), 100) == 1) {
                                yzcode+="n";
                            } else {
                                yzcode+="m";
                            }

                        }

                    }

                }
            }

        }
        return  yzcode;
        //int[][] img1Arr = new int[img1.getWidth()][img1.getHeight()];
        //for (int x = 0; x < img1.getWidth(); ++x) {
        //    for (int y = 0; y < img1.getHeight(); ++y) {
        //        if (CommonUtil.isBlack2(img1.getRGB(x, y), 100) == 1) {
        //            img1Arr[x][y] = 1;
        //        } else {
        //            img1Arr[x][y] = 0;
        //        }
        //    }
        //}
        //for(int j = 0;j < img1.getHeight();j++){
        //    for(int i = 0;i < img1.getWidth();i++){
        //        System.out.print(img1Arr[i][j]+" ");
        //    }
        //    System.out.println("\n");
        //}
        //
        //String mod = "";
        //for(int y = 0;y < img1.getHeight();++y) {
        //    for(int x = 0;x < img1.getWidth(); ++x){
        //        if(CommonUtil.isBlack2(img1.getRGB(x, y),100) == 1){
        //            mod = mod + "1";
        //        }else {
        //            mod = mod + "0";
        //        }
        //    }
        //}
        //System.out.println("mod :" + mod);




    }




}


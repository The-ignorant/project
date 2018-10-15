package com.li.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataDownload {
	/**
	 * 根据网址和页面的编码集获取网页源代码
	 * 
	 * @author 李慧源
	 * @param url
	 * @param encoding
	 * @return String 源代码
	 */
	public static String getHtmlResourceByUrl(String url, String encoding) {

		StringBuffer buffer = new StringBuffer();// 存储源代码的文件
		URL urlObj = null;
		URLConnection uc = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			urlObj = new URL(url);// 建立网络连接
			uc = urlObj.openConnection();// 打开网络连接
			isr = new InputStreamReader(uc.getInputStream(), encoding);// 建立文件输入流
			reader = new BufferedReader(isr);// 建立文件缓冲写入流
			String temp = null;// 建立临时变量
			while ((temp = reader.readLine()) != null) {
				buffer.append(temp + "\n");// 一边读一边写
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("网络不给力，请检查网络设置");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("网络连接失败，请稍后重试");
		} finally {
			if (null != isr)
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return buffer.toString();
	}

	public static void writeFileByLine(String content, String filename) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			bos.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println("=======================第" + (i + 1) + "页======================");
			String url = "https://movie.douban.com/subject/4739952/comments?start=" + i * 20
					+ "&limit=20&sort=new_score&status=P";
			String encoding = "utf-8";
			// 1.根据网址和页面的编码集获取网页源代码
			String html = getHtmlResourceByUrl(url, encoding);
			// System.out.println(html);
			// 2.解析源代码，批量采集影评数据
			Document document = Jsoup.parse(html);
			Elements elements = document.getElementsByClass("comment");
			for (Element ele : elements) {
				String desc = ele.getElementsByClass("short").text();
				System.out.println(desc);
			}
		}
	}
}

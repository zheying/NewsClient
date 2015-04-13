package net.ym.zzy.favoritenews.service;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import net.ym.zzy.favoritenews.Constants;
import net.ym.zzy.favoritenews.app.AppApplication;

public class NewsDetailsService {
	public static String getNewsDetails(String url, String news_title,
			String news_date) {
        Document document = null;
        String data = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Cache-Control\" content=\"no-cache\" />\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/>\n" +
                "<meta name=\"MobileOptimized\" content=\"320\"/>\n" +
                "<meta name=\"robots\" content=\"all\" />\n" +
                "<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<link type=\"text/css\" rel=\"stylesheet\"\n" +
                "\thref=\"" + Constants.HOST + "static/news_detail_sytle.css\"></head><body>" +
                "<header>\n" +
                "<h1>" + news_title + "</h1>\n" +
                "<div class=\"subtitle\">\n" +
                "<a id=\"source\" href=\"\"></a>\n" +
                "<time>" + news_date +"</time>\n" +
                "</div>\n" +
                "</header>" +
                "<article>";

//		data += "<body>" +
//				"<center><h2 style='font-size:16px;'>" + news_title + "</h2></center>";
//		data = data + "<p align='left' style='margin-left:10px'>"
//				+ "<span style='font-size:10px;'>"
//				+ news_date
//				+ "</span>"
//				+ "</p>";
//		data = data + "<hr size='1' />";
		try {
			document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36")
                    .timeout(9000)
                    .get();
            Log.d(AppApplication.getApp().getPackageName(), document.body().toString());
            FileOutputStream fos = AppApplication.getApp().openFileOutput("news.txt", Context.MODE_PRIVATE);
            fos.write(document.body().toString().getBytes("utf-8"));
            fos.flush();
//			Element element = null;
            Elements element = null;
            Log.d(AppApplication.getApp().getPackageName(), url);
			if (TextUtils.isEmpty(url)) {
				data = "";
//				element = document.getElementById("memberArea");
                element = document.select("div#memberArea");
                Log.d(AppApplication.getApp().getPackageName(), "memberArea");
			} else {
//				element = document.getElementById("artibody");
                element = document.select("div#artibody");
                Log.d(AppApplication.getApp().getPackageName(), "artibody");
                Log.d(AppApplication.getApp().getPackageName(), "element : " + element.toString());
			}
			if (element != null) {
                Log.d(AppApplication.getApp().getPackageName(), element.toString());
				data = data + element.toString();
			}else{
                Log.d(AppApplication.getApp().getPackageName(), "element == null");
            }
			data = data + "</article></body></html>";
		} catch (IOException e) {
            Log.d(AppApplication.getApp().getPackageName(), "jsoup exception");
			e.printStackTrace(System.err);
		}

        Log.d(AppApplication.getApp().getPackageName(), data);
		return data;
	}
}

package com.tedu.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.tedu.common.HttpContext;

/**
 * http请求对象，封装一个http请求相关信息
 * 
 * @author shaozg
 *
 */
public class HttpRequest {
	// 请求方法
	private String method;
	// 请求资源
	private String uri;
	// 请求协议版本
	private String protocol;

	// 消息报头信息
	private Map<String, String> header;
	
	private Map<String, String> parameters;
	
	
	public static void main(String[] args) {
		HttpRequest r=new HttpRequest();
		r.parseUri("/reg.html?username=szg&password=123456&nickname=shaozg&mailbox=szg%40tedu.cn");
		System.out.println("URI:"+r.uri);
		System.out.println("kv参数："+r.parameters);
	
	
	}
//	private void parseUri(String uri){
//		/*
//		 * 
//		 * 	private Map<String, String> parameters;
//		 * /reg.html?username=szg&password=123456&nickname=shaozg&mailbox=szg%40tedu.cn
//		 * 1实例化HashMap用于初始化属性parameters
//		 * 
//		 * 
//		 * 
//		 * 
//		 */
//		this.uri=uri;
//		parameters=new HashMap<String,String>();
//		//1先按照?拆分
//				String[] data = uri.split("\\?");
//				System.out.println("loc:"+data[0]);
//				
//				//2按照&拆分所有参数
//				data = data[1].split("&");
//				for(int i=0;i<data.length;i++){
//					//将每一个参数按照=拆分
//					String[] arr = data[i].split("=");
////					System.out.println(arr[0]+":"+arr[1]);
//					parameters.put(arr[0], arr[1]);
//				}
//				
//
//	}
	private void parseUri(String uri){
		/*
		 * /index.html
		 * /reg?username=fancq&password=123456&nickname=fanfan
		 * 在GET请求中，URI可能会有上面两种情况。
		 * HTTP协议中规定，GET请求中的URI可以传递
		 * 参数，而规则是请求的资源后面以"?"分割，
		 * 之后则为所有要传递的参数，每个参数由:
		 * name=value的格式保存，每个参数之间使用
		 * "&"分割。
		 * 这里的处理要求:
		 * 将"?"之前的内容保存到属性uri上。
		 * 之后的每个参数都存入属性parameters中
		 * 其中key为参数名，value为参数的值。
		 * 
		 * 1:实例化HashMap用于初始化属性parameters
		 * 
		 */
		//1
		this.parameters = new HashMap<String,String>();
		
		//先分析uri中是否含有"?"
		int index = -1;
		if((index = uri.indexOf("?"))>=0){
			//先按照"?"拆分
			this.uri = uri.substring(0,index);
			
			/*
			 * 截取出所有参数
			 * paras:username=fancq&password=123456&nickname=fanfan
			 */
			String paras = uri.substring(index+1);
			/*
			 * 拆分每一个参数
			 * [username=fancq,password=123456,nickname=fanfan]
			 */
			String[] paraArray = paras.split("&");
			//遍历每一个参数
			for(String para : paraArray){
				//按照"="拆分
				String[] paraDate = para.split("=");
				this.parameters.put(paraDate[0], paraDate[1]);
			}
		}else{
			this.uri = uri;
		}
		
		
	}
	
	public String getParameter(String name){
		
		return parameters.get(name);
	}

	public HttpRequest(InputStream in) throws Exception {

		try {
			System.out.println("httpRequest构造器");
			// 1解析请求行
			
			parseRequestLine(in);
			// 2解析消息头
			parseRequestHeader(in);
			// 3解析正文
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public HttpRequest() {
		
	}
	/*
	 * 解析消息头
	 */
	private void parseRequestHeader(InputStream in) {
		System.out.println("解析请求头");
		try {
			header = new HashMap<String, String>();
			String line = null;
			while (true) {
				line = readLine(in);
				if (line.length() == 0)
					break;
				int index = line.indexOf(":");

				// System.out.println(Arrays.toString(data));
				// System.out.println(data[0]+data[1]);
				String key = line.substring(0, index);
				String value = line.substring(index + 1).trim();
				// System.out.println(key);
				// System.out.println(value);
				header.put(key, value);
				// header.put(data[0], data[1]);
			}

			System.out.println(header);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void parseRequestLine(InputStream in) throws Exception {
		System.out.println("解析请求行");
		/*
		 * 实现步骤： 1：先读取一行字符串 2：根据空格拆分 3：将请求行中的三项内容设置到HR对应属性上
		 */
		try {
			String line = readLine(in);

			System.out.println("ss:" + line);
			if (line.length() == 0)
				throw new RuntimeException("空的请求行");
			String[] data = line.split("\\s");
			method = data[0];
//			uri = data[1];
			parseUri(data[1]);
			protocol = data[2];
//			System.out.println("我的解析：请求方法=" + method + ",资源标识=" + uri + ",协议=" + protocol);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据输入流读取一行字符串 根据HTTP协议读取请求中的一行内容, 以CRLF结尾的一行字符串
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String readLine(InputStream in) throws IOException {
		// 请求中第一行字符串(请求行内容)
		StringBuilder builder = new StringBuilder();
		/*
		 * 连续读取若干字符，直到连续读取到了CRLF为止
		 */
		// c1是本次读到的字符，c2是上次读到的字符
		int c1 = -1, c2 = -1;
		while ((c1 = in.read()) != -1) {
			if (c1 == HttpContext.LF && c2 == HttpContext.CR) {
				break;
			}
			builder.append((char) c1);
			c2 = c1;
		}
		return builder.toString().trim();
	}

	public String getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}

	public String getProtocol() {
		return protocol;
	}

	public Map<String, String> getHeader() {
		return header;
	}

}

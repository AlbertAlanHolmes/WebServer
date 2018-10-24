package com.tedu.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpContext {
	public static final int CR = 13;// 回车
	public static final int LF = 10;// 换行

	// 状态码-接受成功
	public static final int STATUS_CODE_OK = 200;
	// 状态描述-接受成功
	public static final String STATUS_REASON_OK = "OK";

	// 状态码-资源未发现
	public static final int STATUS_CODE_NOTFOUND = 404;
	// 状态描述-资源未发现
	public static final String STATUS_REASON_NOTFOUND = "Not Found";

	// 状态码-服务器发生未知错误
	public static final int STATUS_CODE_ERROR = 500;
	// 状态描述-服务器发生未知错误
	public static final String STATUS_REASON_ERROR = "Internal Server Error";

	/*
	 * 状态码-状态描述 对应的Map
	 */
	public static final Map<Integer, String> statusMap = new HashMap<Integer, String>();

	/*
	 * Content-Type映射Map key:资源类型(资源文件的后缀名) value:对应该资源在HTTP协议中规定的ContentType
	 * 
	 * 例如:index.html 那么这个文件在该map中对应key应当是html value对应的值就是text/html
	 */
	public static final Map<String, String> contentTypeMapping = new HashMap<String, String>();

	static {
		/*
		 * 根据配置文件初始化相关信息 /conf/web.xml
		 * 
		 */
		// 1初始化ContentType映射
		initContentTypeMapping();
		initStatus();

	}

	private static void initContentTypeMapping() {
		/*
		 * 将web.xml配置文件中<type-mappings>中 的每一个<type-mapping>进行解析，将
		 * 其中属性ext的值作为key,将type属性的 值作为value存入到contentTypeMapping 这个Map中
		 */
		System.out.println("初始化ContentType");
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf" + File.separator + "web.xml"));

			Element root = doc.getRootElement();
			Element mappingsEle = root.element("type-mappings");
			List<Element> mappingList = mappingsEle.elements();
			for (Element mapping : mappingList) {
				String key = mapping.attributeValue("ext");
				String value = mapping.attributeValue("type");
				contentTypeMapping.put(key, value);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws DocumentException {
//		initContentTypeMapping();
//		System.out.println(contentTypeMapping);
//
//		initStatus();
	}

	private static void initStatus() {
		statusMap.put(STATUS_CODE_OK, STATUS_REASON_OK);
		statusMap.put(STATUS_CODE_NOTFOUND, STATUS_REASON_NOTFOUND);
		statusMap.put(STATUS_CODE_ERROR, STATUS_REASON_ERROR);
	}
}

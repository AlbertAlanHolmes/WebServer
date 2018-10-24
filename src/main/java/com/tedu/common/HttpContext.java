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
	public static final int CR = 13;// �س�
	public static final int LF = 10;// ����

	// ״̬��-���ܳɹ�
	public static final int STATUS_CODE_OK = 200;
	// ״̬����-���ܳɹ�
	public static final String STATUS_REASON_OK = "OK";

	// ״̬��-��Դδ����
	public static final int STATUS_CODE_NOTFOUND = 404;
	// ״̬����-��Դδ����
	public static final String STATUS_REASON_NOTFOUND = "Not Found";

	// ״̬��-����������δ֪����
	public static final int STATUS_CODE_ERROR = 500;
	// ״̬����-����������δ֪����
	public static final String STATUS_REASON_ERROR = "Internal Server Error";

	/*
	 * ״̬��-״̬���� ��Ӧ��Map
	 */
	public static final Map<Integer, String> statusMap = new HashMap<Integer, String>();

	/*
	 * Content-Typeӳ��Map key:��Դ����(��Դ�ļ��ĺ�׺��) value:��Ӧ����Դ��HTTPЭ���й涨��ContentType
	 * 
	 * ����:index.html ��ô����ļ��ڸ�map�ж�ӦkeyӦ����html value��Ӧ��ֵ����text/html
	 */
	public static final Map<String, String> contentTypeMapping = new HashMap<String, String>();

	static {
		/*
		 * ���������ļ���ʼ�������Ϣ /conf/web.xml
		 * 
		 */
		// 1��ʼ��ContentTypeӳ��
		initContentTypeMapping();
		initStatus();

	}

	private static void initContentTypeMapping() {
		/*
		 * ��web.xml�����ļ���<type-mappings>�� ��ÿһ��<type-mapping>���н�������
		 * ��������ext��ֵ��Ϊkey,��type���Ե� ֵ��Ϊvalue���뵽contentTypeMapping ���Map��
		 */
		System.out.println("��ʼ��ContentType");
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

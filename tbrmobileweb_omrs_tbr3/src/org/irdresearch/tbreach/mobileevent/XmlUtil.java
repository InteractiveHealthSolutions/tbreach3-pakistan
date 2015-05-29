package org.irdresearch.tbreach.mobileevent;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XmlUtil {
	
	public static String docToString(Document doc) {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = factory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		Source source = new DOMSource(doc);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String xml = writer.toString();
		return xml;
	}
	
	public static String createSuccessXml() {
		String xml = null;
		Document doc = null;

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement(XmlStrings.RESPONSE);
		//responseNode.setAttribute(XmlStrings.TYPE, XmlStrings.LOGIN_TYPE);

		Element statusNode = doc.createElement("status");
		Text statusValue = doc.createTextNode("success");
		statusNode.appendChild(statusValue);

		responseNode.appendChild(statusNode);
		
		doc.appendChild(responseNode);

		xml = docToString(doc);
		return xml;
		
	}
	
	public static String createDivertSuccessXml(String msg) {
		String xml = null;
		Document doc = null;

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement(XmlStrings.RESPONSE);
		//responseNode.setAttribute(XmlStrings.TYPE, XmlStrings.LOGIN_TYPE);

		Element statusNode = doc.createElement("status");
		Text statusValue = doc.createTextNode("diverted");
		statusNode.appendChild(statusValue);
		
		Element msgNode = doc.createElement("msg");
		Text msgValue = doc.createTextNode(msg);
		msgNode.appendChild(msgValue);

		responseNode.appendChild(statusNode);
		responseNode.appendChild(msgNode);
		
		doc.appendChild(responseNode);

		xml = docToString(doc);
		return xml;
		
	}
	
	public static String createErrorXml(String errMsg) {
		String xml  = null;
		Document doc = null;

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement(XmlStrings.RESPONSE);
		//responseNode.setAttribute(XmlStrings.TYPE, XmlStrings.LOGIN_TYPE);

		Element statusNode = doc.createElement("status");
		Text statusValue = doc.createTextNode("error");
		statusNode.appendChild(statusValue);
		
		Element msgNode = doc.createElement("msg");
		Text msgValue = doc.createTextNode(errMsg);
		msgNode.appendChild(msgValue);
		
		
		responseNode.appendChild(statusNode);
		responseNode.appendChild(msgNode);
		
		doc.appendChild(responseNode);

		xml = docToString(doc);
		return xml;
	}
	
	/*public static String getMasterXML(String message) {
		return "<master>" + message + "</master>";
	}
	
	public static String getEMRXML(String name, String message) {
		return "<emr><name>" + name + "</name><message>" + message + "</message></emr>";
	}
	*/
	public static String getXMLMessage(String masterMessage, String emrName, String emrMessage) {
		String xml = "";
		
		Document doc = null;

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement(XmlStrings.RESPONSE);
		//responseNode.setAttribute(XmlStrings.TYPE, XmlStrings.LOGIN_TYPE);

		Element masterNode = doc.createElement("master");
		
		Element masterMessageNode = doc.createElement("message");
		Text masterMessageValue = doc.createTextNode(masterMessage);
		masterMessageNode.appendChild(masterMessageValue);
		
		masterNode.appendChild(masterMessageNode);
		
		Element emrNode = doc.createElement("emr");
		
		Element emrNameNode = doc.createElement("name");
		Text emrNameValue = doc.createTextNode(emrName);
		emrNameNode.appendChild(emrNameValue);
		
		Element emrMessageNode = doc.createElement("message");
		Text emrMessageValue = doc.createTextNode(emrMessage);
		emrMessageNode.appendChild(emrMessageValue);
		
		emrNode.appendChild(emrNameNode);
		emrNode.appendChild(emrMessageNode);
		
		responseNode.appendChild(masterNode);
		responseNode.appendChild(emrNode);
		
		doc.appendChild(responseNode);
		
		xml = docToString(doc);
		return xml;
	}
	

}

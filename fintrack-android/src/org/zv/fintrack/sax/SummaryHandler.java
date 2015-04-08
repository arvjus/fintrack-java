package org.zv.fintrack.sax;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.zv.fintrack.model.Summary;

public class SummaryHandler extends DefaultHandler {
	private StringBuffer buffer = new StringBuffer();
	private ArrayList<Summary> summaries = new ArrayList<Summary>();
	private Summary summary;
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		buffer.setLength(0);
		if (localName.equals("summaryBean")) {
			summary = new Summary();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if (localName.equals("group")) {
			summary.group = buffer.toString();
		} else if (localName.equals("count")) {
			summary.count = Long.valueOf(buffer.toString());
		} else if (localName.equals("amount")) {
			summary.amount = Double.valueOf(buffer.toString());
		} else if (localName.equals("summaryBean")) {
			summaries.add(summary);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}
		
	public ArrayList<Summary> getList() {
		return summaries;
	}
}

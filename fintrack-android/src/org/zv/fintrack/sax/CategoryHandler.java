package org.zv.fintrack.sax;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.zv.fintrack.model.Category;

public class CategoryHandler extends DefaultHandler {
	private StringBuffer buffer = new StringBuffer();
	private ArrayList<Category> categories = new ArrayList<Category>();
	private Category category;
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		buffer.setLength(0);
		if (localName.equals("category")) {
			category = new Category();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if (localName.equals("categoryId")) {
			category.categoryId = buffer.toString();
		} else if (localName.equals("name")) {
			category.name = buffer.toString();
		} else if (localName.equals("nameShort")) {
			category.nameShort = buffer.toString();
		} else if (localName.equals("descr")) {
			category.descr = buffer.toString();
		} else if (localName.equals("category")) {
			categories.add(category);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}
		
	public ArrayList<Category> getList() {
		return categories;
	}
}

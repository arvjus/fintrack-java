package org.zv.fintrack.sax;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.zv.fintrack.model.Income;

public class IncomeHandler extends DefaultHandler {
	private StringBuffer buffer = new StringBuffer();
	private ArrayList<Income> incomes = new ArrayList<Income>();
	private Income income;
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		buffer.setLength(0);
		if (localName.equals("income")) {
			income = new Income();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if (localName.equals("incomeId")) {
			income.incomeId = Integer.valueOf(buffer.toString());
		} else if (localName.equals("userId")) {
			income.userId = buffer.toString();
		} else if (localName.equals("createDate")) {
			income.createDate = buffer.toString();
		} else if (localName.equals("amount")) {
			income.amount = Float.valueOf(buffer.toString());
		} else if (localName.equals("descr")) {
			income.descr = buffer.toString();
		} else if (localName.equals("income")) {
			incomes.add(income);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}
		
	public ArrayList<Income> getList() {
		return incomes;
	}
}

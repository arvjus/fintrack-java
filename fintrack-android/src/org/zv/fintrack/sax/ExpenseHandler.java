package org.zv.fintrack.sax;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.zv.fintrack.model.Expense;

public class ExpenseHandler extends DefaultHandler {
	private StringBuffer buffer = new StringBuffer();
	private ArrayList<Expense> expenses = new ArrayList<Expense>();
	private Expense expense;
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		buffer.setLength(0);
		if (localName.equals("expense")) {
			expense = new Expense();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if (localName.equals("expenseId")) {
			expense.expenseId = Integer.valueOf(buffer.toString());
		} else if (localName.equals("userId")) {
			expense.userId = buffer.toString();
		} else if (localName.equals("category")) {
			expense.category = buffer.toString();
		} else if (localName.equals("createDate")) {
			expense.createDate = buffer.toString();
		} else if (localName.equals("amount")) {
			expense.amount = Float.valueOf(buffer.toString());
		} else if (localName.equals("descr")) {
			expense.descr = buffer.toString();
		} else if (localName.equals("expense")) {
			expenses.add(expense);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}
		
	public ArrayList<Expense> getList() {
		return expenses;
	}
}

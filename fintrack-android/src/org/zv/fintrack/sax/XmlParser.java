package org.zv.fintrack.sax;

import java.util.ArrayList;
import java.io.StringReader;
import javax.xml.parsers.SAXParserFactory;

import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import org.zv.fintrack.model.Category;
import org.zv.fintrack.model.Income;
import org.zv.fintrack.model.Expense;
import org.zv.fintrack.model.Summary;

public class XmlParser {
	public ArrayList<Category> parseCatetoriesResponse(String xml) {
		try {
			XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			CategoryHandler handler = new CategoryHandler();
			xmlreader.setContentHandler(handler);
			xmlreader.parse(new InputSource(new StringReader(xml)));
			return handler.getList();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.toString());
			return new ArrayList<Category>();
		}
	}
	
	public ArrayList<Income> parseIncomesResponse(String xml) {
		try {
			XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			IncomeHandler handler = new IncomeHandler();
			xmlreader.setContentHandler(handler);
			xmlreader.parse(new InputSource(new StringReader(xml)));
			return handler.getList();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.toString());
			return new ArrayList<Income>();
		}
	}
	
	public ArrayList<Expense> parseExpensesResponse(String xml) {
		try {
			XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			ExpenseHandler handler = new ExpenseHandler();
			xmlreader.setContentHandler(handler);
			xmlreader.parse(new InputSource(new StringReader(xml)));
			return handler.getList();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.toString());
			return new ArrayList<Expense>();
		}
	}
	
	public ArrayList<Summary> parseSummaryResponse(String xml) {
		try {
			XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			SummaryHandler handler = new SummaryHandler();
			xmlreader.setContentHandler(handler);
			xmlreader.parse(new InputSource(new StringReader(xml)));
			return handler.getList();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.toString());
			return new ArrayList<Summary>();
		}
	}
}

package org.zv.fintrack.pd;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Category entity.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("serial")
@Entity
@Table(schema = "public", name = "categories")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(namespace="http://fintrack.zv.org/rest")
public class Category implements Serializable {

	@Id
	@Column(name = "category_id")
	private	String	categoryId;
	
	@Column(name = "name")
	private	String	name;

	@Column(name = "name_short")
	private	String	nameShort;
	
	@Column(name = "descr")
	private	String	descr;

	@Column(name = "order_pos")
	private int orderPos;


	@XmlElement(name="categoryId")
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="nameShort")
	public String getNameShort() {
		return nameShort;
	}

	public void setNameShort(String nameShort) {
		this.nameShort = nameShort;
	}

	@XmlElement(name="descr")
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public int getOrderPos() {
		return orderPos;
	}

	public void setOrderPos(int orderPos) {
		this.orderPos = orderPos;
	}
	
	public String toString() {
		return categoryId + ", " + name + ", " + nameShort + ", " + descr + ", " + orderPos;
	}
}

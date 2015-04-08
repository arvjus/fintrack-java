package org.zv.fintrack.pd.bean;

import java.math.BigDecimal;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.zv.fintrack.util.FintrackUtils;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(namespace="http://fintrack.zv.org/rest")
public class SummaryBean implements Serializable {
	private String	group;
	private long	count;
	private double	amount;
	
	public SummaryBean() {
	}

	public SummaryBean(Object group, Object count, Object amount) {
		this.group = group != null ? group.toString() : null;
		this.count = FintrackUtils.numericToLong(count);
		this.amount = FintrackUtils.numericToDouble(amount); 
	}

	public SummaryBean(String group, long count, float amount) {
		this.group = group;
		this.count = count;
		this.amount = amount; 
	}

	@XmlElement(name="group")
	public String getGroup() {
		return group;
	}

	@XmlElement(name="count")
	public long getCount() {
		return count;
	}

	@XmlElement(name="amount")
	public double getAmount() {
		return new BigDecimal(amount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public String toString() {
		return group + ", " + count + ", " + amount;
	}
}

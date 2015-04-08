package org.zv.fintrack.pd;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.SqlResultSetMapping;
/*
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
*/
import javax.persistence.ColumnResult;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Income entity.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("serial")
@Entity
@Table(schema = "public", name = "incomes")
@SequenceGenerator(name = "INCOME_SEQ", sequenceName = "income_seq", allocationSize = 10)
@SqlResultSetMapping(name = "incomeSummarySimpleResults", 
	columns = {
		@ColumnResult(name = "count"),
		@ColumnResult(name = "amount")
	}
/*	entities = {
		@EntityResult(entityClass = org.zv.fintrack.pd.bean.IncomeSummaryBean.class, fields = {
	        @FieldResult(name = "count", column = "count"),
	        @FieldResult(name = "amount", column = "amount")
		})
	} */
)
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(namespace="http://fintrack.zv.org/rest")
public class Income implements Serializable {
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INCOME_SEQ")
	@Column(name = "income_id")
    private	Integer		incomeId;

	@Column(name = "user_id")
	private String		userId;

	@Column(name = "create_date")
	private	Date		createDate;

	private float		amount;

	private String		descr;
	
	
	@XmlElement(name="incomeId")
	public Integer getIncomeId() {
		return incomeId;
	}
	
	public void setIncomeId(Integer incomeId) {
		this.incomeId = incomeId;
	}
	
	@XmlElement(name="userId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		if (userId == null) {
			userId = "none";
		}
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	@XmlElement(name="createDate")
	public String getCreateDateAsString() {
        return (createDate != null) ? simpleDateFormat.format(createDate) : null;
    }
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@XmlElement(name="amount")
	public float getAmount() {
		return amount;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	@XmlElement(name="descr")
	public String getDescr() {
		return descr;
	}
	
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	public String toString() {
		return incomeId + ", " + userId + ", " + createDate + ", " + amount + ", " + descr;
	}
}

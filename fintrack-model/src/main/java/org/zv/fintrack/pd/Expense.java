package org.zv.fintrack.pd;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.zv.fintrack.pd.Category;

/**
 * Expense entity.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("serial")
@Entity
@Table(schema = "public", name = "expenses")
@SequenceGenerator(name = "EXPENSE_SEQ", sequenceName = "expense_seq", allocationSize = 10)
@SqlResultSetMapping(name = "expenseSummarySimpleResults", 
	columns = {
		@ColumnResult(name = "category"),
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
public class Expense implements Serializable {
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPENSE_SEQ")
	@Column(name = "expense_id")
	private	Integer		expenseId;

	@Column(name = "category_id")
	private String		categoryId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", insertable = false, updatable = false, nullable = false)
	private Category category;

	@Column(name = "user_id")
	private String		userId;

	@Column(name = "create_date")
	private	Date		createDate;

	private float		amount;

	private String		descr;
	
	@XmlElement(name="expenseId")
	public Integer getExpenseId() {
		return expenseId;
	}
	
	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Category getCategory() {
		return category;
	}

	@XmlElement(name="category")
   	public String getCategoryName() {
		return category.getName();
	}

	public void setCategory(Category category) {
		this.category = category;
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
		return expenseId + ", " + categoryId + ", " + userId + ", " + createDate + ", " + amount + ", " + descr;
	}
}

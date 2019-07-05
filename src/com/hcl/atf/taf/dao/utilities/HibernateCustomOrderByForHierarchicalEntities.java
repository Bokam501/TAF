package com.hcl.atf.taf.dao.utilities;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

public class HibernateCustomOrderByForHierarchicalEntities extends Order {

	//Ref : http://blog.hexican.com/2012/05/how-to-customize-hibernate-order-by/
	//Ref : http://blog.tremend.com/2008/06/10/how-to-order-by-a-custom-sql-formulaexpression-when-using-hibernate-criteria-api/
	
    private String sqlExpression;
    
    protected HibernateCustomOrderByForHierarchicalEntities(String sqlExpression) {
        super(sqlExpression, true);
        this.sqlExpression = sqlExpression;
    }
 
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return sqlExpression;
    }
 
    public static Order sqlFormula(String sqlFormula) {
        return new HibernateCustomOrderByForHierarchicalEntities(sqlFormula);
    }
 
    public String toString() {
        return sqlExpression;
    }
 }

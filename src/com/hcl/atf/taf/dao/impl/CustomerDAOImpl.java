package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.CustomerDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.UserList;


@Repository
public class CustomerDAOImpl implements CustomerDAO {
	private static final Log log = LogFactory.getLog(CustomerDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Autowired
	private UserListDAO userListDAO;
	
	@Override
	@Transactional
	public List<Customer> list(int status, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("Customer listing");		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Customer.class, "customer");
		if(status != 2){
			c.add(Restrictions.eq("customer.status", status));
		}
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		c.addOrder(Order.desc("customer.status"));
		List<Customer> customers = c.list();
		return customers;
	}

	@Override
	@Transactional
	public boolean isCustomerExistingByName(String customerName) {
		log.info("Is Customer existing");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Customer.class, "customer");
		c.add(Restrictions.eq("customer.customerName", customerName));
		
		List<Customer> customers = c.list();		
		
		if (customers == null || customers.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public Customer getCustomerByName(String customerName) {
		log.debug("listing specific Customer instance");
		Customer customer=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Customer.class, "customer");
			c.add(Restrictions.eq("customer.customerName", customerName));
			
			List<Customer> customerList = c.list();	
			
			customer=(customerList!=null && customerList.size()!=0)?(Customer)customerList.get(0):null;
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return customer;
	}

	@Override
	@Transactional
	public void add(Customer customer) {
		log.debug("adding Customer instance");
		try {
			customer.setStatus(1);			
			sessionFactory.getCurrentSession().save(customer);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}}

	@Override
	@Transactional
	public Customer getByCustomerId(int customerId) {

		log.debug("getting Customer instance by id");
		Customer customer=null;
		List<Customer> customerList = new ArrayList<Customer>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Customer.class, "customer");
			c.add(Restrictions.eq("customer.customerId", customerId));
			
			customerList = c.list();
			
			
			customer=(customerList!=null && customerList.size()!=0)?(Customer)customerList.get(0):null;
			if(customer != null){
				if(customer.getCustomerUser() != null){
					Hibernate.initialize(customer.getCustomerUser());
				}
			}
			
			log.debug("getByCustomerId successful");
		} catch (RuntimeException re) {
			log.error("getByCustomerId failed", re);
		}
		return customer;
	}

	@Override
	@Transactional
	public void update(Customer customer) {
		log.debug("updating Customer instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(customer);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}	
	
	@Override
	@Transactional
	public Customer updateCustomerUserOneToMany(int customerId, int userId, String maporunmap) {
		log.info("updateCustomerUserOneToMany method updating customer - user Association");
		
		Customer customer = null;
		UserList customerUser = null;
		Set<Customer> customerSet = null;
		try{
			customer = getByCustomerId(customerId);
			customerUser = userListDAO.getByUserId(userId);
			Set<UserList> custUserSet = null;
			
			
			if(customer != null && customerUser != null){
				if(maporunmap.equalsIgnoreCase("map")){		
					custUserSet = customer.getCustomerUser();
					if(custUserSet != null){
						custUserSet.add(customerUser);
					}
					customer.setCustomerUser(custUserSet);
					sessionFactory.getCurrentSession().saveOrUpdate(customer);
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					custUserSet = customer.getCustomerUser();
					if(custUserSet.size() != 0){
						if(custUserSet.contains(customerUser)){
							Customer cust1 = customer;
							customer.getCustomerUser().remove(customerUser);
							sessionFactory.getCurrentSession().saveOrUpdate(cust1);
						}					
					}					
				}
			}
			
		} catch (RuntimeException re) {
			log.error("updateDecouplingCategoriesTestCasesOneToMany failed", re);
		}
		return customer;		
	}

	@Override
	@Transactional
	public List<Customer> getCustomerByUserProducts(Integer userId,	Integer userRoleId, Integer activeStatus) {
		List<Customer> customers = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Customer.class, "customer");
			if(userRoleId == 3 || userRoleId == 4 || userRoleId == 5){
				criteria.add(Subqueries.propertyIn("customer.customerId", DetachedCriteria.forClass(ProductUserRole.class, "productUserRole")
					.createAlias("productUserRole.user", "user")
					.createAlias("productUserRole.product", "product")
					.createAlias("product.customer", "productCustomer")
					.add(Restrictions.eq("user.userId", userId))
					.setProjection(Property.forName("productCustomer.customerId"))
				));
			}
			criteria.add(Restrictions.eq("customer.status", activeStatus));
			
			customers = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getCustomerByUserProducts - ", ex);
		}
		return customers;
	}
	
}

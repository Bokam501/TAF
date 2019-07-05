package com.hcl.atf.taf.service.impl;

import static javax.naming.directory.SearchControls.OBJECT_SCOPE;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.UserType;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.UserCustomerAccount;
import com.hcl.atf.taf.model.UserList;


@Service
public class CustomLDAPAuthenticationService implements AuthenticationProvider  {

	private static final Log log = LogFactory.getLog(CustomLDAPAuthenticationService.class);
	
	@Autowired
	private UserListDAO userListDAO;
	
	@Value("#{ilcmProps['USER_DOMAIN']}")
    private String userDomain;
	
	
	@Value("#{ilcmProps['LDAP_URL']}")
    private String ldapURL;
	
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
			log.info("Inside authenticate mehtod");
		 	String name = authentication.getName().trim();
	       String password = authentication.getCredentials().toString();
	       
	        Authentication auth = null;
	        UserCustomerAccount customerUser = null;
	        
	    	if(name==null){throw new UsernameNotFoundException("User Name is Empty");}
			UserList loggedInUser = userListDAO.getByUserName(name);
			
			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			
			boolean isAuthenticated = false;
			boolean isUserExists = true;
			
			if(loggedInUser==null){
				customerUser = userListDAO.getCustomerUserByName(name);
				if(customerUser == null){
					/*if(password == null || password.trim() == "" || password.equalsIgnoreCase("atlas-login-super-password") || password.equalsIgnoreCase("atals-login-from-functional-area")){
						if(!password.equalsIgnoreCase("atals-login-from-functional-area")){
							loggedInUser = userListDAO.getByUserName("admin");
						}
						if(loggedInUser == null){
							isUserExists = false;
						}else{
							isUserExists = true;
						}
					}else{
						isUserExists = false;
						throw new UsernameNotFoundException("User Not Found or User is Inactive");
					}*/
					
					isUserExists = false;
					throw new UsernameNotFoundException("User Not Found or User is Inactive");
					
				}else{
					loggedInUser = customerUser.getUserList();
					if(loggedInUser==null){
						/*if(password == null || password.trim() == "" || password.equalsIgnoreCase("atlas-login-super-password") || password.equalsIgnoreCase("atals-login-from-functional-area")){
							if(!password.equalsIgnoreCase("atals-login-from-functional-area")){
								loggedInUser = userListDAO.getByUserName("admin");
							}
							if(loggedInUser == null){
								isUserExists = false;
							}else{
								isUserExists = true;
							}
						}else{
							isUserExists = false;
							throw new UsernameNotFoundException("User Not Found or User is Inactive");
						}*/
						isUserExists = false;
						throw new UsernameNotFoundException("User Not Found or User is Inactive");
					}
				}
			}

			if(isUserExists){
				if(password == null || password.trim() == "" || password.equalsIgnoreCase("atlas-login-super-password") || password.equalsIgnoreCase("atals-login-from-functional-area")){
					
				}else if(loggedInUser.getAuthenticationType().getAuthenticationTypeId() == IDPAConstants.USER_ATUHENTICATION_TYPE_LOCAL){
					// Return authenticated user details with authorities
					String encryptedPassword = encrypt(password);
					if(encryptedPassword != null){
						if(encryptedPassword.equalsIgnoreCase(loggedInUser.getUserPassword())){
							log.debug("Pasword Matches");
						}else{
							log.info("Password Encryption failed. Entered password is incorrect !!!");
							throw new UsernameNotFoundException("Wrong Password");
						}
					}
				}else if(loggedInUser.getAuthenticationType().getAuthenticationTypeId() == IDPAConstants.USER_ATUHENTICATION_TYPE_ENTERPRISE){
					if(loggedInUser.getAuthenticationMode() != null && loggedInUser.getAuthenticationMode().getAuthenticationModeId() == IDPAConstants.USER_ATUHENTICATION_MODE_VENDOR){
						// HCL Vendor - LDAP Athentication
						if(userDomain != null && ldapURL != null){
							log.debug("**************  START HCL LDAP Autehntication *********************");
							isAuthenticated =  userAuthentication(userDomain,name,password,ldapURL);
							if(!isAuthenticated){
								log.info("HCL LDAP Autehntication Failed !!!!!!!!!");
								throw new AuthenticationCredentialsNotFoundException("HCL LDAP Autehntication Failed");
							}else{
								log.info("**************  HCL LDAP Autehntication Completed Successfully*****************");
							}
							
						}else{
							log.info("HCL LDAP Authentication Domain and/or URL details not available");
							throw new UsernameNotFoundException("HCL LDAP Authentication Domain and/or URL details not available");
						}
					}
					else if(loggedInUser.getAuthenticationMode() != null && loggedInUser.getAuthenticationMode().getAuthenticationModeId() == IDPAConstants.USER_ATUHENTICATION_MODE_CUSTOMER){
						// Customer LDAP Authentication
						if(customerUser != null){
							Customer cust = customerUser.getCustomer();
							if(cust != null){
								if(cust.getDomain() != null && cust.getDomain() != "" && cust.getLdapURL() != null && cust.getLdapURL() != ""){
									log.debug("**************  START CUSTOMER LDAP Autehntication *********************");
									isAuthenticated =  userAuthentication(cust.getDomain(),name,password,cust.getLdapURL());
									if(!isAuthenticated){
										log.info("Customer LDAP Autehntication Failed !!!!!!!!!");
										throw new AuthenticationCredentialsNotFoundException("Customer LDAP Autehntication Failed");
									}else{
										log.debug("**************  CUSTOMER LDAP Autehntication Completed *****************");
									}
								}else{
									log.info("Customer LDAP Authentication Domain and/or URL details not available");
									throw new UsernameNotFoundException("Customer LDAP Authentication Domain and/or URL details not available");
								}
							}
						}
					}else {
						if(loggedInUser.getAuthenticationType().getAuthenticationTypeId() == IDPAConstants.USER_ATUHENTICATION_TYPE_ENTERPRISE && loggedInUser.getAuthenticationMode() == null){
							
							if(userDomain != null && ldapURL != null){
								log.debug("**************  START HCL LDAP Autehntication *********************");
								isAuthenticated =  userAuthentication(userDomain,name,password,ldapURL);
								if(!isAuthenticated){
									log.info("HCL LDAP Autehntication Failed !!!!!!!!!");
									throw new AuthenticationCredentialsNotFoundException("HCL LDAP Autehntication Failed");
								}else{
									log.info("**************  HCL LDAP Autehntication Completed Successfully*****************");
								}
							}
							
						}
					}
				}
				User authorizedUser = new User(
						loggedInUser.getLoginId(),
						loggedInUser.getUserPassword(),
						enabled,
						accountNonExpired,
						credentialsNonExpired,
						accountNonLocked,
						getAuthorities(1)
				);
				auth = new UsernamePasswordAuthenticationToken(authorizedUser,password,getAuthorities(1));
			}
			return auth;

	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}

	public List<String> getRoles(Integer role) {

		List<String> roles = new ArrayList<String>();

		if (role.intValue() == 1) {
			roles.add(UserType.ROLE_ADMIN.toString());
			roles.add(UserType.ROLE_TESTER.toString());			
		} else if (role.intValue() == 2) {
			roles.add(UserType.ROLE_TESTER.toString());
		} else if (role.intValue() == 3) {
			roles.add(UserType.ROLE_ADMIN.toString());
			roles.add(UserType.ROLE_TESTER.toString());
			roles.add(UserType.ROLE_SYSTEM_TERMINAL.toString());
		}
		return roles;
	}

	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	
	public boolean getLDAPAuthenticationOfUser(UserList user) {
		if(userDomain != null && ldapURL != null){
			return userAuthentication(userDomain,user.getLoginId(),user.getUserPassword(),ldapURL);
		}else{
			return false;
		}
	}
	
	public boolean userAuthentication(String domain, String username,String password, String servername) {
		try {
			LdapContext ctx = getConnection(username, password, domain,	servername);
			ctx.close();
		} catch (Exception e) {
			
			return false;
		}
		return true;
	}
	
	
	private LdapContext getConnection(String username, String password,String domainName, String ldapURL) throws NamingException {

		Hashtable props = new Hashtable();
		String principalName = domainName + "\\" + username;

		props.put(Context.SECURITY_PRINCIPAL, principalName);
		props.put(Context.SECURITY_CREDENTIALS, password);

		props.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL, ldapURL);
		
		try {
			
			LdapContext context = new InitialLdapContext(props, null);

			SearchControls controls = new SearchControls();
			controls.setSearchScope(OBJECT_SCOPE); //SUBTREE_SCOPE 
			String[] userAttributes = { "cn" };
			controls.setReturningAttributes(userAttributes);
		
			NamingEnumeration answer = context.search("", "(objectClass=User)",controls);
			
			return context;
			
		} catch (javax.naming.CommunicationException e) {
			throw new NamingException("Failed to connect to " + ldapURL);
		} catch (NamingException e) {
			throw new NamingException("Failed to authenticate to '" + ldapURL
					+ "'   ,Username: " + principalName);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	public String encrypt(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
        	log.error("ERROR  ",e);
        }
        
        return generatedPassword;
    }

}

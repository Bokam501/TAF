package com.hcl.atf.taf.service.impl;

import static javax.naming.directory.SearchControls.OBJECT_SCOPE;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.UserType;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.constants.IDPAConstants;

@Service
@Transactional(readOnly=true)
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserListDAO userListDAO;
	
	@Value("#{ilcmProps['USER_DOMAIN']}")
    private String userDomain;
	
	
	@Value("#{ilcmProps['LDAP_URL']}")
    private String ldapURL;

	private static final Log log = LogFactory.getLog(CustomUserDetailsService.class);
	
	public UserDetails loadUserByUsername(String login)
	throws UsernameNotFoundException {
		log.info("User Name ****  "+login);
		if(login==null){throw new UsernameNotFoundException("User Name is Empty");}
		UserList domainUser = userListDAO.getByUserName(login);
		
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
		boolean isAuthenticated = false;
		
		if(domainUser==null){
			throw new UsernameNotFoundException("User Not Found or User is Inactive");
		}else{
			log.info("domainUser: "+domainUser.getLoginId());
			if(domainUser.getAuthenticationType().getAuthenticationTypeId() == IDPAConstants.USER_ATUHENTICATION_TYPE_LOCAL){
				
			}else if(domainUser.getAuthenticationType().getAuthenticationTypeId() == IDPAConstants.USER_ATUHENTICATION_TYPE_ENTERPRISE){
				if(domainUser.getAuthenticationMode() != null && domainUser.getAuthenticationMode().getAuthenticationModeId() == IDPAConstants.USER_ATUHENTICATION_MODE_VENDOR){
					// HCL Vendor - LDAP Athentication
					if(userDomain != null && ldapURL != null){
						
						isAuthenticated =  authenticate(userDomain,login,"Mitra@14#",ldapURL);
						
					}else{
						throw new UsernameNotFoundException("HCL LDAP Authentication Domain and URL details not available");
					}
				}
				else if(domainUser.getAuthenticationMode() != null && domainUser.getAuthenticationMode().getAuthenticationModeId() == IDPAConstants.USER_ATUHENTICATION_MODE_CUSTOMER){
					// Customer LDAP Authentication
				}
			}
		}
		
		return new User(
				domainUser.getLoginId(),
				domainUser.getUserPassword(),
				enabled,
				accountNonExpired,
				credentialsNonExpired,
				accountNonLocked,
				getAuthorities(1)
		);
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
			return authenticate(userDomain,user.getLoginId(),user.getUserPassword(),ldapURL);
		}else{
			return false;
		}
	}
	
	public boolean authenticate(String domain, String username,String password, String servername) {

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

}
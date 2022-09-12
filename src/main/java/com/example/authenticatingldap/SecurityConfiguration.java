package com.example.authenticatingldap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.userdetails.PersonContextMapper;

/**
 * 
 * This file was signficantly modified from the original WebSecurityConfig.java in https://github.com/spring-guides/gs-authenticating-ldap
 * I have added conditional annotations to change between embedded and external ldap servers
 * as well as removed extending the deprecated WebSecurityConfigurerAdapter 
 * 
 */

@Configuration
public class SecurityConfiguration {
  
    @ConditionalOnProperty(prefix="ldap", name="useEmbedded", havingValue="true")
    @Bean
    public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
        EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean =
            EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
        contextSourceFactoryBean.setPort(0);
        return contextSourceFactoryBean;
    }

     @ConditionalOnProperty(prefix="ldap", name="useEmbedded", havingValue="true")
    @Bean
    AuthenticationManager ldapAuthenticationManager(
            BaseLdapPathContextSource contextSource) {
        LdapBindAuthenticationManagerFactory factory = 
            new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserDnPatterns("uid={0},ou=people");
        factory.setUserDetailsContextMapper(new PersonContextMapper());
        return factory.createAuthenticationManager();
    }
  
     
  @ConditionalOnProperty(prefix="ldap", name="useEmbedded", havingValue="false")
  @Bean
  public LdapContextSource getContextSource() {
      LdapContextSource contextSource = new LdapContextSource();
      contextSource.setUrl("ldap://localhost:1389");
      contextSource.setBase("dc=example,dc=org");
      contextSource.setUserDn("cn=admin,dc=example,dc=org");
      contextSource.setPassword("adminpassword");
      contextSource.afterPropertiesSet();
      return contextSource;
  }
    
    @ConditionalOnProperty(prefix="ldap", name="useEmbedded", havingValue="false")
    @Bean
    AuthenticationManager ldapAuthenticationManager() {
        LdapBindAuthenticationManagerFactory factory = 
            new LdapBindAuthenticationManagerFactory(getContextSource());
        factory.setUserDnPatterns("uid={0},ou=users");
        factory.setUserDetailsContextMapper(new PersonContextMapper());
        return factory.createAuthenticationManager();
    }
}
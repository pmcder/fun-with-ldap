# Summary

This build is based on [Spring Authenticating LDAP](https://spring.io/guides/gs/authenticating-ldap/#initial)

This readme describes the solution to the issues encountered with that guide, provides some general notes, and provides steps and notes and deploying in a Docker container

### Issues

##### Issue 1
* The class WebSecurityConfigurerAdapter referenced in that guide is deprecated 

* The fix is to use the new LDAP classes defined in this Spring article: [Spring Security Without the WebSecurityConfigurerAdapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)

* additional information on these classes at [GitHub repo](https://github.com/spring-projects/spring-security/pull/10138)

##### Issue 2
With Spring Boot, the root entry in the ldif file will cause an error that it has already been created so delete the following lines from the ldif:<br>
`dn: dc=springframework,dc=org`<br>
`objectclass: top`<br>
`objectclass: domain`<br>
`objectclass: extensibleObject`<br>
`dc: springframework`<br>

This issue is due to line 53 in EmbeddedLdapServerContextSourceFactoryBean already creating the Domain Component

# Misc notes

clicking on .ldif file in Eclipse IDE redirects to native mac contacts
right click and edit as text file

if using Docker, make sure that jdk on your base image is capable of running classes compiled on the jdk you compiled on
otherwise you may see 'has been compiled by a more recent version of the Java Runtime' errors

# Running with Docker

To build the Docker image, the following command is used:
`docker build -t authenticatingldap .` <br>
The -t switch is for tag and the argument passed will be the name of the Image

To run the image the following can be used: <br>
`docker run -d -p 9090:8080 authenticatingldap`

This will run the image in detached mode (in the background) 
Port 8080 on the container is mapped to 9090 on the host machine

# Running locally

1. cd into project directory<br>
1. run `mvn clean package`
1. run `java -jar target/authenticating-ldap-0.0.1-SNAPSHOT.jar`





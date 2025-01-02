package org.example.configuration;


import jakarta.ws.rs.NotFoundException;
import service.PersonService;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class JNDIConfig {

    public static PersonService personService(){
        Properties jndiProps = new Properties();
        jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProps.put("jboss.naming.client.ejb.context", true);
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8881");
        try {
            final Context context = new InitialContext(jndiProps);
            return (PersonService) context.lookup("java:global/called-service/PersonServiceImpl!service.PersonService");
        } catch (NamingException e){
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
}
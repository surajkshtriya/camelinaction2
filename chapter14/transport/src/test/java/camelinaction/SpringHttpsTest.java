package camelinaction;

import java.net.URL;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringHttpsTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/https.xml");
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        URL trustStoreUrl = this.getClass().getClassLoader().getResource("./cia_truststore.jks");
        System.setProperty("javax.net.ssl.trustStore", trustStoreUrl.toURI().getPath());
    }
    
    @Test
    public void testHttps() throws Exception {
        final String body = "Hello Camel";

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived(body);

        // send an InOut (= requestBody) to Camel
        log.info("Caller calling Camel with message: " + body);
        String reply = template.requestBody("https://localhost:8080/early", body, String.class);

        // we should get the reply early which means you should see this log line
        // before Camel has finished processed the message
        log.info("Caller finished calling Camel and received reply: " + reply);
        assertEquals("OK", reply);

        assertMockEndpointsSatisfied();
    }

}
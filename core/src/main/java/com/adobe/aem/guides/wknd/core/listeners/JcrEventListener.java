package com.adobe.aem.guides.wknd.core.listeners;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

@Component(immediate = true, service = EventListener.class)
public class JcrEventListener implements EventListener {

    private static final Logger log = LoggerFactory.getLogger(JcrEventListener.class);
    private Session session;

    @Reference
    SlingRepository slingRepository;
/*
SlingRepository slingRepository;
It looks like you're referencing the SlingRepository interface. This is a key component in Apache Sling, 
a web framework that is based on JCR  technology. The SlingRepository interface provides methods for accessing
andmanaging content within the repository, such as creating, reading, updating, and deleting nodes.In a typical 
usage scenario, you would inject an instance of SlingRepository into your Java code using dependency injection
frameworks like Spring or OSGi, and then use its methods to interact with the underlying JCR repository,which could 
be Apache Jackrabbit or another implementation.

 */

    @Activate
/*
@Activate
This annotation is used to notify your component that it is now loaded, resolved and ready to provide service. 
You use this method to do some final setup in your component.
*/
    public void activate() {
        try {
            log.info("Logging in with service user 'geeksserviceuser'...");
            session = slingRepository.loginService("geeksserviceuser", null);
            log.info("Logged in successfully as service user 'geeksserviceuser'.");
            session.getWorkspace().getObservationManager().addEventListener(
                    this,
                    Event.NODE_ADDED | Event.PROPERTY_ADDED,
                    "/content/dq-aem/us/en",
                    true,
                    null,
                    null,
                    false);
            log.info("JCR Event Listener activated successfully.");
        } catch (RepositoryException e) {
            log.error("Error while adding Event Listener: {}", e.getMessage());
        }
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        try {
            while (eventIterator.hasNext()) {
                Event event = eventIterator.nextEvent();
                log.info("Received JCR Event: {} at path: {}", event.getType(), event.getPath());
                // Perform additional processing if needed
            }
        } catch (Exception e) {
            log.error("Error while processing events: {}", e.getMessage());
        }
    }

    // You can add more methods or modify existing ones as needed

}

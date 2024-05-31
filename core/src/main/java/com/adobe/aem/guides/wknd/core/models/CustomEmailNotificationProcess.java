package com.adobe.aem.guides.wknd.core.models;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

import java.util.HashMap;
import java.util.Map;

@Component(
    service = WorkflowProcess.class,
    property = {"process.label=Send Email Notification"}
)
public class CustomEmailNotificationProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(CustomEmailNotificationProcess.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap args) throws WorkflowException {
        ResourceResolver resourceResolver = null;
        try {
            LOG.info("Custom workflow step: Email Notification executed.");

            // Get the resource resolver
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);

            if (resourceResolver == null) {
                LOG.error("ResourceResolver is null. Unable to proceed.");
                throw new WorkflowException("ResourceResolver is null.");
            }

            // Retrieve the email addresses of the users from their profiles
            String adminEmail = getEmailFromProfile(resourceResolver, "/home/users/0/0MTNVilt6M4Ym3nT6nLR/profile");
            String gopalEmail = getEmailFromProfile(resourceResolver, "/home/users/2/2BidnW5LlokibchlIBUI/profile");

            // Set up the Email message
            Email email = new SimpleEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setStartTLSEnabled(true);
            email.setAuthenticator(new DefaultAuthenticator("nirbhaipanchakarla1@gmail.com", "dtumzowlaunswlzp"));

            // Set the mail values
            email.addTo(adminEmail);
            email.addCc(gopalEmail);
            email.setSubject("Workflow Notification");
            email.setMsg("Details of the workflow:\n\n" +
                         "Payload Path: " + item.getWorkflowData().getPayload().toString() + "\n" +
                         "Workflow Initiator: " + item.getWorkflow().getInitiator() + "\n");

            // Log the email details
            LOG.info("Email details:");
            LOG.info("To: {}", adminEmail);
            LOG.info("Cc: {}", gopalEmail);
            LOG.info("Subject: {}", email.getSubject());
           // LOG.info("Body: {}", email.getMsg());

            // Get the message gateway
            MessageGateway<Email> messageGateway = messageGatewayService.getGateway(SimpleEmail.class);

            if (messageGateway == null) {
                LOG.error("MessageGateway is null. Unable to proceed.");
                throw new WorkflowException("MessageGateway is null.");
            }

            // Send the email
            LOG.info("Sending email...");
            messageGateway.send((Email) email);
            LOG.info("Email notification sent successfully.");

        } catch (Exception e) {
            LOG.error("Error executing custom workflow step: {}", e.getMessage(), e);
            throw new WorkflowException("Error executing custom workflow step", e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }

    private String getEmailFromProfile(ResourceResolver resourceResolver, String profilePath) {
        Resource profileResource = resourceResolver.getResource(profilePath);
        if (profileResource != null) {
            ValueMap profileProperties = profileResource.adaptTo(ValueMap.class);
            if (profileProperties != null) {
                return profileProperties.get("email", String.class);
            }
        }
        LOG.warn("Email not found for profile: {}", profilePath);
        return "default@example.com";  // Fallback email, adjust as needed
    }
}

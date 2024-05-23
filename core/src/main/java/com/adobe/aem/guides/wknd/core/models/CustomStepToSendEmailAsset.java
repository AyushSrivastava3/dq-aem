package com.adobe.aem.guides.wknd.core.models;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.ResourceResolver;
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

@Component(
    service = WorkflowProcess.class, 
    property = {"process.label=Send Email Notification Delete Asset"}
)
public class CustomStepToSendEmailAsset implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(CustomStepToSendEmailAsset.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap args) throws WorkflowException {
        try {
            LOG.info("Custom workflow step: Email Notification executed.");

            // Get the resource resolver
            ResourceResolver resourceResolver = wfsession.adaptTo(ResourceResolver.class);

            if (resourceResolver == null) {
                LOG.error("ResourceResolver is null. Unable to proceed.");
                throw new WorkflowException("ResourceResolver is null.");
            }

            // Set up the Email message
            Email email = new SimpleEmail();

            // Set the mail values
            String emailToRecipients = "dileepkandregula4641@gmail.com";
            String emailCcRecipients = "panchakarlanirbhai@gmail.com";
            String subject = "Asset Deleted Notification";
            String body = "The Asset " + item.getWorkflowData().getPayload().toString() + " has been deleted.";

            email.addTo(emailToRecipients);
            email.addCc(emailCcRecipients);
            email.setSubject(subject);
            email.setMsg(body);
            
            // Set SMTP server settings
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setStartTLSEnabled(true);
            email.setAuthenticator(new DefaultAuthenticator("nirbhaipanchakarla1@gmail.com", "fdkyioeocddvaeji"));

            // Log the email details
            LOG.info("Email details:");
            LOG.info("To: {}", emailToRecipients);
            LOG.info("Cc: {}", emailCcRecipients);
            LOG.info("Subject: {}", subject);
            LOG.info("Body: {}", body);

            // Get the message gateway
            MessageGateway messageGateway = messageGatewayService.getGateway(SimpleEmail.class);

            if (messageGateway == null) {
                LOG.error("MessageGateway is null. Unable to proceed.");
                throw new WorkflowException("MessageGateway is null.");
            }

            // Send the email
            LOG.info("Sending email...");
            messageGateway.send((Email) email);
            LOG.info("Email notification sent successfully.");

            // Additional workflow logic or actions here
            // For example, you might update workflow metadata, trigger another workflow step, etc.
            // ...

            LOG.info("Additional workflow logic executed successfully.");
        } catch (Exception e) {
            LOG.error("Error executing custom workflow step: {}", e.getMessage(), e);
            throw new WorkflowException("Error executing custom workflow step", e);
        }
    }
}

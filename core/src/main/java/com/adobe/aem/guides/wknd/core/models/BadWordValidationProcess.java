package com.adobe.aem.guides.wknd.core.models;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component(
        service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Bad Word Validation Workflow Step",
                "process.label=Bad Word Validation"
        }
)
public class BadWordValidationProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(BadWordValidationProcess.class);
    private static final List<String> BAD_WORDS = Arrays.asList("badword1", "badword2", "badword3");

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
        ResourceResolver resolver = null;
        try {
            LOG.info("Logging in with service user 'geeksserviceuser'...");
            Map<String, Object> authInfo = Map.of(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
            resolver = resolverFactory.getServiceResourceResolver(authInfo);
            LOG.info("Logged in successfully as service user 'geeksserviceuser'.");

            String payloadPath = (String) workItem.getWorkflowData().getPayload();
            LOG.info("Validating payload at path: {}", payloadPath);

            Resource resource = resolver.getResource(payloadPath);
            if (resource != null) {
                Resource contentResource = resource.getChild("jcr:content");
                if (contentResource != null) {
                    checkForBadWords(contentResource);
                } else {
                    LOG.warn("jcr:content node not found at path: {}", payloadPath);
                }
            } else {
                LOG.warn("Resource not found at path: {}", payloadPath);
            }
        } catch (Exception e) {
            LOG.error("Error during bad word validation", e);
            throw new WorkflowException(e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

    private void checkForBadWords(Resource resource) throws WorkflowException {
        ValueMap properties = resource.adaptTo(ValueMap.class);
        if (properties != null) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                if (entry.getValue() instanceof String) {
                    String content = (String) entry.getValue();

                    // Check if the content contains any bad words
                    for (String badWord : BAD_WORDS) {
                        if (StringUtils.containsIgnoreCase(content, badWord)) {
                            LOG.error("Payload contains bad word: {}", badWord);
                            // Don't stop the loop, continue checking for other bad words
                        }
                    }
                }
            }
        }

        // Recursively check child resources
        for (Resource child : resource.getChildren()) {
            checkForBadWords(child);
        }
    }

}
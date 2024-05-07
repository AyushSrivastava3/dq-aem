package com.adobe.aem.guides.wknd.core.servlets;


import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentFragmentException;
import com.adobe.cq.dam.cfm.FragmentTemplate;
import com.google.gson.stream.JsonWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=" + CreateGenericContentFragment.SERVICE_DESCRIPTION,
                ServletResolverConstants.SLING_SERVLET_PATHS + "=" + "/bin/content/createContentFragment",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET
        }
)
public class CreateGenericContentFragment extends SlingAllMethodsServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGenericContentFragment.class);
    private static final long serialVersionUID = 1L;

    protected static final String SERVICE_DESCRIPTION = "Create Generic Content Fragment from CSV";
    protected static final String CF_TEMPLATE = "/conf/dq-aem/settings/dam/cfm/models/";
    protected static final String[] EXCEPTION_LIST = new String[] {"branchId"};

    private static final String PARENT_PATH = "parentPath";
    private static final String MODEL_TYPE = "modelType";
    private static final String CSV = "csv";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("Received POST request to create content fragment.");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "nocache");
        response.setCharacterEncoding("utf-8");
        JsonWriter jsout = new JsonWriter(response.getWriter());
        jsout.beginObject();
        LOGGER.info("Inside doPost method.");
        LOGGER.info(CF_TEMPLATE);
        LOGGER.info(MODEL_TYPE);
        LOGGER.info("parent_pathg :"+PARENT_PATH);

        String parentPath = request.getParameter(PARENT_PATH);
        String modelType = request.getParameter(MODEL_TYPE);
        LOGGER.info("parent_path :"+parentPath);
        LOGGER.info("modelType :"+modelType);

        ResourceResolver resolver = request.getResourceResolver();

        try {
            if (StringUtils.isNotEmpty(parentPath) && StringUtils.isNotEmpty(modelType)) {
                LOGGER.info("Parent path and model type are not empty.");

                Part filePart = request.getPart(CSV);
                InputStream fileContent = filePart.getInputStream();
                LOGGER.info("fileContent :"+fileContent);

                CSVReader header = new CSVReaderBuilder(new InputStreamReader(fileContent)).withSkipLines(0).build();
                List<String> headerList = Arrays.asList(header.readNext());
                LOGGER.info("headerList :"+headerList);

                List<String> resourceList = header.readAll().stream()
                        .filter(data -> StringUtils.isNotEmpty(data[0]))
                        .map(data -> {
                            createContentFragment(resolver, parentPath, headerList, data, modelType);
                            return "Yes";
                        }).collect(Collectors.toList());
                LOGGER.info("resources size :"+resourceList.size());

                jsout.name("status").value("Success Created total of " + resourceList.size() + " Content Fragments");
            }

        } catch (IOException | ServletException e) {
            LOGGER.error("Exception occurred while processing POST request.", e);
            jsout.name("status").value("Failed Created all Content Fragments");
        } finally {
            if (resolver != null && resolver.hasChanges()) {
                resolver.commit();
            }
        }

        jsout.endObject();
    }

    public void createContentFragment(ResourceResolver resourceResolver, String parentPath, List<String> headerList, String[] data, String modelType) {
        //  LOGGER.info("In create content fragment :"+data[0] +" "+data[1]+" "+data[2]);

        try {
            Resource modelResource = resourceResolver.getResource(parentPath + "/" + data[0]);
            LOGGER.info(String.valueOf(modelResource));
            ContentFragment newFragment = null;
            if (modelResource == null) {
                Resource templateOrModelRsc = resourceResolver.getResource(CF_TEMPLATE + modelType);
                LOGGER.info("CF_TEMPLATE :"+CF_TEMPLATE);
                LOGGER.info("templateOrModelRsc :"+templateOrModelRsc);
                Resource parentResource = resourceResolver.getResource(parentPath);
                FragmentTemplate tpl = templateOrModelRsc.adaptTo(FragmentTemplate.class);
                newFragment = tpl.createFragment(parentResource, data[0], data[0]);
            } else {
                newFragment = modelResource.adaptTo(ContentFragment.class);
            }

            if (newFragment != null) {
                Iterator<ContentElement> elements = newFragment.getElements();
                while (elements.hasNext()) {
                    ContentElement element = elements.next();
                    for (int i = 0; i < headerList.size(); i++) {
                        if (StringUtils.equalsIgnoreCase(element.getName(), headerList.get(i)) || StringUtils.containsAny(element.getName())) {
                            element.setContent(data[i], element.getContentType());
                            break;
                        }
                    }
                }
            }
        } catch (ContentFragmentException e) {
            LOGGER.error("Exception occurred while creating content fragment.", e);
        }

    }
}



package com.adobe.aem.guides.wknd.core.servlets;


import com.adobe.aem.guides.wknd.core.models.SampleContentFragment;
import com.google.gson.stream.JsonWriter;
import com.opencsv.CSVWriter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.File;

@Component(service = { Servlet.class },
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/export-content-to-csv",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST
        })
public class ExportContentFragmentsToCSVServlet extends SlingAllMethodsServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportContentFragmentsToCSVServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("We are in the function");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String folderPath = request.getParameter("parentPath");
        String csvPath=request.getParameter("csvPath");
        LOGGER.info("folderPath: " + folderPath);
        PrintWriter out = response.getWriter();

        JsonWriter jsonWriter = new JsonWriter(response.getWriter());
        jsonWriter.beginObject();

        try (ResourceResolver resourceResolver = request.getResourceResolver()) {
            Resource folderResource = resourceResolver.getResource(folderPath);
            if (folderResource != null) {
                Iterator<Resource> childResources = folderResource.listChildren();
                while (childResources.hasNext()) {
                    Resource contentFragmentResource = childResources.next();
                    SampleContentFragment sampleContentFragment = new SampleContentFragment();
                    sampleContentFragment.setResource(contentFragmentResource);
                    sampleContentFragment.init();

                    // we have to get model path from content fragment
                    String modelPath = getModelPath(contentFragmentResource);

                    // Extract model name from the model path
                    String modelName = extractModelName(modelPath);

                    // Generate CSV file path based on model name
                    String csvFilePath = csvPath+"/" + modelName + ".csv";
                    //check if csvPath directory exist or not
                    File csvDirectory = new File(csvPath);
                    if (!csvDirectory.exists()) {
                        LOGGER.error("CSV path does not exist. Please create it.");
                        jsonWriter.name("status").value("CSV path does not exist. Please create it.");
                        jsonWriter.endObject();
                        return; // Stop further processing
                    }

                    // Check if CSV file already exists
                    boolean fileExists = csvFileExists(csvFilePath);

                    // Write headers to CSV (only if the file doesn't exist)
                    if (!fileExists) {
                        writeHeadersToCSV(csvFilePath, sampleContentFragment.getAllProperties());
                    }

                    // Write data to CSV
                    writeDataToCSV(csvFilePath, sampleContentFragment);

                }
                jsonWriter.name("status").value("Data exported to csv file successfully");
            } else {
                LOGGER.error("Folder resource not found at path: " + folderPath);
                jsonWriter.name("status").value("Failed to export content fragments to CSV because content fragment resource not found");
            }
        }

//        out.println("\"message\": \"Content fragments exported to CSV successfully.\"");
        out.println("}");
    }

    private String getModelPath(Resource contentFragmentResource) {
        Resource jcrContent = contentFragmentResource.getChild("jcr:content");
        if (jcrContent != null) {
            Resource data = jcrContent.getChild("data");
            if (data != null) {
                return data.getValueMap().get("cq:model", "");
            }
        }
        return "";
    }

    private String extractModelName(String modelPath) {
        if (modelPath != null && !modelPath.isEmpty()) {
            String[] pathParts = modelPath.split("/");
            return pathParts[pathParts.length - 1];
        }
        return "";
    }

    private boolean csvFileExists(String csvFilePath) {
        File file = new File(csvFilePath);
        return file.exists();
    }

    private void writeHeadersToCSV(String csvFilePath, LinkedHashMap<String, String> headers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath))) {
            try (CSVWriter csvWriter = new CSVWriter(writer)) {
                csvWriter.writeNext(headers.keySet().toArray(new String[0]));
            }
        } catch (IOException e) {
            LOGGER.error("Error writing headers to CSV file: {}", e.getMessage());
        }
    }

    private void writeDataToCSV(String csvFilePath, SampleContentFragment sampleContentFragment) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath, true))) {
            try (CSVWriter csvWriter = new CSVWriter(writer)) {
                LinkedHashMap<String, String> data = sampleContentFragment.getAllProperties();
                csvWriter.writeNext(data.values().toArray(new String[0]));
            }
        } catch (IOException e) {
            LOGGER.error("Error writing data to CSV file: {}", e.getMessage());
        }
    }
}



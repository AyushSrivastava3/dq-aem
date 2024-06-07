package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.utils.ResolverUtil;
import com.day.cq.dam.api.AssetManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(immediate = true, service = Runnable.class)
@Designate(ocd = ReportGeneratorService.Config.class)
@ServiceDescription("Content Fragment Report Generator")
public class ReportGeneratorService implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ReportGeneratorService.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Scheduler scheduler;

    @ObjectClassDefinition(name = "Content Fragment Report Generator Configuration")
    public @interface Config {
        @AttributeDefinition(name = "Cron Expression", description = "Cron expression defining when to run the report generator")
        String scheduler_expression() default "0 30 08 * * ?";

        @AttributeDefinition(name = "Concurrent", description = "Whether or not to allow concurrent executions")
        boolean scheduler_concurrent() default false;
    }

    

    @Override
    public void run() {
        log.info("Report generation task started.");

        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            log.info("I have entered the try block that means resolver is not null");
            String[] contentFragmentModelsPaths = {"/conf/dq-aem/settings/dam/cfm/models"};
            log.info("contentFragmentModelsPaths :" + contentFragmentModelsPaths);

            Map<String, Integer> fragmentCountMap = new HashMap<>();

            for (String path : contentFragmentModelsPaths) {
                log.info("Path  in try block:" + path);
                Resource resource = resourceResolver.getResource(path);
                if (resource != null) {
                    for (Resource model : resource.getChildren()) {
                        log.info("model :" + model);
                        String modelName = model.getName();
                        log.info("model name in run method :" + modelName);
                        int fragmentCount = getContentFragmentCount(resourceResolver, path + "/" + modelName);
                        fragmentCountMap.put(modelName, fragmentCount);
                        log.info("Model: {} - Count: {}", modelName, fragmentCount);
                    }
                    log.info("Fragment count map :" + fragmentCountMap);
                } else {
                    log.warn("Resource not found for path: {}", path);
                }
            }

            byte[] fileContent = createExcelReport(fragmentCountMap);
            processAndUploadExcelFile(fileContent);
            log.info("Report generation task completed.");
        } catch (Exception e) {
            log.error("Error generating report", e);
        }
    }

    private int getContentFragmentCount(ResourceResolver resolver, String modelName) {
        log.info("I am in cf count method");
        log.info("Model Name in getContentFragmentCount: {}", modelName);

        // Adjusted query to be more generic for debugging
        String query = "SELECT * FROM [nt:unstructured] AS s " +
                "WHERE ISDESCENDANTNODE([/content/dam/dq-aem])";

        log.info("Query: {}", query);

        Iterator<Resource> fragments = resolver.findResources(query, "JCR-SQL2");
        int count = 0;

        while (fragments.hasNext()) {
            Resource fragment = fragments.next();
            log.info("Found fragment: {}", fragment.getPath());

            // Checking the properties of the node to see if cq:model exists
            ValueMap properties = fragment.getValueMap();
            if (properties.containsKey("cq:model")) {
                log.info("cq:model: {}", properties.get("cq:model", String.class));
                if (modelName.equals(properties.get("cq:model", String.class))) {
                    count++;
                }
            }
        }

        log.info("Total fragments found for model {}: {}", modelName, count);
        return count;
    }

    private byte[] createExcelReport(Map<String, Integer> fragmentCountMap) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Content Fragment Report");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Content Fragment Model");
        headerRow.createCell(1).setCellValue("Fragment Count");

        for (Map.Entry<String, Integer> entry : fragmentCountMap.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }

    public void processAndUploadExcelFile(byte[] fileContent) {
        try {
            // Extract data from Excel file
            String extractedData = ExcelReader.extractExcelData(new ByteArrayInputStream(fileContent));

            // Upload both the original file and the extracted data
            uploadToDAM(fileContent, extractedData);
        } catch (IOException e) {
            log.error("Error processing Excel file", e);
        }
    }

    private void uploadToDAM(byte[] fileContent, String textContent) {
        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
            if (assetManager != null) {
                // Upload the original Excel file
                InputStream excelStream = new ByteArrayInputStream(fileContent);
                assetManager.createAsset("/content/dam/reports/content_fragment_report.xlsx", excelStream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", true);
                log.info("Report uploaded to DAM at /content/dam/reports/content_fragment_report.xlsx");

                // Create a new text file with the extracted data
                InputStream textStream = new ByteArrayInputStream(textContent.getBytes(StandardCharsets.UTF_8));
                assetManager.createAsset("/content/dam/reports/content_fragment_report.txt", textStream, "text/plain", true);
                log.info("Extracted data uploaded to DAM at /content/dam/reports/content_fragment_report.txt");
            } else {
                log.error("AssetManager is null. Could not upload report to DAM.");
            }
        } catch (Exception e) {
            log.error("Error uploading file to DAM", e);
        }
    }
}


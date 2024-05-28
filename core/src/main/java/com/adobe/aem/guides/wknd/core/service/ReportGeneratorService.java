package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.utils.ResolverUtil;
import com.day.cq.dam.api.AssetManager;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.apache.sling.models.annotations.injectorspecific.Self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = Runnable.class)
@Designate(ocd = ReportGeneratorService.Config.class)
@ServiceDescription("Content Fragment Report Generator")
public class ReportGeneratorService implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ReportGeneratorService.class);

    @Inject
    @Self
    private Resource resource;

    @Inject
    private ResourceResolverFactory resourceResolverFactory; // Inject ResourceResolverFactory instead

    @Reference
    private Scheduler scheduler;


    @ObjectClassDefinition(name = "Content Fragment Report Generator Configuration")
    public @interface Config {

        @AttributeDefinition(name = "Cron Expression", description = "Cron expression defining when to run the report generator")
        String scheduler_expression() default "0 50 09 * * ?";

        @AttributeDefinition(name = "Concurrent", description = "Whether or not to allow concurrent executions")
        boolean scheduler_concurrent() default false;
    }

    @Activate
    protected void activate(Config config) {
        scheduleJob(config);
    }

    private void scheduleJob(Config config) {
        try {
            ScheduleOptions options = scheduler.EXPR(config.scheduler_expression())
                    .name("com.example.aem.service.ReportGeneratorService")
                    .canRunConcurrently(config.scheduler_concurrent());

            scheduler.schedule(this, options);
            log.info("Scheduled ReportGeneratorService with expression: {}", config.scheduler_expression());
        } catch (Exception e) {
            log.error("Error scheduling job", e);
        }
    }

    @Override
    public void run() {
        log.info("Report generation task started.");

        try (ResourceResolver resolver = getNewResolver(resourceResolverFactory)) {
            log.info("i have enter the try block that means resolver is not null");
            String[] contentFragmentModelsPaths = {"/conf/dq-aem/settings/dam/cfm/models"};

            Map<String, Integer> fragmentCountMap = new HashMap<>();

            for (String path : contentFragmentModelsPaths) {
                Resource resource = resolver.getResource(path);
                if (resource != null) {
                    for (Resource model : resource.getChildren()) {
                        String modelName = model.getName();
                        int fragmentCount = getContentFragmentCount(resolver, modelName);
                        fragmentCountMap.put(modelName, fragmentCount);
                        log.info("Model: {} - Count: {}", modelName, fragmentCount);
                    }
                } else {
                    log.warn("Resource not found for path: {}", path);
                }
            }

            createExcelReport(fragmentCountMap);
            log.info("Report generation task completed.");
        } catch (Exception e) {
            log.error("Error generating report", e);
        }
    }

    private ResourceResolver getNewResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException {
        log.info("in get new resolver method");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "ayush");
        log.info("paramMao :"+paramMap);
        ResourceResolver resolver=resourceResolverFactory.getServiceResourceResolver(paramMap);
        log.info("resolver: { }"+resolver);
        return resolver;
    }

    private int getContentFragmentCount(ResourceResolver resolver, String modelName) {
        String query = "SELECT * FROM [nt:unstructured] AS s WHERE ISDESCENDANTNODE([/content/dam]) AND s.[cq:model] = '" + modelName + "'";
        Iterable<Resource> fragments = (Iterable<Resource>) resolver.findResources(query, "JCR-SQL2");
        int count = 0;
        for (Resource fragment : fragments) {
            count++;
        }
        return count;
    }

    private void createExcelReport(Map<String, Integer> fragmentCountMap) throws Exception {
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

        File file = new File("/path/to/aem/dam/folder/content_fragment_report.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        }

        uploadToDAM(file);
    }

    private void uploadToDAM(File file) {
        try (ResourceResolver resolver = getNewResolver(resourceResolverFactory)) {
            AssetManager assetManager = resolver.adaptTo(AssetManager.class);
            if (assetManager != null) {
                try (InputStream is = new FileInputStream(file)) {
                    assetManager.createAsset("/content/dam/reports/" + file.getName(), is, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", true);
                    log.info("Report uploaded to DAM at /content/dam/reports/{}", file.getName());
                }
            } else {
                log.error("AssetManager is null. Could not upload report to DAM.");
            }
        } catch (Exception e) {
            log.error("Error uploading file to DAM", e);
        }
    }
}

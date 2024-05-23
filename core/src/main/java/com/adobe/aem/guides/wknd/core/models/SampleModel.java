package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.service.AnotherService;
import com.adobe.aem.guides.wknd.core.service.SampleService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

@Model(adaptables = Resource.class)
public class SampleModel {
    private String greeting;
    private String formalGreeting;
    private String completeGreeting;
    @OSGiService
    SampleService sampleService;
    @OSGiService
    AnotherService anotherService;
    public String getGreeting() {
        return sampleService.getGreeting();
    }

    public String getFormalGreeting() {
        return sampleService.getFormalGreeting();
    }
    public String getCompleteGreeting(){
        return anotherService.getCompleteGreeting();
    }
}

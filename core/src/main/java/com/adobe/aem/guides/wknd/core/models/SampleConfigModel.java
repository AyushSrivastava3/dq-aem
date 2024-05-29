package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.service.osgi.SampleConfig;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
@Model(adaptables = Resource.class)
public class SampleConfigModel {
//    private String greeting;
//    private String formalGreeting;
    @OSGiService
    SampleConfig sampleConfig;
    public String getGreeting() {
        return sampleConfig.getGreeting();
    }

    public String getFormalGreeting() {
        return sampleConfig.getFormalGreeting();
    }
}

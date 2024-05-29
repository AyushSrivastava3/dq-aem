package com.adobe.aem.guides.wknd.core.service;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AnotherService.class)
public class AnotherService {
    @Reference
    SampleService sampleService;
    public String getCompleteGreeting() {
        String greeting = sampleService.getGreeting();
        String formalGreeting = sampleService.getFormalGreeting();
        return greeting + " " + formalGreeting;
    }

}

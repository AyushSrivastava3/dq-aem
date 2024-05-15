package com.adobe.aem.guides.wknd.core.service;

import org.osgi.service.component.annotations.Component;

@Component(service = SampleService.class)
public class SampleService {
    public String getGreeting() {
        return "Hi Ayush !";
    }

    public String getFormalGreeting() {
        return "welcome to digiquad sol.";
    }
}

package com.adobe.aem.guides.wknd.core.service.osgi;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = SampleConfig.class)
@Designate(ocd = SampleServiceConfig.class)
public class SampleConfig {

    private String greetingMessage;
    private String formalGreetingMessage;

    @Activate
    @Modified
    protected void activate(SampleServiceConfig config) {
        this.greetingMessage = config.greetingMessage();
        this.formalGreetingMessage = config.formalGreetingMessage();
    }

    public String getGreeting() {
        return greetingMessage;
    }

    public String getFormalGreeting() {
        return formalGreetingMessage;
    }
}

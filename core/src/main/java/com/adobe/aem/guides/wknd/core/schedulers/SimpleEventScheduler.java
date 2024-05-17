package com.adobe.aem.guides.wknd.core.schedulers;

import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Component(immediate = true)
public class SimpleEventScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleEventScheduler.class);

    @Reference
    private EventAdmin eventAdmin;

    @Reference
    private Scheduler scheduler;

    @Activate
    protected void activate() {
        // Schedule the event to trigger every 5 seconds
        ScheduleOptions options = scheduler.NOW(-1, 5).name("MyEventScheduler");
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                // Trigger the custom event
                LOG.info("Event triggered!");
                eventAdmin.postEvent(new Event("myapp:event", new HashMap<>()));
            }
        }, options);
    }
}

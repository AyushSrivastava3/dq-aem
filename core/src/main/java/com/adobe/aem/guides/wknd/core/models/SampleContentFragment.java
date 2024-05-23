
package com.adobe.aem.guides.wknd.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SampleContentFragment {

    @Self
    private Resource resource;

    private LinkedHashMap<String, String> properties = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void init() {
        if (resource != null) {
            ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);
            if (contentFragment != null) {
                // Get all content elements of the content fragment
                Iterator<ContentElement> elements = contentFragment.getElements();
                while (elements.hasNext()) {
                    ContentElement element = elements.next();
                    String propertyName = element.getName();
                    String propertyValue = element.getContent();
                    // Store property dynamically in the map
                    properties.put(propertyName, propertyValue);
                }
            }
        }
    }

    // Retrieve property value by name from the map
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    // Get all properties
    public LinkedHashMap<String, String> getAllProperties() {
        return properties;
    }
}


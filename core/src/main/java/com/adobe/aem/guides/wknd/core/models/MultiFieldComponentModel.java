package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class , defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class MultiFieldComponentModel {

    @ValueMapValue
    private String label;

    @ValueMapValue
    private String linkPath;

    @ValueMapValue
    private String target;




    public String getLabel() {

        return label;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public String getTarget() {
        return target;
    }


}
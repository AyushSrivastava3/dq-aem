package com.adobe.aem.guides.wknd.core.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class MultiFieldModel {


    @ValueMapValue
    private String multifieldlabel;

    @ChildResource
    private List<MultiFieldComponentModel> links = new ArrayList<>();

    public String getMultifieldlabel() {
        return multifieldlabel;
    }

    public List<MultiFieldComponentModel> getLinks() {
        return links;
    }


}
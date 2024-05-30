package com.adobe.aem.guides.wknd.core.models;

import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentData;
import com.adobe.cq.dam.cfm.FragmentTemplate;
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)


public class SampleContentFragmentAnnotation {
	 public static final String CF_PATH = "/content/dam/dq-aem/car-model-cf";
	 
	 @Inject
	 @Self
	 private Resource resource;
	 /*
	  The @Self annotation in Sling Models (part of Adobe Experience Manager) is used to inject the current adaptable object into 
	  a Sling Model. This is particularly useful for accessing the current resource, request, or any other adaptable directly 
	  within your model class.
	  */
	 
	 @Inject
	 ResourceResolver resourceResolver;
	 
	 private Optional<ContentFragment> contentFragment;
	 
	 @PostConstruct
	 /*
	  This annotation can be used to run the logic once all the field level injections are done.
	  */
	 public void init() {
		  Resource fragmentResource = resourceResolver.getResource(CF_PATH);
		  contentFragment = Optional.ofNullable(fragmentResource.adaptTo(ContentFragment.class));
	
	 }
	 
	 public String getTitle() {
		  return contentFragment.map(cf -> cf.getElement("title")).map(ContentElement::getContent)
				    .orElse(StringUtils.EMPTY);
	 }
	 
	 public String getTags() {
		  return contentFragment.map(cf -> cf.getElement("tags")).map(ContentElement::getContent)
				    .orElse(StringUtils.EMPTY);
		  
}

}

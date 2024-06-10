package com.adobe.aem.guides.wknd.core.models;




import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class , defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class Corecomponent {

    @ValueMapValue
    @Default(values = "")
    private String title;

    @ValueMapValue
    @Default(values = "h1")
    private String type;

    @ValueMapValue
    private String linkURL;

    
    @ValueMapValue
    private String description;
    
    public String getDescription() {
		return description;
	}

	public String getText() {
		return text;
	}

	public String getFileReference() {
		return fileReference;
	}

	@ValueMapValue
    private String text;
    
    @ValueMapValue
    private String fileReference;
    
    @ValueMapValue
    private String id;

    @ValueMapValue
    private boolean checkbox;

    @ValueMapValue
    private String radiogroup;

    @ValueMapValue
    private String dropdown;

    @ValueMapValue
    private String datepicker;

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public String getId() {
        return id;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public String getRadiogroup() {
        return radiogroup;
    }

    public String getDropdown() {
        return dropdown;
    }

    public String getDatepicker() {
        return datepicker;
    }
}

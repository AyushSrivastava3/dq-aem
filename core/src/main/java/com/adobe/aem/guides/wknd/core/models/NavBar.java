
package com.adobe.aem.guides.wknd.core.models;



import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)

public class NavBar {

	@ValueMapValue
	private String nav1;

	@ValueMapValue
	private String nav2;

	@ValueMapValue
	private String nav3;

	@ValueMapValue
	private String nav4;

	@ValueMapValue
	private String nav5;

	@ValueMapValue
	private String nav6;

	public String getNav1() {
		return nav1;
	}

	public String getNav2() {
		return nav2;
	}

	public String getNav3() {
		return nav3;
	}

	public String getNav4() {
		return nav4;
	}

	public String getNav5() {
		return nav5;
	}

	public String getNav6() {
		return nav6;
	}

}

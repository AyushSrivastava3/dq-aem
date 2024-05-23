package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.aem.guides.wknd.core.service.WeatherData;
import com.adobe.aem.guides.wknd.core.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(
    service = { Servlet.class },
    property = {
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/details",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST
    }
)
public class WeatherServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServlet.class);

    @Reference
    private WeatherService weatherService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("WeatherServlet: Inside doPost method");

        // Retrieve the city name from the form submission
        String city = request.getParameter("cityName");
        LOGGER.info("City Name Received: {}", city);
        
        // Get weather data based on the city name
        WeatherData weatherData = weatherService.getWeatherData(city);
        LOGGER.info("Weather Data: {}", weatherData);

        // Set weather data as request attribute
        request.setAttribute("weatherData", weatherData);
        
        // Set content type to JSON
        response.setContentType("application/json");

        // Convert WeatherData object to JSON and write it to response
        ObjectMapper mapper = new ObjectMapper();
        String jsonWeatherData = mapper.writeValueAsString(weatherData);
        response.getWriter().write(jsonWeatherData);

        // Forward to the JSP file to display weather information
        //request.getRequestDispatcher("/apps/digisite/components/weather/weather.html").forward(request, response);
    }
}
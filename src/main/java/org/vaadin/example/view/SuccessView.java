package org.vaadin.example.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Person;
import org.vaadin.example.sneaker.service.PersonService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.vaadin.annotations.JavaScript;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;


@Route("success")

@JavaScript("https://js.stripe.com/v3/")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")
public class SuccessView extends VerticalLayout implements HasUrlParameter<String> {

	private PersonService personService;
	
	private String sessionId;
	
	public String key = "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";				
	
	public SuccessView (@Autowired PersonService personService) {
		this.personService = personService;
	}
	
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		Location location = event.getLocation();
		Stripe.apiKey = key;
		this.sessionId = location.getQueryParameters().getParameters().get("session_id").toString();
		if (sessionId != null && sessionId.length() > 0) {
            	String sessionSplited = sessionId.replace('[', ' ');
            	sessionSplited = sessionSplited.replace(']', ' ');
            	sessionSplited = sessionSplited.trim();
                System.out.println("SESSION ID STRIPE : " + sessionSplited);
            	try {
	            	Person person = personService.findByStripeId(sessionSplited);
	            	personService.confirmRegistation(person);
                	com.stripe.param.billingportal.SessionCreateParams params = new com.stripe.param.billingportal.SessionCreateParams.Builder()
    	                    .setReturnUrl("https://env-4428752.jcloud.ik-server.com/login" + 
    	                    		"")
    	                    //.setCustomer("cus_LG8Lp0XhYAuAaZ")
    	                    .setCustomer(person.getStripeId())    	                    		
    	                    .build();
    	            	com.stripe.model.billingportal.Session portalSession = com.stripe.model.billingportal.Session.create(params);
    	            	System.out.println(portalSession.getUrl());

	            	event.getUI().getPage().open(portalSession.getUrl(), "_self");
		            }catch (StripeException ex) {
		            	System.out.println(ex.getMessage());
		            }
				}
		}
}
	   


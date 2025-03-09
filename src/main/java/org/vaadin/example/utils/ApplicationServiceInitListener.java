package org.vaadin.example.utils;



import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import ch.carnet.kasparscherrer.LanguageSelect;

@org.springframework.stereotype.Component
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        LanguageSelect.readLanguageCookies(event);
    }
}
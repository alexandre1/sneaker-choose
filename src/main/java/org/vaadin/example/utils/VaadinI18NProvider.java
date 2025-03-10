package org.vaadin.example.utils;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.Locale.FRENCH;
import static java.util.ResourceBundle.getBundle;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.success;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.rapidpm.dependencies.core.logger.HasLogger;
import com.vaadin.flow.i18n.I18NProvider;

public class VaadinI18NProvider implements I18NProvider, HasLogger {
	
  public VaadinI18NProvider() {
    logger().info(VaadinI18NProvider.class.getSimpleName() + " was found..");
  }

  public static final String RESOURCE_BUNDLE_NAME = "vaadinapp";

  private static final ResourceBundle RESOURCE_BUNDLE_EN = getBundle(RESOURCE_BUNDLE_NAME , ENGLISH);
  private static final ResourceBundle RESOURCE_BUNDLE_DE = getBundle(RESOURCE_BUNDLE_NAME , GERMAN);
  private static final ResourceBundle RESOURCE_BUNDLE_FR = getBundle(RESOURCE_BUNDLE_NAME ,FRENCH);
  private static final Locale FR_CH = new Locale("fr", "CH");
  private static final ResourceBundle RESOURCE_BUNDLE_FR_CH = getBundle(RESOURCE_BUNDLE_NAME , FR_CH);
  private static final List<Locale> providedLocales = unmodifiableList(asList(ENGLISH , GERMAN, FRENCH, FR_CH));
   

  @Override
  public List<Locale> getProvidedLocales() {
    logger().info("VaadinI18NProvider getProvidedLocales..");
    return providedLocales;
  }

  @Override
  public String getTranslation(String key , Locale locale , Object... params) {
    logger().info("VaadinI18NProvider getTranslation.. key : " + key + " - " + locale);
    return match(
        matchCase(() -> success(RESOURCE_BUNDLE_EN)) ,
        matchCase(() -> GERMAN.equals(locale) , () -> success(RESOURCE_BUNDLE_DE)) ,
        matchCase(() -> ENGLISH.equals(locale) , () -> success(RESOURCE_BUNDLE_EN)),
        matchCase(() -> FRENCH.equals(locale) , () -> success(RESOURCE_BUNDLE_FR)),
        matchCase(() -> FR_CH.equals(locale) , () -> success(RESOURCE_BUNDLE_FR_CH))
        
    )
        .map(resourceBundle -> {
          if (! resourceBundle.containsKey(key))
            logger().info("missing ressource key (i18n) " + key);

          return (resourceBundle.containsKey(key)) ? resourceBundle.getString(key) : key;

        })
        .getOrElse(() -> key + " - " + locale);
  }

}
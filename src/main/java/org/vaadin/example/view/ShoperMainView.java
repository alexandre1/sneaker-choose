package org.vaadin.example.view;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vaadin.example.entity.Shoe;
import org.vaadin.example.service.GreetService;
import org.vaadin.example.sneaker.service.ShoeService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.example.utils.ShoeRequest;
import org.vaadin.example.utils.VaadinI18NProvider;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route("shoperMainView")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")

public class ShoperMainView extends VerticalLayout {

	  private Component previousPhoto;
  
	  final Image image = new Image();

	  private Paragraph photoName;
	  
	  public Image newShoes;
	  
	  private Grid<ShoeRequest> grid = new Grid<>(ShoeRequest.class);
	  
	  private Label labelSize = new Label ("Nombre de boutiques touché");
	  
	  static Set<PosixFilePermission> defaultPosixPermissions = null;
	  
	  private MemoryBuffer buffer = new MemoryBuffer();
	  
	  private static final long serialVersionUID = 4L;
	  
	  private File targetFile;
	  
	  private ShoeService shoeService;
	  
	  @Autowired
	  private Environment env;
	  
	  private static final String LABEL_MENU_ABOUT = "lbl_menu_about";
		
	  private static final String LABEL_MENU_CONSULT_ACTUAL_QUERIES = "lbl_menu_view_actual_queries";
		
	  private static final String LABEL_MENU_CONSULT_REALIZED_QUERIES = "lbl_menu_view_realized_queries";
		
	  private static final String LABEL_MENU_EDIT_ACCOUNT = "lbl_edit_account";
		
	  private static final String LABEL_MENU_CREATE_ACCOUNT = "lbl_create_shop_account";
		
	  private static final String LABEL_MENU_PRIVACY = "lbl_privacy";

	  

	public String getAppPath() {
		return env.getProperty("spring.servlet.multipart.location");
	}

	static {
	      defaultPosixPermissions = new HashSet<>();
	      defaultPosixPermissions.add(PosixFilePermission.OWNER_READ);
	      defaultPosixPermissions.add(PosixFilePermission.OWNER_WRITE);
	      defaultPosixPermissions.add(PosixFilePermission.OWNER_EXECUTE);
	      defaultPosixPermissions.add(PosixFilePermission.GROUP_READ);
	      defaultPosixPermissions.add(PosixFilePermission.GROUP_WRITE);
	      //Others have read permission so that ftp user who doesn't belong to the group can fetch the file
	      defaultPosixPermissions.add(PosixFilePermission.OTHERS_READ);
	      defaultPosixPermissions.add(PosixFilePermission.OTHERS_WRITE);
	  }
    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public ShoperMainView(@Autowired GreetService service, @Autowired ShoeService shoeService) {
    	this.setClassName("background-image2");
    	this.shoeService = shoeService;
		String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
	    Html html = new Html(content);			    
	    add(html);

    	
    	buildMenu();	
        addClassName("v-margin-left");
        HorizontalLayout horizontalLyout = new HorizontalLayout();
    	buildGrid();
        Button buttonClean = new Button("Réinitialiser",
                e ->  {        		
                	removeOutput();
                }
        	);

        horizontalLyout.add( buttonClean);
        add(horizontalLyout);

    }

    private Component createComponent(String mimeType, String fileName, InputStream stream) {
    	 if (mimeType.startsWith("image")) {
             Image image = new Image();
             try {
                 byte[] bytes = IOUtils.toByteArray(stream);
                 URL url = this.getClass().getResource("/upload/");
                 image
                     .getElement()
                     .setAttribute("src", new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));                
                 try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {

                     final Iterator readers = ImageIO.getImageReaders(in);
                     if (readers.hasNext()) {
                         ImageReader reader = (ImageReader) readers.next();                                                
                             reader.setInput(in);
                             image.setMaxWidth("100%");
                             image.setSrc(new StreamResource("upload", () -> new ByteArrayInputStream(bytes)));
                             image.setVisible(true);                             
                         	 reader.dispose();
                         }
                     }                
             } catch (IOException e) {
                 e.printStackTrace();
             }                         
             return image;
         }
         Div content = new Div();
         String text = String.format(
             "Mime type: '%s'\nSHA-256 hash: '%s'",
             mimeType,
             Arrays.toString(MessageDigestUtil.sha256(stream.toString()))
         );
         content.setText(text);
         return content;
     }
    
  
    private void showOutput(String text, Component content, HasComponents outputContainer) {
        if (photoName != null) {
            outputContainer.remove(photoName);
        }
        if (previousPhoto != null) {
            outputContainer.remove(previousPhoto);
        }
        photoName = new Paragraph(text);
        outputContainer.add(photoName);
        previousPhoto = content;
        outputContainer.add(previousPhoto);
    }

    private void buildGrid () {
    	
    	List<ShoeRequest> personList = new ArrayList<>();    	
    	Double price = new Double("600.5");
    	Double price2 = new Double("100.5");
    	ShoeRequest shoeReq1 = new ShoeRequest ();    	
    	shoeReq1.setEmailWisher("alexjaquet@gmail.com");
    	shoeReq1.setMarque("Nike");
    	shoeReq1.setSex("Homme");
    	shoeReq1.setSize("44 EU");
    	
    	personList.add(shoeReq1);
    	labelSize = new Label("Nombre de chaussures en demandes " + personList.size());
    	add(labelSize);
    	grid.setMinWidth("1200px");    	
    	for (int i = 0; i < 1; i++) {
        	grid.addColumn(TemplateRenderer.<ShoeRequest>
            of("<img src=[[item.photoSource]] style=\"height: 240px; border-radius: 50%;\">")
            .withProperty("photoSource", ShoperMainView::randomProfilePictureUrl))
            .setWidth("360px")
            .setFlexGrow(0);    		
    	}
    	
    	grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
    	        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
    	grid.setItems(personList);
        grid.addComponentColumn(item -> createRemoveButton(grid, item)).setHeader("Actions");

        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setClassName("v-margin-right");
        grid.setHeight("900px");
        grid.setMinHeight("650px");
        add(grid);
    }

    private Button createRemoveButton(Grid<ShoeRequest> grid, ShoeRequest item) {
        @SuppressWarnings("unchecked")
		Shoe shoes = new Shoe();

        Button button = new Button("Voir", clickEvent -> {
        	ViewRequestShoes editor = new ViewRequestShoes (shoes, shoeService, getAppPath());
            editor.open();
            editor.setVisible(true);
            
        });
        return button;
    }
    
    private static String randomProfilePictureUrl(Object context) {
    	int tmp = (1 + (int) (Math.random() * 100));
    	if (tmp < 1) {
    		System.out.println("Result : test 1");
    		return "http://localhost:8084/icons/test1.png";
    	} else {
    		System.out.println("Result : test 2");
    		return "http://localhost:8084/icons/test2.png";
    	}
    	//String result = "http://localhost:8081/icons/test"        + (1 + (int) (Math.random() * 100) + ".png");
    	//System.out.println("Result : " + result);
        //return result;    
    }


    private void removeOutput() {
        this.remove(grid);
        this.remove(labelSize);
    }
    
    @SuppressWarnings("deprecation")
	private void buildMenu() {
    	MenuBar menuBar = new MenuBar();
    	VaadinI18NProvider provider = new VaadinI18NProvider();
    	Text selected = new Text("");
    	Div message = new Div(new Text("Selected: "), selected);
    	MenuItem home = menuBar.addItem("Home",
    			e ->  {
    				selected.setText("Home");
    				UI.getCurrent().navigate("");
    			}
    	);;
    	MenuItem about = menuBar.addItem(provider.getTranslation(LABEL_MENU_ABOUT, getLocale()));
    	
    	MenuItem account = menuBar.addItem("Compte");
    	menuBar.addItem("Sign Out", 
    			e -> {
    				VaadinSession.getCurrent().getSession().invalidate();
    				UI.getCurrent().getPage().executeJavaScript("window.location.href=''");
    			}
    	);
    	SubMenu projectSubMenu = about.getSubMenu();
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CONSULT_ACTUAL_QUERIES, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_CONSULT_ACTUAL_QUERIES, getLocale()));
    				UI.getCurrent().navigate("ShopAskShoes");
    			}
    	);
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CONSULT_REALIZED_QUERIES, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_CONSULT_REALIZED_QUERIES, getLocale()));
    				UI.getCurrent().navigate("shoperHistoricMainView");
    			}
    	);

    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()));
    				UI.getCurrent().navigate("editShop");
    			}
    	);
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()));
    				UI.getCurrent().navigate("createProfileShop");
    			}
    	);
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_PRIVACY, getLocale()),
    	        e -> {
    	        	selected.setText(provider.getTranslation(LABEL_MENU_PRIVACY, getLocale()));
    	        	UI.getCurrent().navigate( "map");
    	        }				
    			);
    	
    	add(menuBar, message);
    	
    }
 
 
}
package org.vaadin.valerrdisp.demo;

import java.util.*;

import javax.servlet.annotation.*;

import org.vaadin.valerrdisp.*;
import org.vaadin.valerrdisp.client.*;

import com.vaadin.annotations.*;
import com.vaadin.data.*;
import com.vaadin.data.fieldgroup.*;
import com.vaadin.data.fieldgroup.FieldGroup.*;
import com.vaadin.data.validator.*;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.*;
import com.vaadin.ui.Notification.*;

@Theme("demo")
@Title("Validation Error Display Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.valerrdisp.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
    	// From styles.css : Disable tooltips for the entire UI
    	/*
    	 * 
    	   .v-tooltip {
    			display: none !important;
    			visibility: invisible !important;
			}
    	 */
    	
    	// Example using FormLayout
    	MyFormLayout formLayout = new MyFormLayout();
    	formLayout.setWidth(80.0f, Unit.PERCENTAGE);
    	formLayout.setHeight(400.0f, Unit.PIXELS);
    	
    	// Example using HorizontalLayout
    	MyHorzLayout horzLayout = new MyHorzLayout();
    	horzLayout.setWidth(80.0f, Unit.PERCENTAGE);
    	horzLayout.setHeight(400.0f, Unit.PIXELS);
    	
    	// Example using VerticalLayout
    	MyVertLayout vertLayout = new MyVertLayout();
    	vertLayout.setWidth(80.0f, Unit.PERCENTAGE);
    	vertLayout.setHeight(400.0f, Unit.PIXELS);
    	
    	// Outer wrapper to center child grid
    	VerticalLayout wrapperLayout = new VerticalLayout();
    	wrapperLayout.setSizeFull();
    	wrapperLayout.setMargin(true);
    	wrapperLayout.setSpacing(true);
    	
    	setContent(wrapperLayout);
    	
    	// Instructions
    	Panel instrPanel = new Panel("Instructions");
    	instrPanel.setWidth(90.0f, Unit.PERCENTAGE);
    	Label instrLabel = new Label("Enter text into the fields as suggested by the placeholders to see the validation errors rendered, or click the submit button to "
    			+ "validate entire form.");
    	VerticalLayout instrWrapper = new VerticalLayout(instrLabel);
    	instrWrapper.setSpacing(true);
    	instrWrapper.setMargin(true);
    	
    	instrPanel.setContent(instrWrapper);
    	
    	// Grid for displaying each form panel
    	GridLayout gridLayout = new GridLayout(2,3);
    	gridLayout.setSizeFull();
    	gridLayout.setMargin(true);
    	gridLayout.setSpacing(true);
    	
    	gridLayout.addComponent(instrPanel, 0, 0, 1, 0);
    	gridLayout.addComponents(formLayout, horzLayout, vertLayout);
    	
    	gridLayout.setComponentAlignment(instrPanel, Alignment.TOP_CENTER);
    	gridLayout.setComponentAlignment(formLayout, Alignment.TOP_CENTER);
    	gridLayout.setComponentAlignment(horzLayout, Alignment.TOP_CENTER);
    	gridLayout.setComponentAlignment(vertLayout, Alignment.TOP_CENTER);
    	
    	wrapperLayout.addComponent(gridLayout);
    	wrapperLayout.setComponentAlignment(gridLayout, Alignment.TOP_CENTER);
    	
    }
    
// Helper Classes
    /**
     * Simple POJO that we'll edit in the various forms.
     */
    public class Person {
    	// Constructors
    	public Person() {
    	}
    	
    	public Person(String name, Integer age, String favoriteColor) {
    		this.name = name;
    		this.age = age;
    		this.favoriteColor = favoriteColor;
    	}
    	
    	// Public Methods
    	
    	// Getters & Setters
    	public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getFavoriteColor() {
			return favoriteColor;
		}

		public void setFavoriteColor(String favoriteColor) {
			this.favoriteColor = favoriteColor;
		}

		// Attributes
    	private String name;
    	
    	private Integer age;
    	
    	private String favoriteColor;
    }
    
    
    /**
     * ComboBox for picking favorite color
     */
    public class ColorComboBox extends ComboBox {
    	// Constructors
    	public ColorComboBox(String caption) {
    		super(caption);
    		
    		addItem("Red");
    		addItem("Blue");
    		addItem("Green");
    		addItem("Yellow");
    	}
    }
    
    /**
     * Base for all the forms shown in the demo code.  Just sets up all form fields and adds them to a BeanFieldGroup.  The subclass will then
     * lay them out on the UI.
     */
    public abstract class BaseLayout extends Panel {
    	// Constructors
    	public BaseLayout(String caption) {
    		// Set a header caption
    		super(caption);
    		
    		// Name Field
        	nameField = new TextField("Name");
        	nameField.setRequired(true);
        	nameField.setRequiredError("Required");
        	nameField.setNullRepresentation("");
        	nameField.setWidth(225.0f, Unit.PIXELS);
        	nameField.setInputPrompt("Starts with J and ends with P");
        	
        	nameField.addValidator(Validators.STARTS_WITH_J_VALIDATOR);
        	nameField.addValidator(Validators.ENDS_WITH_P_VALIDATOR);
        	
        	nameField.setValidationVisible(false);
        	nameField.addBlurListener(new ValidationBlurListener(nameField));
        	
        	// Age Field
        	ageField = new TextField("Age");
        	ageField.setRequired(true);
        	ageField.setRequiredError("Required");
        	ageField.setNullRepresentation("");
        	ageField.setConversionError("Not an integer");
        	ageField.setInputPrompt("Greater than 99");
        	
        	ageField.addValidator(Validators.AGE_RANGE_VALIDATOR);
        	
        	ageField.setValidationVisible(false);
        	ageField.addBlurListener(new ValidationBlurListener(ageField));
        	
        	// Favorite Color Field
        	colorField = new ColorComboBox("Fav Color");
        	colorField.setRequired(true);
        	colorField.setRequiredError("Required");
        	colorField.setInputPrompt("Select Green");
        	
        	colorField.addValidator(Validators.HATE_GREEN_VALIDATOR);
        	
        	colorField.setValidationVisible(false);
        	colorField.addBlurListener(new ValidationBlurListener(colorField));
        	
        	
        	// Field Group
        	final BeanFieldGroup<Person> fieldGroup = new BeanFieldGroup<Person>(Person.class);
        	fieldGroup.bindMemberFields(this);
        	fieldGroup.setItemDataSource(new Person());
        	
        	fieldGroup.setBuffered(true);
        	
        	// Save Button
        	saveButton = new Button("Save", new ClickListener() {
    			@Override
    			public void buttonClick(ClickEvent event) {
    				try {
    					// Enable validations before committing so we pick up validations errors on commit.
    					setAllFieldsValidationVisible(true);
    					
    					fieldGroup.commit();
    					
    					Notification.show("Person Saved", Type.HUMANIZED_MESSAGE);
    					
    					// Load a new person
    					fieldGroup.setItemDataSource(new Person());
    				}
    				catch(CommitException e) {
    					Notification.show("Errors exist on the form.", Type.ERROR_MESSAGE);
    				}
    			}
    		});
    	}
    	
    	// Protected Methods
    	/**
    	 * Update each field, setting its validationVisible property to the argument value.
    	 * @param isValidationVisible True if validations should be performed/visible
    	 */
    	protected void setAllFieldsValidationVisible(boolean isValidationVisible) {
    		for(AbstractField<?> field : Arrays.asList(new AbstractField[] {nameField, ageField, colorField})) {
				field.setValidationVisible(isValidationVisible);
			}
    	}
    	
    	// UI Attributes
    	@PropertyId("name")
    	protected final TextField nameField;
    	
    	@PropertyId("age")
    	protected final TextField ageField;
    	
    	@PropertyId("favoriteColor")
    	protected final ColorComboBox colorField;
    	
    	protected final Button saveButton;
    }
    
    /**
     * Basic example using ValidationErrorDisplay.displayFor(Field, ErrorMessagePlacement) for each field individually.
     * 
     * A FormLayout displays the fields.
     */
    public class MyFormLayout extends BaseLayout {
    	// Constructors
    	public MyFormLayout() {
    		super("FormLayout Example - Errors Displayed Right of Fields");
        	
    		// Extend each field to display validation messages to the right of the field
    		ValidationErrorDisplay.displayFor(nameField, ErrorMessagePlacement.RIGHT);
    		ValidationErrorDisplay.displayFor(ageField, ErrorMessagePlacement.RIGHT);
    		ValidationErrorDisplay.displayFor(colorField, ErrorMessagePlacement.RIGHT);
    		
        	// Form Layout
        	FormLayout formLayout = new FormLayout(nameField, ageField, colorField, saveButton);
        	
        	// Add err-form style to hide Vaadin default error indicator ("!")
        	formLayout.addStyleName("err-form");
        	
        	formLayout.setMargin(true);
        	formLayout.setSpacing(true);
        	
        	setContent(formLayout);
    	}
    }
    
    /**
     * Example using top level ErrorMessagePlacement by applying the err-form-below style to the fields' container.
     *
     * A HorizontalLayout displays the fields
     */
    public class MyHorzLayout extends BaseLayout {
    	// Constructors
    	public MyHorzLayout() {
    		super("HorizontalLayout Example - Errors Displayed Below Fields");
    		
    		// Extend each field to display validation messages - no placement specified, so default is used as defined by top level container (horzLayout)
    		ValidationErrorDisplay.displayFor(nameField);
    		ValidationErrorDisplay.displayFor(ageField);
    		ValidationErrorDisplay.displayFor(colorField);
    		
    		HorizontalLayout horzLayout = new HorizontalLayout(nameField, ageField, colorField);
    		
    		// Hides the Vaadin default error indicator and displays error messages below fields by default
    		horzLayout.addStyleName("err-form-below");
    		
    		horzLayout.setSpacing(true);
    		
    		// Move save button below
    		VerticalLayout wrapperLayout = new VerticalLayout(horzLayout, saveButton);
    		wrapperLayout.setMargin(true);
    		wrapperLayout.setSpacing(true);
    		
    		setContent(wrapperLayout);
    	}
    }
    
    public class MyVertLayout extends BaseLayout {
    	// Constructors
    	public MyVertLayout() {
    		super("VerticalLayout Example - Errors Displayed Right of Fields (Except Age)");
    		
    		// Extend each field to display validation messages - single field level override defined for ageField
    		ValidationErrorDisplay.displayFor(nameField);
    		ValidationErrorDisplay.displayFor(ageField, ErrorMessagePlacement.BELOW);		// Define a field level override
    		ValidationErrorDisplay.displayFor(colorField);
    		
    		VerticalLayout vertLayout = new VerticalLayout(nameField, ageField, colorField, saveButton);
    		
    		// Hides the Vaadin default error indicator and displays error messages to the right of fields by default
    		vertLayout.addStyleName("err-form-right");
    		
    		vertLayout.setMargin(true);
    		vertLayout.setSpacing(true);
    		
    		setContent(vertLayout);
    	}
    }
    
    public static class Validators {
		/**
		 * Validate the age is between 0 and 99
		 */
		public static final IntegerRangeValidator AGE_RANGE_VALIDATOR = new IntegerRangeValidator("Must be between 0 and 99", 0, 99);
		
		/**
		 * Validate that value is not "Green"
		 */
		public static final Validator HATE_GREEN_VALIDATOR = new Validator() {
			@Override
			public void validate(Object value) throws InvalidValueException {
				String strVal = (String) value;
				
				if("Green".equalsIgnoreCase(strVal)) {
					throw new InvalidValueException("Nobody likes green");
				}
			}
		};
		
		/**
		 * Validate that name does not start with J
		 */
		public static final Validator STARTS_WITH_J_VALIDATOR = new Validator() {
			@Override
			public void validate(Object value) throws InvalidValueException {
				String strVal = (String) value;
				
				if(strVal != null && strVal.trim().toUpperCase().startsWith("J")) {
					throw new InvalidValueException("Your name cannot start with J");
				}
			}
		};
		
		/**
		 * Validate that name does not end with P
		 */
		public static final Validator ENDS_WITH_P_VALIDATOR = new Validator() {
			@Override
			public void validate(Object value) throws InvalidValueException {
				String strVal = (String) value;
				
				if(strVal != null && strVal.trim().toUpperCase().endsWith("P")) {
					throw new InvalidValueException("Your name cannot end with P");
				}
			}
		};
		
    }

}

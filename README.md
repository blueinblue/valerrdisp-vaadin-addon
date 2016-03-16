# Validation Error Display Add-on for Vaadin 7

The Validation Error Display Add-on helps you display validation error messages directly on the UI, near the field in error.

When an input field has a validation or conversion error, Vaadin's default behavior is to 1) place a red exclamation point next to the field, 2) change the field's background color to red, and 3) add a tooltip which 
displays the error message when the user hovers their pointer over the field.

If you'd rather display the validation error messages directly on the UI, then this add-on may help you. 

## Online demo

Try the add-on demo at <http://env-4028800.jelastic.servint.net/valerrdisp-demo/>

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/valerrdisp

## Building and running demo

	git clone <https://github.com/blueinblue/valerrdisp-vaadin-addon.git>
	mvn clean install
	cd demo
	mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Issue tracking

If you find a bug or have a feature request, please navigate to <https://github.com/blueinblue/valerrdisp-vaadin-addon/issues> and click the green "New Issue" button to report. 

## License & Author

Add-on is distributed under MIT License. For license terms, see LICENSE.txt.

The Validation Error Display Add-on is maintained by <https://github.com/blueinblue>.  Contact me there if you have questions.

# Developer Guide

## Getting started
I **highly** recommened downloading the demo application from github and examining the DemoUI for a complete overview.


The Validation Error Display Add-on comes packaged with a SASS stylesheet that's required for the add-on to function.  Unfortunately, Vaadin does not seem to be correctly processing SASS from add-ons 
at this time, despite the documentation's claims to the otherwise.

According to the documentation, when you add an Add-on that has a SASS stylesheet, the Vaadin compiler should automatically update your project's theme to pull in and compile
the style sheet on the fly.  The Vaadin Eclipse Plugin or the Vaadin Maven Plugin should update the addons.scss file, adding a reference to the Add-on's Sass stylesheet.  Despite
many tries, I wasn't able to get this to work correctly.  Hopefully it'll be fixed in future versions.  Below are the steps you need to follow for the add-on to work correclty.

To start using the Add-on, first add it as a dependency to your pom.xml (or equivalent):

	<dependency>
		<groupId>org.vaadin</groupId>
		<artifactId>valerrdisp</artifactId>
		<version>${version}</version>
	</dependency>

Update ${version} to the latest stable release.

Next, compile your widget-set:

	mvn vaadin:clean vaadin:compile vaadin:update-widgetset

After it compiles, do a quick check of your addons.scss to see if Vaadin added the SASS correctly.  The file should be here:

	webapp/VAADIN/themes/{your theme name}/addons.scss
	
If the file exists, and Vaadin behaved correctly, you should see the following two lines some where in the file:

	@import "../../../VAADIN/addons/valerrdisp/valerrdisp.scss";
	
	@mixin addons {
		@include valerrdisp;
	}
	
There should also be the following in your project's main styles.sccs located at:

	webapp/VAADIN/themes/{your theme name}/styles.scss
	
	@import "addons.scss";
	.
	.
	.yourthemename {
		@include addons;
	}
	
If both of these conditions are met, then you should just compile your widgetset and you should be ready to go.  Otherwise, you will need to do the following:

In your theme folder, create the following file:

	webapp/VAADIN/themes/{your theme name}/valerrdisp-cust.scss
	
Then add the following text to it:

	/* Customization for valerrdisp */
	@import "../../../VAADIN/addons/valerrdisp/valerrdisp.scss";
	@mixin valerrdisp-cust {
		@include valerrdisp;
	}
	
Now, update your styles.scss:

	webapp/VAADIN/themes/{your theme name}/styles.scss
	
To the top of the file, add the import statement:

	@import "valerrdisp-cust.scss";
	
Within the top level style defintion, add the following:

	@include valerrdisp-cust;
	
The completed file will look as such (for example - your file will have different names):

	@import "../valo/valo.scss";
	@import "addons.scss";
	@import "valerrdisp-cust.scss";

	// Prefix all selectors in your theme with .demo
	.demo {
		// Include valo theme styles in your theme
		@include valo;
		@include addons;
		@include valerrdisp-cust;
	
		... rest of your custom style selectors ...
	}

Finally, compile your widgetset:
	
	mvn vaadin:clean vaadin:compile vaadin:update-widgetset
	
And then refresh your entire project by right clicking the top level folder in Eclipse and selecting "Refresh".  You should be ready to rock!

## Getting started - Using the Add-on in Your Code

Here's a basic form configured to show validation error messages using the add-on.

	/**
     * Create a FormLayout component populated with auto-validating fields
     * @return A FormLayout component, never null
     */
    private Component getSampleForm() {
    	/*
    	 * Simple PropertysetItem to back form
    	 */
    	final PropertysetItem item = new PropertysetItem();
    	
    	item.addItemProperty("name", new ObjectProperty<String>("", String.class));
    	item.addItemProperty("age", new ObjectProperty<Integer>(null, Integer.class));
    	item.addItemProperty("catchPhrase", new ObjectProperty<String>("", String.class));
    	
    	/*
    	 * Build Fields
    	 */
    	
    	// Name Field
    	final TextField nameField = new TextField("Name");
    	nameField.setNullRepresentation("");
    	nameField.setRequired(true);
    	nameField.setRequiredError("Required");
    	
    	nameField.addValidator(new StringLengthValidator("Must be 5 to 12 characters", 5, 12, false));
    	
    	// Disable validation for initial form display; add a ValidationBlurListener (included with add-on) to trigger validation onBlur
    	nameField.setValidationVisible(false);
    	nameField.addBlurListener(new ValidationBlurListener(nameField));
    	
    	// Extend the name field with the Validation Error Display add-on - explictly setting the placement
    	ValidationErrorDisplay.displayFor(nameField, ErrorMessagePlacement.BELOW);
    	
    	
    	// Age Field
    	final TextField ageField = new TextField("Age");
    	ageField.setNullRepresentation("");
    	ageField.setRequired(true);
    	ageField.setRequiredError("Required");
    	
    	ageField.addValidator(new IntegerRangeValidator("Must be 0 to 99", 0, 99));
    	ageField.setConversionError("Invalid integer");
    	
    	// Disable initial validation; add a ValidationBlurListener
    	ageField.setValidationVisible(false);
    	ageField.addBlurListener(new ValidationBlurListener(ageField));
    	
    	// Extend to show error messages on UI - not setting the placement, allowing the form container to determine
    	ValidationErrorDisplay.displayFor(ageField);
    	
    	
    	// Catch phrase Field
    	final TextField catchPhraseField = new TextField("Catch Phrase");
    	catchPhraseField.setNullRepresentation("");
    	
    	catchPhraseField.addValidator(new Validator() {
			@Override
			public void validate(Object value) throws InvalidValueException {
				if("EAT MY SHORTS".equalsIgnoreCase(value == null ? "" : value.toString())) {
					throw new InvalidValueException("Sorry, that's taken");
				}
			}
		});
    	
    	catchPhraseField.setValidationVisible(false);
    	catchPhraseField.addBlurListener(new ValidationBlurListener(catchPhraseField));
    	
    	ValidationErrorDisplay.displayFor(catchPhraseField);
    	
    	
    	/*
    	 * Field Group to bind fields to PropertysetItem
    	 */
    	final FieldGroup fieldGroup = new FieldGroup(item);
    	fieldGroup.bind(nameField, "name");
    	fieldGroup.bind(ageField, "age");
    	fieldGroup.bind(catchPhraseField, "catchPhrase");
    	
    	// Buffer so validations also occur on commit
    	fieldGroup.setBuffered(true);
    	
    	/*
    	 * Submit button to commit field changes to underlying item
    	 */
    	Button saveButton = new Button("Save", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					// Set all validation visible before calling commit so validation will occur
					for(AbstractField<?> field : Arrays.asList(new AbstractField[] {nameField, ageField, catchPhraseField})) {
						field.setValidationVisible(true);
					}
					
					// Calling commit triggers validation of all bound fields - validation errors will be displayed if any exist
					fieldGroup.commit();
					
					// If no field errors, then no exception is thrown and save can proceed
					Notification.show("Save succeeded.", Type.HUMANIZED_MESSAGE);
					
					
					// Disable validations again and then clear the form to simulate loading a new backing object
					for(AbstractField<?> field : Arrays.asList(new AbstractField[] {nameField, ageField, catchPhraseField})) {
						field.setValidationVisible(false);
					}
					
					fieldGroup.clear();
				}
				catch(CommitException e) {
					Notification.show("Errors exist on form.", Type.ERROR_MESSAGE);
				}
			}
		});
    	
    	
    	/*
    	 * Form layout to display everything 
    	 */
    	FormLayout formLayout = new FormLayout(nameField, ageField, catchPhraseField, saveButton);
    	
    	// Add one of the following style classes to define default error message placement for all contained fields that don't have explict overrides
    	// Possible values: err-form, err-form-right, err-form-above, err-form-below
    	formLayout.addStyleName("err-form-right");
    	
    	return formLayout;
    }

For a complete example, pull the valerrdisp add-on from git and examine the DemoUI class in the valerrdisp-demo project.

	git clone <https://github.com/blueinblue/valerrdisp-vaadin-addon.git>

## Features

### Add Visible Validation to a Field
To add visible validation to a field, simply extend your field instance:

	final TextField nameField = new TextField("Name");
	ValidationErrorDisplay.displayFor(nameField, ErrorMessagePlacement.RIGHT);

Possible values for ErrorMessagePlacement:
	
* RIGHT
* ABOVE
* BELOW
	
### Container Level Styling / Defaults
Invididual fields are typically placed into a container, ie FormLayout, VerticalLayout, HorizontalLayout, etc.

By setting a container style class, you can define defaults and also trigger certain style rules that make your form look better
when using the Validation Error Display Add-on.

Possible values for the container level style class name:

* err-form
* err-form-right
* err-form-above
* err-form-below 

Set the class name as such:

	FormLayout formLayout = new FormLayout();
	formLayout.addStyleName("err-form-right");
	
You could then extend a field without specifying the error message placement, instead relying on the container level default:

	final TextField nameField = new TextField("Name");
	ValidationErrorDisplay.displayFor(nameField);
	
With the above style set, all error messages will appear to the right of the form fields.  An individual form field can override this placement by
explictly setting a placement:

	final TextField nameField = new TextField("Name");
	ValidationErrorDisplay.displayFor(nameField, ErrorMessagePlacement.RIGHT);
	
By setting one of the container level style class names, the following CSS modifications are applied to the container:
* Hide the default Vaadin error indicator (red ! by default)
* Set a default error placement for each field

### Validation Blur Listener
Vaadin, by default, validates all fields as soon as they are shown on the UI.  This is problematic when you first present the form to the user.  You don't want a bunch of red x's appearing before the user has even done anything with the form.

As seen in the sample code above, the Add-on package includes a ValidationBlurListener.  This blur listener, when attached to a field by calling the field's addBlurListener() method, will enable validation on the field when the user exits the field.  Using this, we can initially disable field validation and then allow the blur listener to enable it once the user has visited the field.

The typical flow is:

	Create a field, set its validationVisible attribute to false
	Attach a ValidationBlurListener to the field
	Attach the field to a container component
	If using buffered field groups, add logic to enable all validations before commit()

This feature was generalized from Nicolas Frankel's excellent article: <https://dzone.com/articles/bean-validation-vaadin-7>	

### Disable Vaadin ToolTips (Optional)
With the add-on handling your validation error messages, you may want to disable Vaadin's default tooltip behavior.  The default behavior shows
error messages in a tooltip when the user hovers their mouse over a field.  If you'd like to disable this tooltip, then add the following 
selector to your theme's styles.scss:

	.v-tooltip {
    	display: none !important;
    	visibility: invisible !important;
	}

### Modify Add-on Appearance
By changing some CSS syles, you can customize how error messages provided by the add-on appear on the UI.

**NOTE:** You must recompile your widget set after making style changes.

#### Change the Error Message Icon / Font
By default, the Add-on displays errors messages with a red x (a FontAwesome icon), with the error message itself in a red font color.  To change
this behavior modify the following styles.

To change the icon, add the following to your theme's styles.scss file:

	// Defines the FontAwesome icon that appears before each error message, default is an X 
	.err-msg-wrapper div:before {
		content: "\f00d ";
	}
	
To change the text font properties, add the following to your theme's styles.scss file:

	// Sets the font color of the error messages
	.msg-div {
		color: #ed473b !important;
	}
	
#### Change the Layout of Error Messages
By default, the Add-on shows multiple error messages for a single field in a single column table, with each message getting its own row.  If you'd like
to display the messages side by side, inline, then add the following to your styles.scss file:

	// Defines how each invididual error message is displayed in relation to the other error messages
	.err-msg-wrapper div {
		// Use inline-block to display them next to one another, ie x Error Message 1  x Error Message 2
		// display: inline-block;
		
		// Use table to display them on top of one another, like a single column table
		display: table;
		padding-top: 5px;
	}   


#### More Style Info
For more insight on the styles used by the add-on, check out the project from Git and examine the **valerrdisp/src/main/webapp/VAADIN/addons/valerrdisp/valerrdisp.scss** file.

The file is fully commented, so it should be easy to find a hook into what you'd like to customize.

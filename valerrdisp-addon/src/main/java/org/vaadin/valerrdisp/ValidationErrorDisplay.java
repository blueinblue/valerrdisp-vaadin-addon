package org.vaadin.valerrdisp;

import org.vaadin.valerrdisp.client.*;

import com.vaadin.server.*;
import com.vaadin.ui.*;

/**
 * Extends an AbstractField to display its validation error.  Placement of the error message is controlled through css class names.
 */
public class ValidationErrorDisplay extends AbstractExtension {
// Constructors
	protected ValidationErrorDisplay(AbstractField<?> field, ErrorMessagePlacement errorMessagePlacement) {
		// Set state before calling extend
		getState().errorMessagePlacement = errorMessagePlacement;
		
		// Non-public constructor to discourage direct instantiation
		extend(field);
	}

// Public Methods
	/**
	 * Display errors for the argument field with the argument placement of the error message.
	 * @param field
	 * @param errorMessagePlacement
	 * @return
	 */
	public static ValidationErrorDisplay displayFor(AbstractField<?> field, ErrorMessagePlacement errorMessagePlacement) {
		return new ValidationErrorDisplay(field, errorMessagePlacement);
	}
	
    /**
     * Display errors for the argument field.
     * 
     * Use this method if you add one of the following class names to your FormLayout: err-form-above, err-form-below, err-form-right
     * 
     * The form level placement can be override by extending a field and specifying the ErrorMessagePlacement for the field.
     * 
     * @param field
     * @return
     */
    public static ValidationErrorDisplay displayFor(AbstractField<?> field) {
        return displayFor(field, ErrorMessagePlacement.DEFAULT);
    }

// Getters & Setters
    @Override
    public ValidationErrorDisplayState getState() {
    	return (ValidationErrorDisplayState) super.getState();
    }

// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;
}



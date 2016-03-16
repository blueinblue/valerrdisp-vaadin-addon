package org.vaadin.valerrdisp.client;

import com.vaadin.shared.*;

/**
 * Shared state for ValidationErrorDisplay extension.
 */
public class ValidationErrorDisplayState extends AbstractComponentState {
// Constructors

// Public Methods

// Getters & Setters

// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Where the validation errors should appear - above, below, right, or default (allow parent container style class to dictate)
	 */
	public ErrorMessagePlacement errorMessagePlacement = ErrorMessagePlacement.DEFAULT;
}



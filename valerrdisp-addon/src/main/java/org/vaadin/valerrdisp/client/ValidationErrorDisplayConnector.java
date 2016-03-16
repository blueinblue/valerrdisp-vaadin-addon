package org.vaadin.valerrdisp.client;

import org.vaadin.valerrdisp.*;

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.*;
import com.vaadin.client.*;
import com.vaadin.client.annotations.*;
import com.vaadin.client.communication.*;
import com.vaadin.client.communication.StateChangeEvent.*;
import com.vaadin.client.extensions.*;
import com.vaadin.shared.ui.*;

@Connect(ValidationErrorDisplay.class)
public class ValidationErrorDisplayConnector extends AbstractExtensionConnector {
// Constructors
	
// Public Methods
	/*
	 * tr v-formlayout-row
	 * 		td v-formlayout-contentcall
	 * 			input v-textfield v-widget v-textfield-error
	 * 
	 */
	@Override
    protected void extend(final ServerConnector target) {
		// Set initial state
		this.errorMessagePlacement = getState().errorMessagePlacement;
		
		// Add state change handler to target field
		target.addStateChangeHandler("errorMessage", new ErrorMessageStateChangeHandler());
    }
	
	
	/**
	 * Update error message placement - only matters if init() hasn't be called yet on the ErrorMessageStateChangeHandler.  Value ignored after init() called.
	 */
	@OnStateChange("errorMessagePlacement")
	void placement() {
		this.errorMessagePlacement = getState().errorMessagePlacement;
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
	
	/**
	 * Placement of error message 
	 */
	private ErrorMessagePlacement errorMessagePlacement = null;
	
// Helper Classes
	private class ErrorMessageStateChangeHandler implements StateChangeHandler {
		// Constructors
		public ErrorMessageStateChangeHandler() {
			
		}
		
		// Public Methods
		@Override
		public void onStateChanged(StateChangeEvent stateChangeEvent) {
			Widget fieldWidget = ((ComponentConnector)stateChangeEvent.getConnector()).getWidget();
			
			if(!initialized) {
				init(fieldWidget);
			}
			
			// Remove existing errors
			errDiv.removeAllChildren();
			errDiv.addClassName("msg-div-hidden");
			
			String errMsg = ((ComponentConnector)stateChangeEvent.getConnector()).getState().errorMessage;
			
			if(errMsg != null && errMsg.trim().length() > 0) {
				DivElement div = wrapErrorsInDiv(errMsg);
				errDiv.appendChild(div);
				errDiv.removeClassName("msg-div-hidden");
			}
		}
		
		// Private Methods
		/**
		 * Creates a wrapper div and adds two children divs: 1) for the field we extended, 2) for the error messages.
		 * 
		 * Result looks like:
		 * <div class="err-wrap-container-div">
		 * 	<div class="input-div"> <INPUT .... /> </div>
		 * 	<div class="msg-div"></div>
		 * </div>
		 * 
		 * This method should only be called once per instance and must be called before any validation occurs.
		 * 
		 * @param fieldWidget Widget we extended
		 */
		private void init(Widget fieldWidget) {
			Element fieldElement = fieldWidget.getElement();
			Element parentElement = fieldElement.getParentElement();
			
			// Remove fieldWidget from parent
			fieldElement.removeFromParent();
			
			// Create a div to wrap input field
			DivElement fieldWrapper = Document.get().createDivElement();
			fieldWrapper.setClassName("input-div");
			
			fieldWrapper.appendChild(fieldElement);
			
			// Create a div to wrap error message
			DivElement errorWrapper = Document.get().createDivElement();
			errorWrapper.setClassName("msg-div");
			errorWrapper.addClassName("msg-div-hidden");
			
			// Wrap divs in container div
			DivElement containerWrapper = Document.get().createDivElement();
			containerWrapper.setClassName(getContainerClassName());
			
			containerWrapper.appendChild(fieldWrapper);
			containerWrapper.appendChild(errorWrapper);
			
			// Add the container div to the parent
			parentElement.appendChild(containerWrapper);
			
			// Store reference to errDiv so we can hide/show and update its body content.
			this.errDiv = errorWrapper;
			
			// Don't run init() again for the lifetime of this object
			initialized = true;
		}
		
		/**
		 * The argument string represents the field errors as returned by vaadin.  The format is:
		 * <div>
		 * 	<div>Error message one here.</div>
		 *  <div>Error message two here.</div>
		 * </div>
		 * 
		 * This method replaces the outer div with a new styled div and returns it as a DivElement.  Resulting content:
		 * <div err-msg-wrapper>
		 * 	<div>Error message one here.</div>
		 *  <div>Error message two here.</div>
		 * </div>
		 * 
		 * @param errContainerDiv The raw errorMessage string property sent by Vaadin
		 * @return A DivElement, never null
		 */
		private DivElement wrapErrorsInDiv(String errContainerDiv) {
			DivElement div = Document.get().createDivElement();
			div.addClassName("err-msg-wrapper");
			
			// Remove outer div by extracting children divs and setting them as the content to div
			HTML html = new HTML(errContainerDiv);
			
			String childrenDivStr = "";
			if(html.getElement() != null && html.getElement().getFirstChildElement() != null) {
				String innerHtml = html.getElement().getFirstChildElement().getInnerHTML();
				innerHtml = (innerHtml == null) ? "" : innerHtml.trim();
				
				// If there's only one error, then we need to wrap it in a DIV; if there are multiple errors, then they are already wrapped in DIVs
				boolean isMultipleChildren = innerHtml.startsWith("<div");
				
				if(isMultipleChildren) {
					childrenDivStr = innerHtml;
				}
				else {
					childrenDivStr = "<div>" + innerHtml + "</div>";
				}
			}
			
			div.setInnerHTML(childrenDivStr);
			
			return div;
		}
		
		/**
		 * Get the class name for the err-wrap-container-div - outermost div wrapping both input and error message
		 * @return A css class name
		 */
		private String getContainerClassName() {
			String className = null;
			
			switch(ValidationErrorDisplayConnector.this.errorMessagePlacement) {
				case ABOVE: {
					className = "err-wrap-container-div-above";
					break;
				}
				case BELOW: {
					className = "err-wrap-container-div-below";
					break;
				}
				case RIGHT: {
					className = "err-wrap-container-div-right";
					break;
				}
				default: {
					className = "err-wrap-container-div";
					break;
				}
			}
			
			return className;
		}
		
		// Attributes
		/**
		 * Serialization 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Has the handler been initialized?
		 */
		private boolean initialized = false;
		
		/**
		 * DIV whose inner html contains the error message.
		 */
		private DivElement errDiv = null;
		
	}
}



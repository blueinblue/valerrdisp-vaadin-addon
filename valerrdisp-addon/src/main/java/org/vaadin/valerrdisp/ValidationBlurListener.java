package org.vaadin.valerrdisp;

import com.vaadin.event.FieldEvents.*;
import com.vaadin.ui.*;

/**
 * Adds a blur listener to an AbstractField implementation.  On blur, the field's validation visible attribute is set to true.
 * 
 * In a typical scenario, you would:
 * 1. Create a TextField
 * 2. Add validators to the textfield
 * 3. Disable the field's validation
 * 4. Add an instance of this BlurListener to the field
 * ...
 * When the field is first rendered, it's validation is disabled.  When the user first leaves the field, an onBlur event occurs, which
 * triggers the logic below to enable validations.
 */
public class ValidationBlurListener implements BlurListener {
// Constructors
	/**
	 * Create a ValidationBlurListener for the argument field
	 * @param field
	 */
	public ValidationBlurListener(AbstractField<?> field) {
		this.field = field;
	}
	
// Public Methods
	@Override
	public void blur(BlurEvent event) {
		if(this.field instanceof AbstractField<?>) {
			((AbstractField<?>) this.field).setValidationVisible(true);
		}
	}
	
// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Field we are attaching to
	 */
	private final AbstractField<?> field;

}



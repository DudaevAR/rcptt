/*******************************************************************************
 * Copyright (c) 2009, 2019 Xored Software Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Xored Software Inc - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.rcptt.tesla.ecl.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Click Link</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.rcptt.tesla.ecl.model.ClickLink#getRef <em>Ref</em>}</li>
 * </ul>
 *
 * @see org.eclipse.rcptt.tesla.ecl.model.TeslaPackage#getClickLink()
 * @model annotation="http://www.eclipse.org/ecl/docs description='Clicks on a link.' returns='value of &lt;code&gt;control&lt;/code&gt; parameter' recorded='true' example='with [get-window Preferences] {\n    get-tree | select \"Ant/Editor\"\n    get-link \"See &lt;a href=\\\"org.eclipse.ui.preferencePages.GeneralTextEditor\\\"&gt;\\\'Text Editors\\\'&lt;/a&gt; for general text editor preferences and &lt;a href=\\\"org.eclipse.ui.preferencePages.ColorsAndFonts\\\"&gt;\\\'Colors and Fonts\\\'&lt;/a&gt; to configure the font.\" \n        | click-link \"org.eclipse.ui.preferencePages.GeneralTextEditor\"\n    get-button OK | click\n}'"
 * @generated
 */
public interface ClickLink extends ControlCommand {
	/**
	 * Returns the value of the '<em><b>Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' attribute.
	 * @see #setRef(String)
	 * @see org.eclipse.rcptt.tesla.ecl.model.TeslaPackage#getClickLink_Ref()
	 * @model
	 * @generated
	 */
	String getRef();

	/**
	 * Sets the value of the '{@link org.eclipse.rcptt.tesla.ecl.model.ClickLink#getRef <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' attribute.
	 * @see #getRef()
	 * @generated
	 */
	void setRef(String value);

} // ClickLink

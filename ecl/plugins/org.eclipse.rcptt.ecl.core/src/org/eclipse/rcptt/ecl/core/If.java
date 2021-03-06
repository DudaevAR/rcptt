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
package org.eclipse.rcptt.ecl.core;

import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>If</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.rcptt.ecl.core.If#getCondition <em>Condition</em>}</li>
 *   <li>{@link org.eclipse.rcptt.ecl.core.If#getThen <em>Then</em>}</li>
 *   <li>{@link org.eclipse.rcptt.ecl.core.If#getElse <em>Else</em>}</li>
 * </ul>
 *
 * @see org.eclipse.rcptt.ecl.core.CorePackage#getIf()
 * @model annotation="http://www.eclipse.org/ecl/docs description='Based on a condition corresponds either command either from &lt;code&gt;then&lt;/code&gt; or &lt;code&gt;else&lt;/code&gt; branch. ' returns='Output of executed branch' example='if [get-property caption -raw | not-eq \"Test\" ] {\r\n\tselect-item\r\n\tget-menu Delete | click\r\n\tget-window \"Delete Resources\" | get-button OK | click\r\n}'"
 * @generated
 */
public interface If extends Command {
	/**
	 * Returns the value of the '<em><b>Condition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Condition</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Condition</em>' reference.
	 * @see #setCondition(EObject)
	 * @see org.eclipse.rcptt.ecl.core.CorePackage#getIf_Condition()
	 * @model annotation="http://www.eclipse.org/ecl/docs description='Accepted values are boolean, string (&lt;code&gt;true&lt;/code&gt; when case-insensitive equals to &lt;code&gt;true&lt;/code&gt;, &lt;code&gt;false&lt;/code&gt; otherwise) and number (&lt;code&gt;0&lt;/code&gt; for &lt;code&gt;false&lt;/code&gt;, &lt;code&gt;true&lt;/code&gt; otherwise). '"
	 * @generated
	 */
	EObject getCondition();

	/**
	 * Sets the value of the '{@link org.eclipse.rcptt.ecl.core.If#getCondition <em>Condition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Condition</em>' reference.
	 * @see #getCondition()
	 * @generated
	 */
	void setCondition(EObject value);

	/**
	 * Returns the value of the '<em><b>Then</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Then</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Then</em>' containment reference.
	 * @see #setThen(Command)
	 * @see org.eclipse.rcptt.ecl.core.CorePackage#getIf_Then()
	 * @model containment="true"
	 *        annotation="http://www.eclipse.org/ecl/docs description='Branch being executed on &lt;code&gt;true&lt;/code&gt; input. Can be omitted.'"
	 * @generated
	 */
	Command getThen();

	/**
	 * Sets the value of the '{@link org.eclipse.rcptt.ecl.core.If#getThen <em>Then</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Then</em>' containment reference.
	 * @see #getThen()
	 * @generated
	 */
	void setThen(Command value);

	/**
	 * Returns the value of the '<em><b>Else</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Else</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Else</em>' containment reference.
	 * @see #setElse(Command)
	 * @see org.eclipse.rcptt.ecl.core.CorePackage#getIf_Else()
	 * @model containment="true"
	 *        annotation="http://www.eclipse.org/ecl/docs description='Branch being executed on &lt;code&gt;false&lt;/code&gt; input. Can be omitted.'"
	 * @generated
	 */
	Command getElse();

	/**
	 * Sets the value of the '{@link org.eclipse.rcptt.ecl.core.If#getElse <em>Else</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Else</em>' containment reference.
	 * @see #getElse()
	 * @generated
	 */
	void setElse(Command value);

} // If

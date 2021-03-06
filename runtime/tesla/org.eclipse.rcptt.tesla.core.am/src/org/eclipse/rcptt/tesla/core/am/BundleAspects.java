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
package org.eclipse.rcptt.tesla.core.am;

import java.util.List;

public class BundleAspects {

	public BundleAspects(String bundle, List<String> aspects) {
		this.bundle = bundle;
		this.aspects = aspects;
	}

	public List<String> getAspects() {
		return aspects;
	}

	public String getBundle() {
		return bundle;
	}

	private String bundle;
	private List<String> aspects;

}

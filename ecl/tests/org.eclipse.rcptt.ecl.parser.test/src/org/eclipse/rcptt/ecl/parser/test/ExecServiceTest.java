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
package org.eclipse.rcptt.ecl.parser.test;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

public class ExecServiceTest extends TestWithSession {
	@Test
	public void letExpression() throws CoreException {
		runScript("let [val a 1] {\n\t$a | eq 1 | assert-true\n\t$a | eq 1 | assert-true}");
	}

}

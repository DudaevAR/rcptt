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
package org.eclipse.rcptt.ecl.operations.internal.commands;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.rcptt.ecl.core.Command;
import org.eclipse.rcptt.ecl.operations.Or;
import org.eclipse.rcptt.ecl.operations.internal.OpUtils;
import org.eclipse.rcptt.ecl.runtime.BoxedValues;
import org.eclipse.rcptt.ecl.runtime.ICommandService;
import org.eclipse.rcptt.ecl.runtime.IProcess;

public class OrService implements ICommandService {

	public IStatus service(Command command, IProcess context)
			throws InterruptedException, CoreException {
		Or or = (Or) command;

		boolean result = false;
		for (EObject a : or.getArgs())
			result |= OpUtils.asBoolean(a);

		context.getOutput().write(BoxedValues.box(result));

		context.getOutput().close(Status.OK_STATUS);
		return Status.OK_STATUS;
	}

}

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
package org.eclipse.rcptt.ecl.internal.commands;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.rcptt.ecl.core.Case;
import org.eclipse.rcptt.ecl.core.Command;
import org.eclipse.rcptt.ecl.core.Switch;
import org.eclipse.rcptt.ecl.runtime.ICommandService;
import org.eclipse.rcptt.ecl.runtime.IProcess;
import org.eclipse.rcptt.ecl.runtime.ISession;

public class SwitchService implements ICommandService {

	@Override
	public IStatus service(Command command, IProcess context) throws InterruptedException, CoreException {
		if (!(command instanceof Switch))
			return Status.CANCEL_STATUS;
		Switch switchCmd = (Switch) command;
		Command targetCmd = null;
		ISession session = context.getSession();

		for (Case caseCmd : switchCmd.getItems()) {
			if (EcoreUtil.equals(caseCmd.getCondition(), switchCmd.getInput())) {
				// Found matching case block.
				targetCmd = caseCmd.getThen();
				break;
			}
		}

		// Didn't find matching case, set it to the default block.
		if (targetCmd == null) {
			targetCmd = switchCmd.getDefault();
		}

		if (targetCmd != null) {
			return session.execute(targetCmd, context.getInput(), context.getOutput()).waitFor();
		}

		return Status.OK_STATUS;
	}

}

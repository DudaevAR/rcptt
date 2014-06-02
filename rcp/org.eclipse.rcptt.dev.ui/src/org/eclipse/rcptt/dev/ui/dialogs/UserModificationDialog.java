/*******************************************************************************
 * Copyright (c) 2009, 2014 Xored Software Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Xored Software Inc - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.rcptt.dev.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import org.eclipse.rcptt.core.scenario.Scenario;
import org.eclipse.rcptt.internal.ui.Q7UIPlugin;
import org.eclipse.rcptt.ui.preferences.IPreferenceKeys;

public enum UserModificationDialog {
	INSTANCE;

	private static IPreferenceStore preferenceStore = Q7UIPlugin.getDefault()
			.getPreferenceStore();

	public boolean open(final Scenario scenario) {
		String show = preferenceStore
				.getString(IPreferenceKeys.DISCARD_USER_CHANGES_PROMT);
		if (MessageDialogWithToggle.PROMPT.equals(show)) {
			final boolean[] answer = new boolean[1];
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					IWorkbenchWindow window = Q7UIPlugin.getDefault()
							.getWorkbench().getActiveWorkbenchWindow();
					MessageDialogWithToggle dialog = MessageDialogWithToggle
							.openYesNoQuestion(window.getShell(), "Error",
									"Test case \"" + scenario.getName()
											+ "\" was changed and "
											+ "doesn't match captured data.\n"
											+ "Would you like to recover "
											+ "ECL script from captured data?",
									null, false, preferenceStore,
									IPreferenceKeys.DISCARD_USER_CHANGES_PROMT);
					int returnCode = dialog.getReturnCode();
					answer[0] = returnCode == IDialogConstants.YES_ID;
				}
			});
			return answer[0];
		}
		return MessageDialogWithToggle.ALWAYS.equals(show);
	}
}
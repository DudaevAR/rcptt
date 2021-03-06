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
package org.eclipse.rcptt.ui.report;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.NewFolderDialog;
import org.eclipse.ui.views.navigator.ResourceComparator;

import org.eclipse.rcptt.internal.ui.Messages;
import org.eclipse.rcptt.ui.commons.SWTFactory;

public class FolderSelectionDialog extends ElementTreeSelectionDialog implements
		ISelectionChangedListener {

	private Button fNewFolderButton;
	private IContainer fSelectedContainer;

	public FolderSelectionDialog(Shell parent, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
		setComparator(new ResourceComparator(ResourceComparator.NAME));
	}

	/*
	 * @see org.eclipse.jface.window.Window#configureShell(Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite result = (Composite) super.createDialogArea(parent);

		getTreeViewer().addSelectionChangedListener(this);
		getTreeViewer().expandToLevel(2);
		fNewFolderButton = new Button(result, SWT.PUSH);
		fNewFolderButton.setText(Messages.FolderSelectionDialog_NewFolderButton);
		fNewFolderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				newFolderButtonPressed();
			}
		});
		fNewFolderButton.setFont(parent.getFont());
		fNewFolderButton.setLayoutData(new GridData());
		SWTFactory.setButtonDimensionHint(fNewFolderButton);

		applyDialogFont(result);
		return result;
	}

	private void updateNewFolderButtonState() {
		IStructuredSelection selection = (IStructuredSelection) getTreeViewer()
				.getSelection();
		fSelectedContainer = null;
		if (selection.size() == 1) {
			Object first = selection.getFirstElement();
			if (first instanceof IContainer) {
				fSelectedContainer = (IContainer) first;
			}
		}
		fNewFolderButton.setEnabled(fSelectedContainer != null);
	}

	protected void newFolderButtonPressed() {
		NewFolderDialog dialog = new NewFolderDialog(getShell(),
				fSelectedContainer);
		if (dialog.open() == Window.OK) {
			TreeViewer treeViewer = getTreeViewer();
			treeViewer.refresh(fSelectedContainer);
			Object createdFolder;
			if (dialog.getResult() != null) {
				createdFolder = dialog.getResult()[0];
				treeViewer.reveal(createdFolder);
				treeViewer.setSelection(new StructuredSelection(createdFolder));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		updateNewFolderButtonState();
	}

}

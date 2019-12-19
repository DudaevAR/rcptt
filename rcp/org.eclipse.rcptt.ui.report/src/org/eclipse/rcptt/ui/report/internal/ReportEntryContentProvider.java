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
package org.eclipse.rcptt.ui.report.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rcptt.reporting.util.Q7ReportIterator;
import org.eclipse.rcptt.reporting.util.ReportEntry;
import org.eclipse.rcptt.sherlock.core.model.sherlock.report.Report;

import com.google.common.base.Preconditions;

class ReportEntryContentProvider implements IStructuredContentProvider {
	public void dispose() {
		Job.getJobManager().cancel(this);
	}

	private List<ReportEntry> entries = Collections.emptyList();
	private Viewer viewer = null;

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		Job.getJobManager().cancel(this);
		if (newInput instanceof Q7ReportIterator)
			new UpdateJob((Q7ReportIterator) newInput).schedule();
	}

	public Object[] getElements(Object inputElement) {
		return entries.toArray();
	}

	private class UpdateJob extends Job {
		private final Iterable<Report> reports;

		@Override
		public boolean belongsTo(Object family) {
			return ReportEntryContentProvider.this == family;
		}

		public UpdateJob(Iterable<Report> reports) {
			super("Building test list for report");
			this.reports = reports;
			Preconditions.checkNotNull(reports);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			final List<ReportEntry> entries = new ArrayList<ReportEntry>();
			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;
			synchronized (reports) {
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
				Iterator<Report> iterator = reports.iterator();
				while (iterator.hasNext()) {
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
					Report next = iterator.next();
					if (next == null) {
						break;
					}
					{
						entries.add(ReportEntry.create(next));
					}
				}
			}
			ReportEntryContentProvider.this.entries = entries;
			viewer.getControl().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!viewer.getControl().isDisposed())
						viewer.refresh();
				}
			});
			return Status.OK_STATUS;
		}

	}
}
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
package org.eclipse.rcptt.internal.core.model.cache;

public class ToStringSorter {
	Object[] sortedObjects;
	String[] sortedStrings;

	public boolean compare(String stringOne, String stringTwo) {
		return stringOne.compareTo(stringTwo) < 0;
	}

	private void quickSort(int left, int right) {
		int originalLeft = left;
		int originalRight = right;
		int midIndex = (left + right) / 2;
		String midToString = this.sortedStrings[midIndex];

		do {
			while (compare(this.sortedStrings[left], midToString))
				left++;
			while (compare(midToString, this.sortedStrings[right]))
				right--;
			if (left <= right) {
				Object tmp = this.sortedObjects[left];
				this.sortedObjects[left] = this.sortedObjects[right];
				this.sortedObjects[right] = tmp;
				String tmpToString = this.sortedStrings[left];
				this.sortedStrings[left] = this.sortedStrings[right];
				this.sortedStrings[right] = tmpToString;
				left++;
				right--;
			}
		} while (left <= right);

		if (originalLeft < right)
			quickSort(originalLeft, right);
		if (left < originalRight)
			quickSort(left, originalRight);
	}

	public void sort(Object[] unSortedObjects, String[] unsortedStrings) {
		int size = unSortedObjects.length;
		this.sortedObjects = new Object[size];
		this.sortedStrings = new String[size];

		// copy the array so can return a new sorted collection
		System.arraycopy(unSortedObjects, 0, this.sortedObjects, 0, size);
		System.arraycopy(unsortedStrings, 0, this.sortedStrings, 0, size);
		if (size > 1)
			quickSort(0, size - 1);
	}
}

/*******************************************************************************
 * Copyright (c) 2009, 2026 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.report.internal.html.resources;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jacoco.core.analysis.ICoverageNode.ElementType;
import org.jacoco.report.MemoryMultiReportOutput;
import org.jacoco.report.internal.ReportOutputFolder;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link Resources}.
 */
public class ResourcesTest {

	private MemoryMultiReportOutput output;

	private ReportOutputFolder root;

	private Resources resources;

	@Before
	public void setup() {
		output = new MemoryMultiReportOutput();
		root = new ReportOutputFolder(output);
		resources = new Resources(root);
	}

	@Test
	public void testGetLink() {
		ReportOutputFolder base = root.subFolder("f1").subFolder("f2");
		assertEquals("../../jacoco-resources/test.png",
				resources.getLink(base, "test.png"));

	}

	@Test
	public void testCopyResources() throws IOException {
		resources.copyResources();
		output.assertNoFile("jacoco-resources/report.gif");
		output.assertNoFile("jacoco-resources/redbar.gif");
		output.assertNoFile("jacoco-resources/branchfc.gif");
		output.assertFile("jacoco-resources/tkur.svg");
		output.assertFile("jacoco-resources/lucide-LICENSE.txt");
		output.assertFile("jacoco-resources/branchfc.svg");
		output.assertFile("jacoco-resources/branchnc.svg");
		output.assertFile("jacoco-resources/branchpc.svg");
		output.assertFile("jacoco-resources/bundle.svg");
		output.assertFile("jacoco-resources/class.svg");
		output.assertFile("jacoco-resources/down.svg");
		output.assertFile("jacoco-resources/greenbar.svg");
		output.assertFile("jacoco-resources/group.svg");
		output.assertFile("jacoco-resources/method.svg");
		output.assertFile("jacoco-resources/package.svg");
		output.assertFile("jacoco-resources/prettify.css");
		output.assertFile("jacoco-resources/prettify.js");
		output.assertFile("jacoco-resources/redbar.svg");
		output.assertFile("jacoco-resources/report.css");
		output.assertFile("jacoco-resources/report.svg");
		output.assertFile("jacoco-resources/class.svg");
		output.assertFile("jacoco-resources/sort.js");
		output.assertFile("jacoco-resources/source.svg");
		output.assertFile("jacoco-resources/up.svg");
	}

	@Test
	public void testGetElementStyle() {
		assertEquals("el_group", Resources.getElementStyle(ElementType.GROUP));
		assertEquals("el_bundle",
				Resources.getElementStyle(ElementType.BUNDLE));
		assertEquals("el_package",
				Resources.getElementStyle(ElementType.PACKAGE));
		assertEquals("el_source",
				Resources.getElementStyle(ElementType.SOURCEFILE));
		assertEquals("el_class", Resources.getElementStyle(ElementType.CLASS));
		assertEquals("el_method",
				Resources.getElementStyle(ElementType.METHOD));
	}

}

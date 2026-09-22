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
package org.jacoco.report.internal.html.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jacoco.report.internal.ReportOutputFolder;
import org.jacoco.report.internal.html.HTMLElement;
import org.jacoco.report.internal.html.HTMLSupport;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Unit tests for {@link ReportPage}.
 */
public class ReportPageTest extends PageTestBase {

	private ReportPage rootpage;

	private ReportPage page;

	private class TestReportPage extends ReportPage {

		private final String label;
		private final String style;

		protected TestReportPage(String label, String style,
				ReportPage parent) {
			super(parent, rootFolder, ReportPageTest.this.context);
			this.label = label;
			this.style = style;
		}

		@Override
		protected void content(HTMLElement body) throws IOException {
			body.div("testcontent").text("Hello Test");
		}

		@Override
		protected String getFileName() {
			return label + ".html";
		}

		public String getLinkLabel() {
			return label;
		}

		public String getLinkStyle() {
			return style;
		}

	}

	@Before
	@Override
	public void setup() throws Exception {
		super.setup();
		rootpage = new TestReportPage("Report", "el_report", null);
		page = new TestReportPage("Test", "el_group", rootpage);
	}

	@Test
	public void testIsRootPage1() {
		assertFalse(page.isRootPage());
	}

	@Test
	public void testIsRootPage2() {
		assertTrue(rootpage.isRootPage());
	}

	@Test
	public void testGetLink() throws IOException {
		ReportOutputFolder base = rootFolder.subFolder("here");
		assertEquals("../Test.html", page.getLink(base));
	}

	@Test
	public void testPageContent() throws Exception {
		page.render();
		final HTMLSupport support = new HTMLSupport();
		final Document doc = support.parse(output.getFile("Test.html"));

		// language
		assertEquals("ru", support.findStr(doc, "/html/@lang"));

		// style sheet
		assertEquals("jacoco-resources/report.css", support.findStr(doc,
				"/html/head/link[@rel='stylesheet']/@href"));

		// bread crumb
		assertEquals("Report", support.findStr(doc,
				"/html/body/div[@class='breadcrumb']/a[1]/text()"));
		assertEquals("Report.html", support.findStr(doc,
				"/html/body/div[@class='breadcrumb']/a[1]/@href"));
		assertEquals("el_report", support.findStr(doc,
				"/html/body/div[@class='breadcrumb']/a[1]/@class"));
		assertEquals("Test", support.findStr(doc,
				"/html/body/div[@class='breadcrumb']/span[2]/text()"));
		assertEquals("el_group", support.findStr(doc,
				"/html/body/div[@class='breadcrumb']/span[2]/@class"));

		// Header
		assertEquals("Сформирован: 22.09.2026 12:30:00 +0300",
				support.findStr(doc, "//span[@class='tkur-created']/text()"));
		assertEquals("jacoco-resources/report.svg", support.findStr(doc,
				"/html/head/link[@rel='shortcut icon']/@href"));
		assertEquals("image/svg+xml", support.findStr(doc,
				"/html/head/link[@rel='shortcut icon']/@type"));
		assertEquals("jacoco-resources/tkur.svg", support.findStr(doc,
				"/html/body/div[@class='tkur-header']/img/@src"));
		assertEquals("Логотип ТЕХКОНСУР", support.findStr(doc,
				"/html/body/div[@class='tkur-header']/img/@alt"));
		assertEquals("Test", support.findStr(doc, "/html/body/h1/text()"));
		assertEquals("Испытательная лаборатория ООО \"ТЕХКОНСУР\"",
				support.findStr(doc,
						"/html/body/div[@class='tkur-header']/span/text()"));
		assertEquals("tkur_jacoco 0.8.15-tkur.4", support.findStr(doc,
				"//span[@class='tkur-product-name']/text()"));
		assertEquals("width=device-width, initial-scale=1", support.findStr(doc,
				"/html/head/meta[@name='viewport']/@content"));
		assertEquals("Покрыто", support.findStr(doc,
				"//div[@class='tkur-legend']/span[@class='tkur-covered']/text()"));
		assertEquals("Частично", support.findStr(doc,
				"//div[@class='tkur-legend']/span[@class='tkur-partial']/text()"));
		assertEquals("Не покрыто", support.findStr(doc,
				"//div[@class='tkur-legend']/span[@class='tkur-missed']/text()"));

		// Content
		assertEquals("Hello Test", support.findStr(doc,
				"/html/body/div[@class='testcontent']/text()"));

		// Footer
		assertEquals("CustomFooter",
				support.findStr(doc, "/html/body/div[@class='footer']/text()"));
		assertEquals(
				"Испытательная лаборатория ООО \"ТЕХКОНСУР\" · tkur_jacoco",
				support.findStr(doc,
						"//div[@class='footer']/span[@class='tkur-owner']/text()"));
	}

}

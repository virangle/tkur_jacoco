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

import java.io.IOException;

import org.jacoco.core.JaCoCo;
import org.jacoco.report.internal.ReportOutputFolder;
import org.jacoco.report.internal.html.HTMLElement;
import org.jacoco.report.internal.html.IHTMLReportContext;
import org.jacoco.report.internal.html.ILinkable;
import org.jacoco.report.internal.html.resources.Resources;
import org.jacoco.report.internal.html.resources.Styles;

/**
 * Base class for HTML page generators. It renders the page skeleton with the
 * breadcrumb, the title and the footer. Every report page is part of a
 * hierarchy and has a parent page (except the root page).
 */
public abstract class ReportPage implements ILinkable {

	private final ReportPage parent;

	/** output folder for this node */
	protected final ReportOutputFolder folder;

	/** context for this report */
	protected final IHTMLReportContext context;

	/**
	 * Creates a new report page.
	 *
	 * @param parent
	 *            optional hierarchical parent
	 * @param folder
	 *            base folder to create this report in
	 * @param context
	 *            settings context
	 */
	protected ReportPage(final ReportPage parent,
			final ReportOutputFolder folder, final IHTMLReportContext context) {
		this.parent = parent;
		this.context = context;
		this.folder = folder;
	}

	/**
	 * Checks whether this is the root page of the report.
	 *
	 * @return <code>true</code> if this is the root page
	 */
	protected final boolean isRootPage() {
		return parent == null;
	}

	/**
	 * Renders this page's content and optionally additional pages. This method
	 * must be called at most once.
	 *
	 * @throws IOException
	 *             if the page can't be written
	 */
	public void render() throws IOException {
		final HTMLElement html = new HTMLElement(
				folder.createFile(getFileName()), context.getOutputEncoding());
		html.attr("lang", "ru");
		head(html.head());
		body(html.body());
		html.close();
	}

	/**
	 * Creates the elements within the head element.
	 *
	 * @param head
	 *            head tag of the page
	 * @throws IOException
	 *             in case of IO problems with the report writer
	 */
	protected void head(final HTMLElement head) throws IOException {
		head.meta("Content-Type", "text/html;charset=UTF-8");
		final HTMLElement viewport = head.element("meta");
		viewport.attr("name", "viewport");
		viewport.attr("content", "width=device-width, initial-scale=1");
		head.link("stylesheet",
				context.getResources().getLink(folder, Resources.STYLESHEET),
				"text/css");
		head.link("shortcut icon",
				context.getResources().getLink(folder, "report.gif"),
				"image/gif");
		head.title().text(getLinkLabel());
	}

	private void body(final HTMLElement body) throws IOException {
		body.attr("onload", getOnload());
		final HTMLElement brand = body.div("tkur-header");
		final HTMLElement logo = brand.element("img");
		logo.attr("class", "tkur-logo");
		logo.attr("src", context.getResources().getLink(folder, "tkur.svg"));
		logo.attr("alt", "Логотип ТЕХКОНСУР");
		brand.span("tkur-wordmark")
				.text("Испытательная лаборатория ООО \"ТЕХКОНСУР\"");
		final HTMLElement product = brand.div("tkur-product");
		product.span("tkur-product-name").text("tkur_jacoco / 0.8.15");
		product.span("tkur-product-description").text("ОТЧЁТ О ПОКРЫТИИ КОДА");
		final HTMLElement navigation = body.div(Styles.BREADCRUMB);
		navigation.attr("id", "breadcrumb");
		infoLinks(navigation.span(Styles.INFO));
		breadcrumb(navigation, folder);
		body.h1().text(getLinkLabel());
		final HTMLElement legend = body.div("tkur-legend");
		legend.span("tkur-covered").text("Покрыто");
		legend.span("tkur-partial").text("Частично");
		legend.span("tkur-missed").text("Не покрыто");
		content(body);
		footer(body);
	}

	/**
	 * Returns the onload handler for this page.
	 *
	 * @return handler or <code>null</code>
	 */
	protected String getOnload() {
		return null;
	}

	/**
	 * Inserts additional links on the top right corner.
	 *
	 * @param span
	 *            parent element
	 * @throws IOException
	 *             in case of IO problems with the report writer
	 */
	protected void infoLinks(final HTMLElement span) throws IOException {
		span.a(context.getSessionsPage(), folder);
	}

	private void breadcrumb(final HTMLElement div,
			final ReportOutputFolder base) throws IOException {
		breadcrumbParent(parent, div, base);
		div.span(getLinkStyle()).text(getLinkLabel());
	}

	private static void breadcrumbParent(final ReportPage page,
			final HTMLElement div, final ReportOutputFolder base)
			throws IOException {
		if (page != null) {
			breadcrumbParent(page.parent, div, base);
			div.a(page, base);
			div.text(" > ");
		}
	}

	private void footer(final HTMLElement body) throws IOException {
		final HTMLElement footer = body.div(Styles.FOOTER);
		final HTMLElement versioninfo = footer.span(Styles.RIGHT);
		versioninfo.text("Создано с помощью ");
		versioninfo.a(JaCoCo.HOMEURL).text("JaCoCo");
		versioninfo.text(" ");
		versioninfo.text(JaCoCo.VERSION);
		footer.span("tkur-owner").text(
				"Испытательная лаборатория ООО \"ТЕХКОНСУР\" · tkur_jacoco");
		footer.text(context.getFooterText());
	}

	/**
	 * Specifies the local file name of this page.
	 *
	 * @return local file name
	 */
	protected abstract String getFileName();

	/**
	 * Creates the actual content of the page.
	 *
	 * @param body
	 *            body tag of the page
	 * @throws IOException
	 *             in case of IO problems with the report writer
	 */
	protected abstract void content(final HTMLElement body) throws IOException;

	// === ILinkable ===

	public final String getLink(final ReportOutputFolder base) {
		return folder.getLink(base, getFileName());
	}

}

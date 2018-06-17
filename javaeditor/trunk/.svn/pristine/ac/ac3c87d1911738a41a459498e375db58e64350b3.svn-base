/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.gui.dialog.help;

import java.awt.Component;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import net.sf.gridarta.utils.CommonConstants;
import org.jetbrains.annotations.Nullable;

/**
 * Pane for displaying HTML.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
class HtmlPane extends JScrollPane implements HyperlinkListener {

    /**
     * The Logger for printing log messages.
     */
    private static final Logger log = Logger.getLogger("HtmlPane.class");

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The JEditorPane that displays the html page.
     */
    private final JEditorPane html;

    /**
     * Constructor to load the html-file &lt;fileName&gt; and display it's
     * contents in this HtmlPane.
     * @param fileName the name of the HTML-file
     */
    HtmlPane(final String fileName) {
        try {
            // first looking for the html file in extracted form
            final File file = new File(CommonConstants.HELP_DIR, fileName);
            if (file.exists()) {
                // file exists in expected directory
                final String s = "file:" + file.getAbsolutePath();
                html = new JEditorPane(s);
            } else {
                // file missing, so let's look if we can get it from the jar
                final URL url1 = ClassLoader.getSystemResource(CommonConstants.HELP_DIR.replace('\\', '/') + '/' + fileName);

                if (url1 != null) {
                    html = new JEditorPane(url1);
                } else {
                    // let's try it again without first directory
                    log.info("trying: HelpFiles/" + fileName);
                    final URL url2 = ClassLoader.getSystemResource("HelpFiles/" + fileName);
                    if (url2 != null) {
                        html = new JEditorPane(url2);
                    } else {
                        log.info("Failed to open help file '" + fileName + "'!");
                        throw new RuntimeException(); // FIXME
                    }
                }
            }

            html.setEditable(false);
            html.addHyperlinkListener(this);
            final JViewport vp = getViewport();

            // under windows, the content of the panel get destroyed after scrolling
            // this will avoid this problem!
            // but it will make the scrolling slower
            // so test this!
            // FIXME: This problem should be fixed in the latest versions of the JRE.
            getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
            vp.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

            vp.add(html);
            setFocusable(true);
            requestFocus();
            setAutoscrolls(true);
        } catch (final NullPointerException e) {
            // failed to open the html file
            throw new RuntimeException(e); // FIXME
        } catch (final MalformedURLException e) {
            log.log(Level.WARNING, "Malformed URL: %s", e);
            throw new RuntimeException(e); // FIXME
        } catch (final IOException e) {
            log.log(Level.WARNING, "IOException: %s", e);
            throw new RuntimeException(e); // FIXME
        }
    }

    /**
     * Constructor to load the html-file &lt;fileName&gt; and display it's
     * contents in this HtmlPane.
     * @param type mime-type of the given text (e.g. "text/html")
     * @param text text to display (can be html-text for example)
     */
    HtmlPane(final String type, final String text) {
        // open new JEditorPane
        html = new JEditorPane(type, text);
        html.setEditable(false);
        html.addHyperlinkListener(this);
        final JViewport vp = getViewport();

        // under windows, the content of the panel get destroyed after scrolling
        // this will avoid this problem!
        // but it will make the scrolling slower
        // so test this!
        // XXX The main issue probably is extending JScrollPane (Cher)
        getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
        vp.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        vp.add(html);
        //vp.setView(html);
        //vp.setViewPosition(new Point(0, 0));

        setAutoscrolls(true);
    }

    /**
     * Notification of a change relative to a hyperlink.
     * @param e occurred <code>HyperlinkEvent</code>
     */
    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            linkActivated(e.getURL());
        }
    }

    /**
     * Follows the reference in an link.  The given url is the requested
     * reference. By default this calls <a href="#setPage">setPage</a>, and if
     * an exception is thrown the original previous document is restored and a
     * beep sounded.  If an attempt was made to follow a link, but it
     * represented a malformed url, this method will be called with a null
     * argument.
     * @param u the URL to follow
     */
    private void linkActivated(final URL u) {
        final Cursor cursor = html.getCursor();
        final Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        html.setCursor(waitCursor);
        SwingUtilities.invokeLater(new PageLoader(u, cursor));
    }

    /**
     * Synchronous page loader, loads a page and handles the cursor.
     */
    private class PageLoader implements Runnable {

        /**
         * URL to load.
         */
        @Nullable
        private URL url;

        /**
         * Original cursor that should be restored once the document is loaded.
         */
        private final Cursor cursor;

        /**
         * Create a PageLoader.
         * @param url the URL to load
         * @param cursor the cursor to restore once the document is loaded
         */
        private PageLoader(final URL url, final Cursor cursor) {
            this.url = url;
            this.cursor = cursor;
        }

        @Override
        public void run() {
            if (url == null) {
                // restore the original cursor
                html.setCursor(cursor);

                // PENDING(prinz) remove this hack when
                // automatic validation is activated.
                final Component parent = html.getParent();
                parent.repaint();
            } else {
                final Document doc = html.getDocument();
                try {
                    html.setPage(url);
                } catch (final IOException ioe) {
                    html.setDocument(doc);
                    getToolkit().beep();
                    // TODO not just beep but display an error message as well.
                } finally {
                    // schedule the cursor to revert after
                    // the paint has happened.
                    url = null;
                    SwingUtilities.invokeLater(this);
                }
            }
        }

    }

}

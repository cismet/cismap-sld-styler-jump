/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jump.sld.editor;

import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import java.io.StringReader;

import javax.swing.JTextArea;

import de.cismet.cids.search.QuerySearch;
import de.cismet.cids.search.QuerySearchMethod;

import de.cismet.cismap.commons.featureservice.AbstractFeatureService;
import de.cismet.cismap.commons.featureservice.FeatureServiceUtilities;
import de.cismet.cismap.commons.featureservice.WebFeatureService;
import de.cismet.cismap.commons.wfs.WFSFacade;
import de.cismet.cismap.commons.wfs.capabilities.WFSCapabilities;

import de.cismet.connectioncontext.ConnectionContext;

/**
 * Uses the query search to create a query defination for feature services.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class QuerySearchDialog extends javax.swing.JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(QuerySearchDialog.class);

    //~ Instance fields --------------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.cismet.cids.search.QuerySearch querySearch1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form QuerySearchDialog.
     *
     * @param  parent    DOCUMENT ME!
     * @param  modal     DOCUMENT ME!
     * @param  service   DOCUMENT ME!
     * @param  textArea  DOCUMENT ME!
     */
    public QuerySearchDialog(final java.awt.Frame parent,
            final boolean modal,
            final AbstractFeatureService service,
            final JTextArea textArea) {
        super(parent, modal);

        querySearch1 = new QuerySearch(
                null,
                new String[] {},
                new AbstractFeatureService[] { service },
                new QuerySearchMethod[] { new BuildQuerySearchMethod(textArea) });
        querySearch1.initWithConnectionContext(ConnectionContext.createDummy());
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final java.awt.GridBagConstraints gridBagConstraints;

        querySearch1 = querySearch1;

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(QuerySearchDialog.class, "QuerySearchDialog.title")); // NOI18N
        setPreferredSize(new java.awt.Dimension(430, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(querySearch1, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Creates a query for the given service and put it into the given JTextArea.
     *
     * @version  $Revision$, $Date$
     */
    private class BuildQuerySearchMethod implements QuerySearchMethod {

        //~ Instance fields ----------------------------------------------------

        private QuerySearch querySearch;
        private final JTextArea textArea;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new BuildQuerySearchMethod object.
         *
         * @param  textArea  DOCUMENT ME!
         */
        public BuildQuerySearchMethod(final JTextArea textArea) {
            this.textArea = textArea;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void setQuerySearch(final QuerySearch search) {
            this.querySearch = search;
        }

        @Override
        public void actionPerformed(final Object layer, final String query) {
            if ((query != null) && !query.isEmpty()) {
                if (layer instanceof WebFeatureService) {
                    final WebFeatureService wfs = (WebFeatureService)layer;
                    try {
                        final Element e = (Element)wfs.getQueryElement().clone();
                        final Element queryElement = e.getChild(
                                "Query",
                                Namespace.getNamespace("wfs", "http://www.opengis.net/wfs"));
                        queryElement.removeChild("Filter", Namespace.getNamespace("ogc", "http://www.opengis.net/ogc"));
                        final Element filterElement = new Element(
                                "Filter",
                                Namespace.getNamespace("ogc", "http://www.opengis.net/ogc"));
                        final SAXBuilder builder = new SAXBuilder();
                        final String geoQuery = "<And>" + query
                                    + "<BBOX><PropertyName>app:the_geom</PropertyName><cismapBoundingBoxAsGmlPlaceholder /></BBOX></And>";
                        final Document d = builder.build(new StringReader(geoQuery));
                        filterElement.addContent((Element)d.getRootElement().clone());
                        queryElement.addContent(0, filterElement);
                        textArea.setText(FeatureServiceUtilities.elementToString(e));
                    } catch (Exception ex) {
                        LOG.error("Error preparing query", ex);
                    }
                } else if (layer instanceof AbstractFeatureService) {
                    textArea.setText(query);
                }
            } else {
                if (layer instanceof WebFeatureService) {
                    final WebFeatureService wfs = (WebFeatureService)layer;
                    final WFSCapabilities caps = wfs.getFeature().getWFSCapabilities();
                    final WFSFacade facade = caps.getServiceFacade();
                    final Element defaultQueryElement = facade.getGetFeatureQuery(wfs.getFeature());
                    final String defaultQuery = FeatureServiceUtilities.elementToString(defaultQueryElement);
                    textArea.setText(defaultQuery);
                } else {
                    textArea.setText("");
                }
            }

            setVisible(false);
        }
    }
}

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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.AssertionFailedException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.ColorThemingStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.ColorThemingStylePanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.Style;
import com.vividsolutions.jump.workbench.ui.renderer.style.VertexStyle;
import com.vividsolutions.jump.workbench.ui.style.DecorationStylePanel;
import com.vividsolutions.jump.workbench.ui.style.StylePanel;

import de.latlon.deejump.plugin.style.DeeRenderingStylePanel;
import de.latlon.deejump.plugin.style.LayerStyle2SLDPlugIn;

import org.deegree.datatypes.Types;
import org.deegree.model.feature.FeatureProperty;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

import org.openjump.util.SLDImporter;
import org.openjump.util.XPathUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import java.net.URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import de.cismet.cismap.commons.features.FeatureServiceFeature;
import de.cismet.cismap.commons.featureservice.AbstractFeatureService;
import de.cismet.cismap.commons.featureservice.FeatureServiceAttribute;
import de.cismet.cismap.commons.featureservice.H2FeatureService;
import de.cismet.cismap.commons.featureservice.ShapeFileFeatureService;
import de.cismet.cismap.commons.featureservice.style.StyleDialogInterface;
import de.cismet.cismap.commons.gui.MappingComponent;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
@ServiceProvider(
    service = StyleDialogInterface.class,
    position = 10
)
public class JumpSLDEditor implements StyleDialogInterface {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    private MultiInputDialog dialog;
    private Layer layer;
    private final ArrayList<StylePanel> stylePanels = new ArrayList<StylePanel>();
    private FeatureServiceFeature firstFeature;
    private AbstractFeatureService service;
    private String geomProperty;
    private JTabbedPane tabbedPane;
    private AllgemeinPanel allgemein;
    private QueryPanel queryPanel;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  l           DOCUMENT ME!
     * @param  bs          DOCUMENT ME!
     * @param  vs          DOCUMENT ME!
     * @param  ls          DOCUMENT ME!
     * @param  cts         DOCUMENT ME!
     * @param  ctsDefault  DOCUMENT ME!
     */
    public void setStyle(final Layer l,
            final BasicStyle bs,
            final VertexStyle vs,
            final LabelStyle ls,
            final ColorThemingStyle cts,
            final BasicStyle ctsDefault) {
        Style oldStyle = null;
        if (bs != null) {
            bs.setEnabled(true);
            oldStyle = l.getBasicStyle();
            if (oldStyle != bs) {
                if (oldStyle != null) {
                    l.removeStyle(oldStyle);
                }
                l.addStyle(bs);
            }
        } else if (ctsDefault != null) {
            ctsDefault.setEnabled(true);
            oldStyle = l.getBasicStyle();
            if (oldStyle != ctsDefault) {
                if (oldStyle != null) {
                    l.removeStyle(oldStyle);
                }
                l.addStyle(ctsDefault);
            }
        }
        if (vs != null) {
            vs.setEnabled(true);
            oldStyle = l.getVertexStyle();
            if (oldStyle != vs) {
                if (oldStyle != null) {
                    l.removeStyle(oldStyle);
                }
                l.addStyle(vs);
            }
        }
        if (ls != null) {
            ls.setEnabled(true);
            oldStyle = l.getLabelStyle();
            if (oldStyle != ls) {
                if (oldStyle != null) {
                    l.removeStyle(oldStyle);
                }
                l.addStyle(ls);
            }
        }
        if (cts != null) {
            try {
                cts.setDefaultStyle((BasicStyle)cts.getAttributeValueToBasicStyleMap().values().iterator().next());
                cts.setEnabled(true);
                oldStyle = l.getStyle(ColorThemingStyle.class);
                if (oldStyle != cts) {
                    if (oldStyle != null) {
                        l.removeStyle(oldStyle);
                    }
                    l.addStyle(cts);
                }
            } catch (NumberFormatException e) {
                logger.error("Error in StyleDialog", e);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  doc    DOCUMENT ME!
     * @param  layer  DOCUMENT ME!
     */
    public void importSLD(final Document doc, final Layer layer) {
        final LinkedList<String> rules = SLDImporter.getRuleNames(doc);

        if (rules.get(0).equals("ColorThemingDefaultValue")) {
            final String rule = rules.remove(0);
            rules.add(rule);
        }

        if (rules.isEmpty()) {
            logger.info("No style found");
            return;
        }

        if (rules.size() == 1) {
            try {
                final Element minElement = XPathUtils.getElement("//sld:Rule[sld:Name='" + rules.peek()
                                + "']/sld:MinScaleDenominator",
                        doc.getDocumentElement(),
                        SLDImporter.NSCONTEXT);
                final Element maxElement = XPathUtils.getElement("//sld:Rule[sld:Name='" + rules.peek()
                                + "']/sld:MaxScaleDenominator",
                        doc.getDocumentElement(),
                        SLDImporter.NSCONTEXT);
                // NOTE: layer.setMaxScale() saves the MinScaleDenominator and vice versa
                if (minElement != null) {
                    final Double max = new Double(minElement.getTextContent());
                    if (max.doubleValue() > 0) {
                        layer.setMaxScale(max);
                    }
                }
                if (maxElement != null) {
                    final Double min = new Double(maxElement.getTextContent());
                    if (min.doubleValue() > 0) {
                        layer.setMinScale(min);
                    }
                }
                if ((minElement != null) || (maxElement != null)) {
                    layer.setScaleDependentRenderingEnabled(true);
                }
            } catch (XPathExpressionException ex) {
                Exceptions.printStackTrace(ex);
            }
            setStyle(
                layer,
                SLDImporter.getBasicStyle(rules.peek(), doc),
                SLDImporter.getVertexStyle(rules.peek(), doc),
                SLDImporter.getLabelStyle(rules.peek(), doc),
                SLDImporter.getColorThemingStyle(rules.peek(), doc),
                null);
            return;
        }

        final LinkedList<String> colorThemeNames = SLDImporter.getPossibleColorThemingStyleNames(doc);
        final LinkedList<ColorThemingStyle> colorThemeStyles = new LinkedList<ColorThemingStyle>();
        for (final String styleName : colorThemeNames) {
            colorThemeStyles.add(SLDImporter.getColorThemingStyle(styleName, doc));
        }
        if (colorThemeStyles.size() > 1) {
            logger.info("Found multiple colorThemes in sld file, use first");
        }
        ColorThemingStyle cts = null;
        // contains all cts rules
        boolean firstStyle = true;
        final TreeSet<String> colorStyles = new TreeSet<String>();

        if (colorThemeStyles.size() != 0) {
            cts = colorThemeStyles.peek();
            colorStyles.addAll(cts.getAttributeValueToLabelMap().values());
        }

        Double totalMax = null;
        Double totalMin = null;
        final Element documentElement = doc.getDocumentElement();

        for (final String ruleName : rules) {
            // check the min/max scale
            if (!ruleName.equals("labelStyle")) {
                try {
                    if ((totalMax == null) || (totalMax != -1)) {
                        final Element maxElement = XPathUtils.getElement("//sld:Rule[sld:Name='" + ruleName
                                        + "']/sld:MaxScaleDenominator",
                                documentElement,
                                SLDImporter.NSCONTEXT);

                        if (maxElement != null) {
                            final Double max = new Double(maxElement.getTextContent());
                            if (max.doubleValue() > 0) {
                                if (totalMax == null) {
                                    totalMax = max;
                                } else if (totalMax > 0) {
                                    if (!totalMax.equals(max)) {
                                        totalMax = -1.0;
                                    }
                                }
                            }
                        } else {
                            totalMax = -1.0;
                        }
                    }

                    if ((totalMin == null) || (totalMin != -1)) {
                        final Element minElement = XPathUtils.getElement("//sld:Rule[sld:Name='" + ruleName
                                        + "']/sld:MinScaleDenominator",
                                documentElement,
                                SLDImporter.NSCONTEXT);

                        if (minElement != null) {
                            final Double min = new Double(minElement.getTextContent());

                            if (min.doubleValue() > 0) {
                                if (totalMin == null) {
                                    totalMin = min;
                                } else if (totalMin > 0) {
                                    if (!totalMin.equals(min)) {
                                        totalMin = -1.0;
                                    }
                                }
                            }
                        } else {
                            totalMin = -1.0;
                        }
                    }
                } catch (XPathExpressionException ex) {
                    logger.warn("XPath exception", ex);
                }
            }

            if (firstStyle || !colorStyles.contains(ruleName)) {
                firstStyle = false;
                BasicStyle bs = SLDImporter.getBasicStyle(ruleName, doc);

                // Do not convert a Style without a fill and line to a basic style
                if ((bs != null) && !bs.isRenderingFill() && !bs.isRenderingLine()) {
                    bs = null;
                }
                setStyle(
                    layer,
                    (cts != null) ? null : bs,
                    SLDImporter.getVertexStyle(ruleName, doc),
                    SLDImporter.getLabelStyle(ruleName, doc),
                    cts,
                    SLDImporter.getBasicStyle("ColorThemingDefaultValue", doc));
            }
        }

        // The scale can be used, if every rule have the same scale
        // NOTE: layer.setMaxScale() saves the MinScaleDenominator and vice versa
        if (((totalMax != null) && (totalMax > 0)) || ((totalMin != null) && (totalMin > 0))) {
            layer.setScaleDependentRenderingEnabled(true);

            if ((totalMax != null) && (totalMax > 0)) {
                layer.setMinScale(totalMax);
            }

            if ((totalMin != null) && (totalMin > 0)) {
                layer.setMaxScale(totalMin);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   featureService    DOCUMENT ME!
     * @param   parentFrame       DOCUMENT ME!
     * @param   mappingComponent  DOCUMENT ME!
     * @param   configTabs        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    @Override
    public JDialog configureDialog(final AbstractFeatureService featureService,
            final Frame parentFrame,
            final MappingComponent mappingComponent,
            final ArrayList<String> configTabs) {
        stylePanels.clear();
        final Blackboard blackboard = new Blackboard();
        final WorkbenchContext workbenchContext = new WorkbenchContext() {

                @Override
                public Blackboard getBlackboard() {
                    return blackboard; // To change body of generated methods, choose Tools | Templates.
                }
            };
        service = featureService;
        final FeatureSchema featureSchema = new FeatureSchema();
        List<FeatureServiceFeature> featureList;
        if (featureService.getFeatureFactory() != null) {
            featureList = featureService.getFeatureFactory().getLastCreatedFeatures();
            if (featureList.size() > 0) {
                firstFeature = ((FeatureServiceFeature)featureList.get(0));
            }
        } else {
            featureList = new ArrayList<FeatureServiceFeature>();
        }
        List props;
        boolean useFeature = true;

        if (service != null) {
            props = featureService.getOrderedFeatureServiceAttributes();
            useFeature = false;
        } else {
            props = new ArrayList(firstFeature.getProperties().entrySet());
        }

        AttributeType type;
        geomProperty = "GEOM";
        for (final Object entry : props) {
            String name;
            if (useFeature) {
                name = ((Map.Entry)entry).getKey().toString();
                if (((Map.Entry)entry).getValue() instanceof Geometry) {
                    type = AttributeType.GEOMETRY;
                    geomProperty = (String)((Map.Entry)entry).getKey();
                } else if (((Map.Entry)entry).getValue() instanceof String) {
                    type = AttributeType.STRING;
                } else if (((Map.Entry)entry).getValue() instanceof Integer) {
                    type = AttributeType.INTEGER;
                } else if (((Map.Entry)entry).getValue() instanceof Long) {
                    type = AttributeType.INTEGER;
                } else if (((Map.Entry)entry).getValue() instanceof Double) {
                    type = AttributeType.DOUBLE;
                } else if (((Map.Entry)entry).getValue() instanceof Float) {
                    type = AttributeType.DOUBLE;
                } else if (((Map.Entry)entry).getValue() instanceof Date) {
                    type = AttributeType.DATE;
                } else if (((Map.Entry)entry).getValue() instanceof java.sql.Date) {
                    type = AttributeType.DATE;
                } else {
                    type = AttributeType.OBJECT;
                }
            } else {
                final FeatureServiceAttribute attr = (FeatureServiceAttribute)
                    featureService.getFeatureServiceAttributes().get(entry);
                name = (String)entry;

                if (attr.isGeometry()) {
                    type = AttributeType.GEOMETRY;
                    geomProperty = name;
                } else if (attr.getType().equals(String.valueOf(Types.CHAR))
                            || attr.getType().equals(String.valueOf(Types.VARCHAR))
                            || attr.getType().equals(String.valueOf(Types.LONGVARCHAR))) {
                    type = AttributeType.STRING;
                } else if (attr.getType().equals(String.valueOf(Types.INTEGER))
                            || attr.getType().equals(String.valueOf(Types.BIGINT))
                            || attr.getType().equals(String.valueOf(Types.SMALLINT))
                            || attr.getType().equals(String.valueOf(Types.TINYINT))) {
                    type = AttributeType.INTEGER;
                } else if (attr.getType().equals(String.valueOf(Types.DOUBLE))
                            || attr.getType().equals(String.valueOf(Types.FLOAT))
                            || attr.getType().equals(String.valueOf(Types.DECIMAL))) {
                    type = AttributeType.DOUBLE;
                } else if (attr.getType().equals(String.valueOf(Types.DATE))
                            || attr.getType().equals(String.valueOf(Types.TIME))
                            || attr.getType().equals(String.valueOf(Types.TIMESTAMP))) {
                    type = AttributeType.DATE;
                } else {
                    type = AttributeType.OBJECT;
                }
            }

            featureSchema.addAttribute(name, type);
        }
        final List<Feature> jumpFeatures = new LinkedList<Feature>();
        for (final FeatureServiceFeature feature : featureList) {
            jumpFeatures.add(new Feature() {

                    @Override
                    public void setAttributes(final Object[] attributes) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public void setSchema(final FeatureSchema schema) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public int getID() {
                        return feature.getId();
                    }

                    @Override
                    public void setAttribute(final int attributeIndex, final Object newAttribute) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public void setAttribute(final String attributeName, final Object newAttribute) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public void setGeometry(final Geometry geometry) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public Object getAttribute(final int i) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public Object getAttribute(final String name) {
                        return feature.getProperty(name);
                    }

                    @Override
                    public String getString(final int attributeIndex) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public int getInteger(final int attributeIndex) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public double getDouble(final int attributeIndex) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public String getString(final String attributeName) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public Geometry getGeometry() {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public FeatureSchema getSchema() {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public Feature clone(final boolean deep) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public Object[] getAttributes() {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public int compareTo(final Object o) {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }

                    @Override
                    public Object clone() {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }
                });
        }
        final FeatureCollection features = new FeatureDataset(jumpFeatures, featureSchema);
        final LayerManager layerManager = new LayerManager();
        layerManager.setFiringEvents(false);
        layer = new Layer(featureService.getName(), Color.BLUE, features, layerManager);

        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Reader sld = featureService.getSLDDefiniton();
            final InputSource is = new InputSource(sld);
            final Document doc = builder.parse(is);
            importSLD(doc, layer);
        } catch (IOException e) {
            logger.info("could not load the old sld definition", e);
        } catch (ParserConfigurationException e) {
            logger.info("could not load the old sld definition", e);
        } catch (SAXException e) {
            logger.info("could not load the old sld definition", e);
        } catch (AssertionFailedException e) {
            logger.info("could not load the old sld definition", e);
        }
        dialog = new MultiInputDialog(parentFrame, "Style Editor", true);
        dialog.setInset(0);
        dialog.setSideBarImage(IconLoader.icon("Symbology.gif"));
        dialog.setSideBarDescription(I18N.get(
                "ui.style.ChangeStylesPlugIn.you-can-use-this-dialog-to-change-the-colour-line-width"));
        if (configTabs.contains("Allgemein")) {
            allgemein = new AllgemeinPanel(service);
            stylePanels.add(allgemein);
        }
        DeeRenderingStylePanel renderingStylePanel = null;
        if (configTabs.contains("Darstellung")) {
            renderingStylePanel = new CidsRenderingStylePanel(blackboard, layer, blackboard);
            stylePanels.add(renderingStylePanel);
        }
        CidsScalePanel.mappingComponent = mappingComponent;
        if (configTabs.contains("Massstab")) {
            stylePanels.add(new CidsScalePanel(layer, mappingComponent, service));
        }

        if (configTabs.contains("Thematische Farbgebung")) {
            if (featureSchema.getAttributeCount() > 1) {
                final ColorThemingStylePanel colorThemingStylePanel = new ColorThemingStylePanel(
                        layer,
                        workbenchContext);
                colorThemingStylePanel.setPreferredSize(new Dimension(610, 300));
                stylePanels.add(colorThemingStylePanel);
                if (configTabs.contains("Darstellung")) {
                    GUIUtil.sync(renderingStylePanel.getTransparencySlider(),
                        colorThemingStylePanel.getTransparencySlider());
                }
            } else {
                stylePanels.add(new DummyColorThemingStylePanel());
            }
        }
        if (configTabs.contains("Beschriftung")) {
            stylePanels.add(new CidsLabelStylePanel(layer, dialog));
        }
        if (configTabs.contains("Begleitsymbole")) {
            final DecorationStylePanel decorationStylePanel = new DecorationStylePanel(layer, new ArrayList());
            decorationStylePanel.setPreferredSize(new Dimension(450, 300));
            stylePanels.add(decorationStylePanel);
        }

        if (configTabs.contains("QueryPanel")) {
            queryPanel = new QueryPanel(service);
            stylePanels.add(queryPanel);
        }
        SLDDefinitionPanel definitonPanel = null;
        if (configTabs.contains("TextEditor")) {
            definitonPanel = new SLDDefinitionPanel();
            stylePanels.add(definitonPanel);
        }

        tabbedPane = new JTabbedPane();

        for (final Iterator<StylePanel> i = stylePanels.iterator(); i.hasNext();) {
            final StylePanel stylePanel = i.next();
            tabbedPane.add((Component)stylePanel, stylePanel.getTitle());
            dialog.addEnableChecks(stylePanel.getTitle(),
                Arrays.asList(
                    new EnableCheck[] {
                        new EnableCheck() {

                            @Override
                            public String check(final JComponent component) {
                                return stylePanel.validateInput();
                            }
                        }
                    }));
        }

        if (configTabs.contains("TextEditor")) {
            final SLDDefinitionPanel panel = definitonPanel;
            tabbedPane.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(final ChangeEvent e) {
                        for (final Iterator<StylePanel> i = stylePanels.iterator(); i.hasNext();) {
                            final StylePanel stylePanel = i.next();
                            stylePanel.updateStyles();
                        }
                        if (tabbedPane.getSelectedComponent() instanceof SLDDefinitionPanel) {
                            panel.setSLDString(exportSLD());
                        }
                    }
                });
        }

        // mappingComponent.reconsiderFeature(null);
        dialog.addRow(tabbedPane);

        // String selectedTab = (String) blackboard.get(LAST_TAB_KEY, (stylePanels.iterator().next()).getTitle());

        // tabbedPane.setSelectedComponent(find(stylePanels, selectedTab));
        dialog.pack();
        return dialog;
    }

    @Override
    public boolean isAccepted() {
        return dialog.wasOKPressed();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected String exportSLD() {
        String sld = null;
        try {
            final Java2XML java2Xml = new Java2XML();
//            final File tempFile = File.createTempFile("layer", ".xml");
            final StringWriter xmlWriter = new StringWriter();
            final Geometry geom = firstFeature.getGeometry();

            java2Xml.write(layer, "layer", xmlWriter);
            final HashMap<String, String> params = new HashMap<String, String>();
            params.put("wmsLayerName", service.getName());
            params.put("featureTypeStyle", service.getName());
            params.put("styleName", service.getName());
            params.put("styleTitle", service.getName());
            params.put("Namespace", "http://cismet.de");
            params.put("NamespacePrefix", "");
            params.put("geoType", geom.getGeometryType());
            params.put("geomProperty", geomProperty);
            if (layer.getMinScale() != null) {
                params.put("maxScale", "" + layer.getMinScale());
            }
            if (layer.getMaxScale() != null) {
                params.put("minScale", "" + layer.getMaxScale());
            }

            sld = LayerStyle2SLDPlugIn.transformContext(new StringReader(xmlWriter.toString()), params);
        } catch (Exception e) {
            logger.info("could not save sld definition", e);
        }
        return sld;
    }

    @Override
    public Runnable createResultTask() {
        return new Runnable() {

                @Override
                public void run() {
                    // final Collection<?> oldStyles = layer.cloneStyles();

                    //J-
                layer.getLayerManager().deferFiringEvents(new Runnable() {
                    @Override
                    public void run() {
                        for (Iterator<StylePanel> i = stylePanels.iterator(); i.hasNext();) {
                            StylePanel stylePanel = i.next();
                            stylePanel.updateStyles();
                        }

                    }
                });

                // fix the problem with mixing styles
                layer.getLayerManager().deferFiringEvents(new Runnable() {
                    @Override
                    public void run() {
                        if (layer.getVertexStyle().isEnabled()) {
                            layer.getBasicStyle().setRenderingVertices(false);
                        }
                    }
                });

                if (service instanceof ShapeFileFeatureService) {
                    final ShapeFileFeatureService sffs = (ShapeFileFeatureService)service;

                    if (sffs.isFileNotFound() && (allgemein != null && !allgemein.equals(sffs.getDocumentURI().toString()))) {
                        layer.getLayerManager().deferFiringEvents(new Runnable() {
                            @Override
                            public void run() {
                                String uriAsString = allgemein.getSource();

                                try {
                                    URI uri = new URI(uriAsString);
                                    File f = new File(uri);

                                    if (!f.exists()) {
                                        JOptionPane.showMessageDialog(allgemein,
                                                NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().fileNotFound.message", uriAsString),
                                                NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().fileNotFound.title"),
                                                JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                    sffs.setDocumentURI(uri);
                                } catch (Exception e)  {
                                    logger.error("Invalid shape file path", e);
                                    JOptionPane.showMessageDialog(allgemein,
                                            NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().invalidUri.message", uriAsString),
                                            NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().invalidUri.title"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    }
                }

                if (service instanceof H2FeatureService) {
                    final H2FeatureService sffs = (H2FeatureService)service;

                    if (sffs.isTableNotFound() && (allgemein != null)) {
                        layer.getLayerManager().deferFiringEvents(new Runnable() {
                            @Override
                            public void run() {
                                String uriAsString = allgemein.getSource();

                                try {
                                    URI uri = new URI(uriAsString);
                                    File f = new File(uri);

                                    if (!f.exists()) {
                                        JOptionPane.showMessageDialog(allgemein,
                                                NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().fileNotFound.message", uriAsString),
                                                NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().fileNotFound.title"),
                                                JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                    sffs.createFromShapeFile(uri);
                                } catch (Exception e)  {
                                    logger.error("Invalid shape file path", e);
                                    JOptionPane.showMessageDialog(allgemein,
                                            NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().invalidUri.message", uriAsString),
                                            NbBundle.getMessage(JumpSLDEditor.class, "JumpSLDEditor.createResultTask().invalidUri.title"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    }
                }

                allgemein.syncServiceWithModel();
                if (queryPanel != null) {
                    queryPanel.syncServiceWithPanel();
                }
                    //J+

                    String sld;
                    if (tabbedPane.getSelectedComponent() instanceof SLDDefinitionPanel) {
                        sld = ((SLDDefinitionPanel)tabbedPane.getSelectedComponent()).getSLDString();
                    } else {
                        sld = exportSLD();
                    }
                    service.setSLDInputStream(sld);
                    service.retrieve(true);
                }
            };
    }

    @Override
    public String getKey() {
        return "Jump";
    }
}

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

import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.style.ScaleStylePanel;

import org.openide.util.NbBundle;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.cismet.cismap.cidslayer.CidsLayer;

import de.cismet.cismap.commons.featureservice.AbstractFeatureService;
import de.cismet.cismap.commons.gui.MappingComponent;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
public class CidsScalePanel extends ScaleStylePanel {

    //~ Static fields/initializers ---------------------------------------------

    public static MappingComponent mappingComponent;
    /** The same dpi that is used in the MappingComponent to calculate the scale. */
    private static final int ASSUMED_DPI = 100;
    private static final double METER_PER_INCH = 0.0254;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsScalePanel object.
     *
     * @param  layer             DOCUMENT ME!
     * @param  mappingComponent  DOCUMENT ME!
     * @param  service           DOCUMENT ME!
     */
    public CidsScalePanel(final Layer layer,
            final MappingComponent mappingComponent,
            final AbstractFeatureService service) {
        super(layer, null);

        super.currentScale1Label.setVisible(false);
        super.currentScaleLabel.setVisible(false);
        super.currentScaleTextField.setVisible(false);
        super.spacerPanelBelowCurrentScale.setVisible(false);
        super.showAtThisScaleButton.setVisible(false);

        if (service instanceof CidsLayer) {
            final CidsLayer cidsLayer = (CidsLayer)service;
            final Double maxArea = cidsLayer.getMaxArea();

            if (maxArea != null) {
                final GridBagConstraints serverScale = new GridBagConstraints();
                serverScale.gridx = 1;
                serverScale.gridy = 2;
                serverScale.insets = new Insets(2, 2, 5, 2);
                serverScale.anchor = GridBagConstraints.WEST;
                serverScale.gridwidth = 6;

                final JPanel pan = new JPanel();
                pan.setLayout(new GridLayout(3, 1));
                final JCheckBox chBox = new JCheckBox(NbBundle.getMessage(
                            CidsScalePanel.class,
                            "CidsScalePanel.CidsScalePanel()"));
                chBox.setEnabled(false);
                chBox.setSelected(true);

                final JPanel minPanel = new JPanel(new GridLayout(1, 2));
                final JLabel minScaleLab = new JLabel(NbBundle.getMessage(
                            CidsScalePanel.class,
                            "CidsScalePanel.CidsScalePanel().minScale"));
                minPanel.add(minScaleLab);
                final Dimension mapDimension = mappingComponent.getSize(null);
                final double mapSizeInSqrMeter = mapDimension.getWidth() * mapDimension.getHeight()
                            * Math.pow(METER_PER_INCH / ASSUMED_DPI, 2);
                final JTextField minScale = new JTextField("1:"
                                + (long)(Math.sqrt(maxArea.longValue() / mapSizeInSqrMeter)));
                minScale.setEnabled(false);
                minPanel.add(minScale);

                final JPanel maxPanel = new JPanel(new GridLayout(1, 2));
                final JLabel maxScaleLab = new JLabel(NbBundle.getMessage(
                            CidsScalePanel.class,
                            "CidsScalePanel.CidsScalePanel().maxScale"));
                maxPanel.add(maxScaleLab);
                final JTextField maxScale = new JTextField("1:1");
                maxScale.setEnabled(false);
                maxPanel.add(maxScale);

                pan.add(chBox);
                pan.add(minPanel);
                pan.add(maxPanel);
                super.add(pan, serverScale);
            }
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected double realScale() {
        return mappingComponent.getScaleDenominator();
    }

    @Override
    protected double computeScaleFactor(final double internalScale, final double realScale) {
        return 1;
    }

    @Override
    protected double currentScale() {
        return mappingComponent.getScaleDenominator();
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(CidsScalePanel.class, "CidsScalePanel.getTitle()");
    }
}

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
import java.awt.GridBagLayout;
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
        super.hideAboveCurrentScaleButton.setVisible(false);
        super.hideBelowCurrentScaleButton.setVisible(false);

        if (service instanceof CidsLayer) {
            final CidsLayer cidsLayer = (CidsLayer)service;
            final Double maxScale = cidsLayer.getMaxScale();
            final Double maxArea = cidsLayer.getMaxArea();

            if ((maxArea != null) || (maxScale != null)) {
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

                final JPanel minPanel = new JPanel(new GridBagLayout());
                final JLabel minScaleLab = new JLabel(NbBundle.getMessage(
                            CidsScalePanel.class,
                            "CidsScalePanel.CidsScalePanel().minScale"));
                final JLabel oneMinLab = new JLabel("1:");
                minScaleLab.setPreferredSize(new Dimension(158, 20));
                minPanel.add(
                    minScaleLab,
                    new GridBagConstraints(
                        0,
                        0,
                        1,
                        1,
                        0,
                        0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 10, 0, 0),
                        0,
                        0));
                minPanel.add(
                    oneMinLab,
                    new GridBagConstraints(
                        1,
                        0,
                        1,
                        1,
                        0,
                        0,
                        GridBagConstraints.NORTHEAST,
                        GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 0),
                        0,
                        0));
                final Dimension mapDimension = mappingComponent.getSize(null);
                final double mapSizeInSqrMeter = mapDimension.getWidth() * mapDimension.getHeight()
                            * Math.pow(METER_PER_INCH / ASSUMED_DPI, 2);
                double finalScale = -1;

                if (maxArea != null) {
                    finalScale = Math.sqrt(maxArea.longValue() / mapSizeInSqrMeter);
                }

                if (maxScale != null) {
                    if (finalScale != -1) {
                        finalScale = Math.min(finalScale, maxScale);
                    } else {
                        finalScale = maxScale;
                    }
                }

                final JTextField minScale = new JTextField("1:"
                                + (long)finalScale);
                minScale.setEnabled(false);
                minScale.setPreferredSize(new Dimension(82, 20));
                minPanel.add(
                    minScale,
                    new GridBagConstraints(
                        2,
                        0,
                        1,
                        1,
                        0,
                        0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(2, 5, 0, 0),
                        0,
                        0));
                minPanel.add(
                    new JPanel(),
                    new GridBagConstraints(
                        3,
                        0,
                        1,
                        1,
                        1,
                        0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 10, 0, 0),
                        0,
                        0));

                final JPanel maxPanel = new JPanel(new GridBagLayout());
                final JLabel maxScaleLab = new JLabel(NbBundle.getMessage(
                            CidsScalePanel.class,
                            "CidsScalePanel.CidsScalePanel().maxScale"));
                final JLabel oneLab = new JLabel("1:");
                maxScaleLab.setPreferredSize(new Dimension(158, 20));
                maxPanel.add(
                    maxScaleLab,
                    new GridBagConstraints(
                        0,
                        0,
                        1,
                        1,
                        0,
                        0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 10, 0, 0),
                        0,
                        0));
                maxPanel.add(
                    oneLab,
                    new GridBagConstraints(
                        1,
                        0,
                        1,
                        1,
                        0,
                        0,
                        GridBagConstraints.NORTHEAST,
                        GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 0),
                        0,
                        0));
                final JTextField txtMaxScale = new JTextField("1");
                txtMaxScale.setEnabled(false);
                txtMaxScale.setPreferredSize(new Dimension(82, 20));
                maxPanel.add(
                    txtMaxScale,
                    new GridBagConstraints(
                        2,
                        0,
                        1,
                        1,
                        0,
                        0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(2, 5, 0, 0),
                        0,
                        0));
                maxPanel.add(
                    new JPanel(),
                    new GridBagConstraints(
                        3,
                        0,
                        1,
                        1,
                        1,
                        0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 10, 0, 0),
                        0,
                        0));

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

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
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.style.LabelStylePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
public class CidsLabelStylePanel extends LabelStylePanel {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsLabelStylePanel object.
     *
     * @param  layer   DOCUMENT ME!
     * @param  dialog  DOCUMENT ME!
     */
    public CidsLabelStylePanel(final Layer layer, final MultiInputDialog dialog) {
        super(layer, null, dialog, null);
        this.remove(previewPanel);
        previewPanel = new JPanel() {

                @Override
                protected void paintComponent(final Graphics g) {
                    super.paintComponent(g); // To change body of generated methods, choose Tools | Templates.
                }
            };
        previewPanel.setBackground(Color.white);
        previewPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        previewPanel.setMaximumSize(new Dimension(200, 38));
        previewPanel.setMinimumSize(new Dimension(200, 38));
        previewPanel.setPreferredSize(new Dimension(200, 38));
        this.add(
            previewPanel,
            new GridBagConstraints(
                0,
                15,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 10, 0, 4),
                0,
                0));
    }
}

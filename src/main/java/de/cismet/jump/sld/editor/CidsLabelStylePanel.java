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

import java.awt.GridBagConstraints;
import java.awt.Insets;

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
        this.remove(horizontalAlignmentComboBox);
        this.remove(horizontalAlignmentLabel);
        this.remove(horizontalPositionComboBox);
        this.remove(horizontalPositionLabel);
        this.remove(verticalAlignmentComboBox);
        this.remove(verticalAlignmentLabel);
        this.remove(scaleCheckBox);
        this.remove(hideOverlappingLabelsCheckBox);
        this.remove(previewLabel);
        this.remove(previewPanel);
        this.remove(outlineButtonPanel);
        this.remove(showOutlineCheckBox);
        this.add(new JPanel(), new GridBagConstraints(0, 14, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(190, 0, 0, 0), 0, 0));
//        previewPanel = new JPanel() {
//
//                @Override
//                protected void paintComponent(final Graphics g) {
//                    super.paintComponent(g); // To change body of generated methods, choose Tools | Templates.
//                }
//            };
//        previewPanel.setBackground(Color.white);
//        previewPanel.setBorder(BorderFactory.createLoweredBevelBorder());
//        previewPanel.setMaximumSize(new Dimension(200, 38));
//        previewPanel.setMinimumSize(new Dimension(200, 38));
//        previewPanel.setPreferredSize(new Dimension(200, 38));
//        this.add(
//            previewPanel,
//            new GridBagConstraints(
//                0,
//                15,
//                2,
//                1,
//                1.0,
//                1.0,
//                GridBagConstraints.WEST,
//                GridBagConstraints.NONE,
//                new Insets(10, 10, 45, 4),
//                0,
//                0));
//        this.add(previewLabel,
//            new GridBagConstraints(0, 14, 2, 1, 0.0, 0.0,
//                GridBagConstraints.WEST, GridBagConstraints.NONE,
//                new Insets(15, 0, 0, 0), 0, 0));        
    }
}

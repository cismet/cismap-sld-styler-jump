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

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.renderer.style.ColorThemingStylePanel;
import com.vividsolutions.jump.workbench.ui.style.StylePanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
public class DummyColorThemingStylePanel extends JPanel implements StylePanel {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DummyColorThemingStylePanel object.
     */
    public DummyColorThemingStylePanel() {
        super(new GridBagLayout());
        add(new JLabel(I18N.get("ui.style.ChangeStylesPlugIn.this-layer-has-no-attributes")));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getTitle() {
        return ColorThemingStylePanel.TITLE;
    }

    @Override
    public void updateStyles() {
    }

    @Override
    public String validateInput() {
        return null;
    }
}

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

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsScalePanel object.
     *
     * @param  layer             DOCUMENT ME!
     * @param  mappingComponent  DOCUMENT ME!
     */
    public CidsScalePanel(final Layer layer, final MappingComponent mappingComponent) {
        super(layer, null);
        // this.mappingComponent = mappingComponent;
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
}

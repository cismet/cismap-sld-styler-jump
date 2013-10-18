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

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;

import de.latlon.deejump.plugin.style.DeeRenderingStylePanel;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
public class CidsRenderingStylePanel extends DeeRenderingStylePanel {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsRenderingStylePanel object.
     *
     * @param  blackboard             DOCUMENT ME!
     * @param  layer                  DOCUMENT ME!
     * @param  persistanceBlackboard  DOCUMENT ME!
     */
    public CidsRenderingStylePanel(final Blackboard blackboard,
            final Layer layer,
            final Blackboard persistanceBlackboard) {
        super(blackboard, layer, persistanceBlackboard);
    }
}

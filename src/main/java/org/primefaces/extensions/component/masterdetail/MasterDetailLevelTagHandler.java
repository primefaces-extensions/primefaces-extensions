/*
* @author  Oleg Varaksin (ovaraksin@googlemail.com)
* $$Id$$
*/

package org.primefaces.extensions.component.masterdetail;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.TagAttribute;

public class MasterDetailLevelTagHandler extends ComponentHandler
{
    private final TagAttribute level;

    public MasterDetailLevelTagHandler(final ComponentConfig config) {
        super(config);
        this.level = getRequiredAttribute("level");
    }
}

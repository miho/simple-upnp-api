/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.upnp.sonos;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.Service;
import org.tensin.sonos.control.AbstractService;
import org.tensin.sonos.control.SonosActionInvocation;
import org.tensin.sonos.control.ZonePlayerConstants;
import org.tensin.sonos.model.UpdateType;
import org.tensin.sonos.model.ZoneGroupState;
import org.tensin.sonos.xml.ResultParser;
import org.xml.sax.SAXException;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
class ZoneGroupService extends AbstractService {

    public ZoneGroupService(final UpnpService upnpService, final Service service) {
        super(upnpService, service, ZonePlayerConstants.SONOS_SERVICE_ZONE_GROUP_TOPOLOGY);
    }

    public ZoneGroupState getZoneGroupState() {
        String action = "GetZoneGroupState";

        SonosActionInvocation invocation
                = messageFactory.getMessage(getService(), action);
        executeImmediate(invocation);

        try {
            ZoneGroupState state
                    = ResultParser.getGroupStateFromResult(
                            invocation.getOutputAsString("ZoneGroupState"));
            
            return state;
            
        } catch (SAXException ex) {
            Logger.getLogger(ZoneGroupService.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    //    public String get
    public static void main(String[] args) {
        ZonePlayers p = ZonePlayers.discover();
        
        ZonePlayer zp =p.getPlayers(3000).get(0);
        System.out.println(zp.getZoneGroups(p).toString());
        System.out.println("ID:::"+zp.getZoneId());
        System.exit(0);
    }

}

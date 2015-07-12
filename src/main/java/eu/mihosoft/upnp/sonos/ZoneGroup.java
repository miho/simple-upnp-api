/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.upnp.sonos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class ZoneGroup {

    private final List<String> devices = new ArrayList<>();
    private final String coordinator;
    private String name;
    private final String id;
    private final ZonePlayers zonePlayers;

    ZoneGroup(ZonePlayers zPlayers, org.tensin.sonos.model.ZoneGroup g) {
        this.zonePlayers = zPlayers;
        this.devices.addAll(g.getMembers());
        this.coordinator = g.getCoordinator();
        this.id = g.getId();
        this.name = null;
    }

    public List<String> getDevices() {
        return devices;
    }

    public ZonePlayer getCoordinator() {
        return zonePlayers.getDiscoveredPlayers().
                stream().filter(
                        (p) -> Objects.equals(
                                p.getZoneId(),
                                getCoordinatorId())).
                findFirst().orElse(null);
    }

    /**
     * @return the coordinator
     */
    private String getCoordinatorId() {
        return coordinator;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        
        if (name == null) {
            this.name = getCoordinator().getZoneName();
        }
        
        return name;
    }

    @Override
    public String toString() {

        String memberString = String.join(", ", devices);

        return "[Group: " + getId()
                + ", coordinator: " + getCoordinatorId()
                + ", [members: " + memberString + "]]";
    }

}

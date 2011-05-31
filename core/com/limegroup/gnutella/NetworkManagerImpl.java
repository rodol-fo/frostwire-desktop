package com.limegroup.gnutella;

import java.io.IOException;
import java.util.Properties;

import org.limewire.io.NetworkInstanceUtils;
import org.limewire.io.NetworkUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.limegroup.gnutella.connection.RoutedConnection;
import com.limegroup.gnutella.messages.vendor.HeaderUpdateVendorMessage;
import com.limegroup.gnutella.settings.SearchSettings;
import com.limegroup.gnutella.statistics.OutOfBandStatistics;

@Singleton
public class NetworkManagerImpl implements NetworkManager {
    
    private final Provider<UDPService> udpService;
    private final Provider<ActivityCallback> activityCallback;
    private final OutOfBandStatistics outOfBandStatistics;
    private final NetworkInstanceUtils networkInstanceUtils;
    
    @Inject
    public NetworkManagerImpl(Provider<UDPService> udpService,
            Provider<ActivityCallback> activityCallback,
            OutOfBandStatistics outOfBandStatistics,
            NetworkInstanceUtils networkInstanceUtils) {
        this.udpService = udpService;
        this.activityCallback = activityCallback;
        this.outOfBandStatistics = outOfBandStatistics;
        this.networkInstanceUtils = networkInstanceUtils;
    }
    

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#isIpPortValid()
     */
    public boolean isIpPortValid() {
        return (NetworkUtils.isValidAddress(getAddress()) &&
                NetworkUtils.isValidPort(getPort()));
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#getUDPConnectBackGUID()
     */
    public GUID getUDPConnectBackGUID() {
        return udpService.get().getConnectBackGUID();
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#isOOBCapable()
     */
    public boolean isOOBCapable() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#isGUESSCapable()
     */
    public boolean isGUESSCapable() {
    	return udpService.get().isGUESSCapable();
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#getNonForcedPort()
     */
    public int getNonForcedPort() {
        return 0;//acceptor.get().getPort(false);
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#getPort()
     */    
    public int getPort() {
    	return 0;//acceptor.get().getPort(true);
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#getNonForcedAddress()
     */
    public byte[] getNonForcedAddress() {
        return null;// acceptor.get().getAddress(false);
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#getAddress()
     */
    public byte[] getAddress() {
    	return null;// acceptor.get().getAddress(true);
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#getExternalAddress()
     */
    public byte[] getExternalAddress() {
        return null;// acceptor.get().getExternalAddress();
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#incomingStatusChanged()
     */
    public boolean incomingStatusChanged() {
        activityCallback.get().handleAddressStateChanged();
        // Only continue if the current address/port is valid & not private.
        byte addr[] = getAddress();
        int port = getPort();
        if(!NetworkUtils.isValidAddress(addr))
            return false;
        if(networkInstanceUtils.isPrivateAddress(addr))
            return false;            
        if(!NetworkUtils.isValidPort(port))
            return false;
            
        return true;
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#addressChanged()
     */
    // TODO: Convert to listener pattern
    public boolean addressChanged() {
//        activityCallback.get().handleAddressStateChanged();        
//        
//        // Only continue if the current address/port is valid & not private.
//        byte addr[] = getAddress();
//        int port = getPort();
//        if(!NetworkUtils.isValidAddress(addr))
//            return false;
//        if(networkInstanceUtils.isPrivateAddress(addr))
//            return false;            
//        if(!NetworkUtils.isValidPort(port))
//            return false;
//    
//        
//        // reset the last connect back time so the next time the TCP/UDP
//        // validators run they try to connect back.
//        acceptor.get().resetLastConnectBackTime();
//        
//    	Properties props = new Properties();
//    	props.put(HeaderNames.LISTEN_IP,NetworkUtils.ip2string(addr)+":"+port);
//    	HeaderUpdateVendorMessage huvm = new HeaderUpdateVendorMessage(props);
//    	
//        for(RoutedConnection c : connectionManager.get().getInitializedConnections()) {
//    		if (c.getConnectionCapabilities().remoteHostSupportsHeaderUpdate() >= HeaderUpdateVendorMessage.VERSION)
//    			c.send(huvm);
//    	}
//    	
//        for(RoutedConnection c : connectionManager.get().getInitializedClientConnections()) {
//    		if (c.getConnectionCapabilities().remoteHostSupportsHeaderUpdate() >= HeaderUpdateVendorMessage.VERSION)
//    			c.send(huvm);
//    	}
        
        return true;
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#acceptedIncomingConnection()
     */
    public boolean acceptedIncomingConnection() {
    	return false;// acceptor.get().acceptedIncoming();
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#setListeningPort(int)
     */
    public void setListeningPort(int port) throws IOException {
        //acceptor.get().setListeningPort(port);
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#canReceiveUnsolicited()
     */
    public boolean canReceiveUnsolicited() {
    	return udpService.get().canReceiveUnsolicited();
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#canReceiveSolicited()
     */
    public boolean canReceiveSolicited() {
    	return udpService.get().canReceiveSolicited();
    }

    /* (non-Javadoc)
     * @see com.limegroup.gnutella.NetworkManager#canDoFWT()
     */
    public boolean canDoFWT() {
        return udpService.get().canDoFWT();
    }
    
    public int getStableUDPPort() {
        return udpService.get().getStableUDPPort();
    }

    public GUID getSolicitedGUID() {
        return udpService.get().getSolicitedGUID();
    }

    public int supportsFWTVersion() {
        return  0;
    }
    
    public boolean isPrivateAddress(byte[] addr) {
        return networkInstanceUtils.isPrivateAddress(addr);
    }
}

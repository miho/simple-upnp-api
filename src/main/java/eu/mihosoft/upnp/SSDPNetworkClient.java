/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.upnp.sonos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 * @see <a href=http://www.kompf.de/java/multicast.html">
 * http://www.kompf.de/java/multicast.html</a>
 */
public class SSDPNetworkClient {

    /**
     * UPNP/SSDP client to demonstrate the usage of UDP multicast sockets.
     *
     * @throws IOException
     */
    public void multicast() throws IOException {
        try {
            InetAddress multicastAddress = InetAddress.getByName("239.255.255.250");
            // multicast address for SSDP
            final int port = 1900; // standard port for SSDP
            MulticastSocket socket = new MulticastSocket(port);
            socket.setReuseAddress(true);
            socket.setSoTimeout(15000);
            socket.joinGroup(multicastAddress);

            // send discover
            byte[] txbuf = DISCOVER_MESSAGE_ROOTDEVICE.getBytes("UTF-8");
            DatagramPacket hi = new DatagramPacket(txbuf, txbuf.length,
                    multicastAddress, port);
            socket.send(hi);
            System.out.println("SSDP discover sent");

            do {
                byte[] rxbuf = new byte[8192];
                DatagramPacket packet = new DatagramPacket(rxbuf, rxbuf.length);
                socket.receive(packet);
                dumpPacket(packet);
            } while (true); // should leave loop by SocketTimeoutException
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout");
        }
    }

    private void dumpPacket(DatagramPacket packet) throws IOException {
        InetAddress addr = packet.getAddress();
        System.out.println("Response from: " + addr);
        ByteArrayInputStream in = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
        copyStream(in, System.out);
    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(in);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        int c = bin.read();
        while (c != -1) {
            out.write((char) c);
            c = bin.read();
        }
        bout.flush();
    }

    private final static String DISCOVER_MESSAGE_ROOTDEVICE
            = "M-SEARCH * HTTP/1.1\r\n"
            + "ST: upnp:rootdevice\r\n"
            + "MX: 3\r\n"
            + "MAN: \"ssdp:discover\"\r\n"
            + "HOST: 239.255.255.250:1900\r\n\r\n";

    /**
     * MAIN
     */
    public static void main(String[] args) throws Exception {
        SSDPNetworkClient client = new SSDPNetworkClient();
        client.multicast();
    }
}

package nl.robojan.real_pipboy.Connection;

import nl.robojan.real_pipboy.Connection.Packets.DataPacket;

/**
 * Created by s120330 on 20-7-2015.
 */
public interface IPacketHandler {
    void HandlePacket(DataPacket packet);
}

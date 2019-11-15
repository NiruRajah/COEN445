package data;

import java.io.IOException;

public abstract class PacketHandler {

	public abstract void process(Packet packet) throws ClassNotFoundException, IOException;

}

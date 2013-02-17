package CCPortable.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
		handlePacket(packet, playerEntity);
	}
	
	public void handlePacket(Packet250CustomPayload packet, Player ply)
	{
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(packet.data)));
        
		String data;
		
        try {
                data = inputStream.readLine();
        } catch (IOException e) {
                e.printStackTrace();
                return;
        }
        String[] message = data.split(":");
        ObjectPDA pda;
        if (message[0] == "CPOS") {
        	pda = (ObjectPDA) CCPortable.allPDAs.get(Integer.parseInt(message[3]));
        	pda.setCursorPos(Integer.parseInt(message[1]),Integer.parseInt(message[2]), false);
        	CCPortable.allPDAs.put(Integer.parseInt(message[3]),pda);
        } else if (message[0] == "WRIT") {
        	pda = (ObjectPDA) CCPortable.allPDAs.get(Integer.parseInt(message[2]));
        	pda.write(message[1], false);
        	CCPortable.allPDAs.put(Integer.parseInt(message[2]),pda);
        } else if (message[0] == "ALL") {
        	pda = (ObjectPDA) CCPortable.allPDAs.get(Integer.parseInt(message[message.length-1]));
        	pda.id = Integer.parseInt(message[message.length-1]);
        	pda.cursorX = Integer.parseInt(message[1]);
        	pda.cursorY = Integer.parseInt(message[2]);
        	pda.lineNumber = Integer.parseInt(message[3]);
        	pda.lineLength = Integer.parseInt(message[4]);
        	pda.receiver = Integer.parseInt(message[5]);
        	pda.offX = Integer.parseInt(message[6]);
        	pda.offY = Integer.parseInt(message[7]);
        	pda.textureX = Integer.parseInt(message[8]);
        	pda.textureY = Integer.parseInt(message[9]);
        	pda.texture = message[10];
        	for (int i = 0; i < pda.lineNumber; i++) {
        		pda.lines.put(i, message[11+i]);
        	}
        	CCPortable.allPDAs.put(Integer.parseInt(message[2]),pda);
        }
	}
}
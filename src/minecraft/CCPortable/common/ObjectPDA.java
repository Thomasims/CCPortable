package CCPortable.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.ModLoader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import dan200.computer.api.IComputerAccess;

public class ObjectPDA {
    public Map lines = new HashMap();
    public int cursorX;
    public int cursorY;
    public int lineNumber;
    public int lineLength;
    public int receiver;
    public int id;
    public int offX;
    public int offY;
    public int textureX;
    public int textureY;
    public IComputerAccess computer;
    public String texture;

    public ObjectPDA(String texture, int sizeX, int sizeY, int textureX, int textureY, int offX, int offY) {
    	this.texture = texture;
    	this.lineNumber = sizeY;
    	this.lineLength = sizeX;
    	this.offX = offX;
    	this.offY = offY;
    	this.textureX = textureX;
    	this.textureY = textureY;
        this.cursorX = 1;
        this.cursorY = 1;
        for (int i = 0; i < lineNumber; i++) {
            this.lines.put(i, "");
        }
        this.computer = null;
        this.texture = "/CCPortable/pda.png";
        this.sendAll();
    }

    public void setCursorPos(int x, int y, boolean fl) {
        this.cursorX = x;
        this.cursorY = y;
        if (fl) {
        	this.sendChanges("CPOS:"+x+":"+y+":"+this.id);
        }
    }

    public void write(String text, boolean fl) {
        String line = (String) this.lines.get(this.cursorY);
        String finals = "";
        for (int i = 0; i < this.cursorX; i++) {
        	try {
        		finals = finals + line.charAt(i);
        	} catch(Exception e) { finals = finals + " ";}
        }
        finals = finals + text;
        for (int j = finals.length(); j < this.lineLength; j++) {
        	try {
        		if (j < line.length()) {
        			finals = finals + line.charAt(j);
        		}
        	} catch(Exception e) {finals = finals + " ";}
        }
        String ffinal = "";
        for (int k = 0; k < this.lineLength; k++) {
        	try {
        		if (k < finals.length()) {
        			ffinal = ffinal + finals.charAt(k);
        		}
        	} catch(Exception e) {ffinal = ffinal + " ";}
        }
        this.lines.put(this.cursorY, ffinal);
        if (fl) {
        	this.sendChanges("WRIT:"+ffinal+":"+this.id);
        }
    }

    public void linkComputer(IComputerAccess cp) {
        this.computer = cp;
    }

    public void doEvent(String event, Object[] args) {
        if (this.computer != null) {
            this.computer.queueEvent(event,args);
        }
    }

    public void setReceiver(int id) {
        this.receiver = id;
        if (CCPortable.allReceivers.get(id) != null) {
        	TileEntityPDA rec = (TileEntityPDA) CCPortable.allReceivers.get(id);
            this.linkComputer(rec.computer);
        }
    }
    
    public void sendAll() {
    	String all = "ALL:";
    	all += this.cursorX + ":";
    	all += this.cursorY + ":";
    	all += this.lineNumber + ":";
    	all += this.lineLength + ":";
    	all += this.receiver + ":";
    	all += this.offX + ":";
    	all += this.offY + ":";
    	all += this.textureX + ":";
    	all += this.textureY + ":";
    	all += this.texture + ":";
    	for (int i = 0; i < this.lineNumber; i++) {
    		all += (String) this.lines.get(i) + ":";
    	}
    	all += this.id;
    	this.sendChanges(all);
    }
    
    private void sendChanges(String s) {
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
    	if (side == Side.SERVER) {
    		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
    		DataOutputStream outputStream = new DataOutputStream(bos);
    		try {
    		        outputStream.writeChars(s);
    		} catch (Exception ex) {
    		        ex.printStackTrace();
    		}

    		Packet250CustomPayload packet = new Packet250CustomPayload();
    		packet.channel = "CCPortable";
    		packet.data = bos.toByteArray();
    		packet.length = bos.size();
    		Player ply = (Player) ModLoader.getMinecraftInstance().theWorld.getPlayerEntityByName(((EntityPlayer) CCPortable.allPLYs.get(this.id)).username);
    		if (ply != null) {
    			PacketDispatcher.sendPacketToPlayer(packet, (Player) CCPortable.allPLYs.get(this.id));
    		}
    	} else if (side == Side.CLIENT) {
    		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
    		DataOutputStream outputStream = new DataOutputStream(bos);
    		try {
    		        outputStream.writeChars(s);
    		} catch (Exception ex) {
    		        ex.printStackTrace();
    		}

    		Packet250CustomPayload packet = new Packet250CustomPayload();
    		packet.channel = "CCPortable";
    		packet.data = bos.toByteArray();
    		packet.length = bos.size();
    		PacketDispatcher.sendPacketToServer(packet);
    	}
    }
}
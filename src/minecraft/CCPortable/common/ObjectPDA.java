package CCPortable.common;

import java.util.HashMap;
import java.util.Map;
import dan200.computer.api.IComputerAccess;

public class ObjectPDA {
    public Map lines = new HashMap();
    public int cursorX;
    public int cursorY;
    public int lineNumber;
    public int lineLength;
    public int receiver;
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
            this.lines.put(i, "hello");
        }
        this.computer = null;
        this.texture = "/CCPortable/pda.png";
    }

    public void setCursorPos(int x, int y) {
        this.cursorX = x;
        this.cursorY = y;
    }

    public void write(String text) {
        String line = (String) this.lines.get(this.cursorY);
        String finals = "";
        for (int i = 0; i < this.cursorX; i++) {
            finals = finals + line.charAt(i);
        }
        finals = finals + text;
        for (int j = finals.length(); j < this.lineLength; j++) {
            finals = finals + line.charAt(j);
        }
        String ffinal = "";
        for (int k = 0; k < this.lineLength; k++) {
            ffinal = ffinal + finals.charAt(k);
        }
        this.lines.put(this.cursorY, ffinal);
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
}
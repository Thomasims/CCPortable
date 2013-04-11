package CCPortable.common;

import java.util.HashMap;
import java.util.Map;
import dan200.computer.api.IComputerAccess;

public class PDAFrequency {
    public Map lines = new HashMap();
    public Map events = new HashMap();
    public Map pdaevents = new HashMap();
    public int cursorX;
    public int cursorY;
    public int frequency;

    public PDAFrequency(int freq) {
        this.frequency = freq;
        this.cursorX = 1;
        this.cursorY = 1;
        for (int i = 0; i < 40; i++) {
            this.lines.put(i, "");
        }
    }

    public void setCursorPos(int x, int y) {
        this.cursorX = x;
        this.cursorY = y;
    }

    public void write(String text) {
        String line = (String) this.lines.get(this.cursorY);
        String finals = "";
        for (int i = 0; i < 23; i++) {
            finals = finals + line.charAt(i);
        }
        finals = finals + text;
        for (int j = finals.length(); j < 60; j++) {
            finals = finals + line.charAt(j);
        }
        String ffinal = "";
        for (int k = 0; k < 60; k++) {
            ffinal = ffinal + finals.charAt(k);
        }
        this.lines.put(this.cursorY, ffinal);
    }

    public void doEvent(String event, Object[] args) {
        Object[] fargs = new Object[args.length+1];
        fargs[0] = event;
        for(int i = 1; i < fargs.length; i++) {
            fargs[i] = args[i-1];
        }
        int pos = 0;
        for(int i = 0; i < 80; i++) {
            if(this.events.get(i) == null) {
                pos = i;
                break;
            }
        }
        this.events.put(pos, fargs);
    }

    public Object[] getEvent() {
        Object[] returned = (Object[]) this.events.get(0);
        this.events.put(0,null);
        for (int i = 0; i < 80; i++) {
            if (this.events.get(i) == null) {
                for(int j = 0; j < 3; j++) {
                    if (this.events.get(i+j) != null) {
                        this.events.put(i,this.events.get(i+j));
                    }
                }
            }
        }
        return returned;
    }

    public void doItemEvent(String event, Object[] args) {
        Object[] fargs = new Object[args.length+1];
        fargs[0] = event;
        for(int i = 1; i < fargs.length; i++) {
            fargs[i] = args[i-1];
        }
        int pos = 0;
        for(int i = 0; i < 80; i++) {
            if(this.events.get(i) == null) {
                pos = i;
                break;
            }
        }
        this.events.put(pos, fargs);
    }

    public Object[] getItemEvent() {
        Object[] returned = (Object[]) this.events.get(0);
        this.events.put(0,null);
        for (int i = 0; i < 80; i++) {
            if (this.events.get(i) == null) {
                for(int j = 0; j < 3; j++) {
                    if (this.events.get(i+j) != null) {
                        this.events.put(i,this.events.get(i+j));
                    }
                }
            }
        }
        return returned;
    }
}
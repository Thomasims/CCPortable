package CCPortable.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileEntityPDA extends TileEntity implements IPeripheral
{
    public IComputerAccess computer;
    public String channel;
    public int count = 0;
    public NBTTagCompound[] tagList;
	private BlockReceiver block;
	public int id;


    public TileEntityPDA(BlockReceiver blockReceiver, int id) {
		this.block = blockReceiver;
		this.id = id;
	}

	public String getType()
    {
        return "receiver";
    }

    public String[] getMethodNames()
    {
        String[] methods = { "alert", "getSize", "setTitle", "setIcon", "write", "setChannel", "setCursorPos" };
        return methods;
        //ME WANT TERM FUNCTIONS
    }

    public Object[] callMethod(IComputerAccess computer, int method, Object[] arg) throws Exception
    {
    	/*
        try
        {
        	NBTTagCompound nbt;
			if ((method == 0) && (arg.length == 2) && (checkPDA((Integer) arg[1])) && ((arg[0] instanceof String)) && ((arg[1] instanceof Double))) {
                nbt = loadNBTFromList((Integer) arg[1]);
                tagList[nbt.getInteger("ID")].setString("Alert",(String) arg[0]);
        	}
            if (method == 1)
                return new Object[] { tagList.length };
            if ((method == 2) && (arg.length == 2) && (PDA.loadPDA((Integer) arg[1]) != null) && ((arg[0] instanceof String)) && ((arg[1] instanceof Double)))
                PDA.loadPDA((Integer) arg[1]).title = ((String)arg[0]);
            if ((method == 3) && (arg.length == 2) && (PDA.loadPDA((Integer) arg[1]) != null) && ((arg[0] instanceof Double)) && ((arg[1] instanceof Double)))
                PDA.loadPDA((Integer) arg[1]).icon = (Integer) arg[0];
            if ((method == 4) && (arg.length == 2) && (PDA.loadPDA((Integer) arg[1]) != null) && ((arg[0] instanceof String)) && ((arg[1] instanceof Double)))
                PDA.loadPDA((Integer) arg[1]).setText((String)arg[0]);
            if ((method == 5) && (arg.length == 1) && ((arg[0] instanceof String)))
                this.channel = ((String)arg[0]);
            if ((method == 6) && (arg.length == 3) && (PDA.loadPDA(DtI(arg[2])) != null) && ((arg[0] instanceof Double)) && ((arg[1] instanceof Double)) && ((arg[2] instanceof Double)))
                PDA.loadPDA(DtI(arg[2])).setCursorPos(DtI(arg[0]), DtI(arg[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return null;
    }

    public boolean canAttachToSide(int side)
    {
        return block.getSide() == side;
    }

    public void attach(IComputerAccess acomputer, String computerSide)
    {
        this.computer = acomputer;
        this.computer.mountFixedDir("rom/apis/receiver", "mods/CCPortable/receiver.lua", true);
    }

    public void detach(IComputerAccess computer)
    {
        computer.unmount("rom/apis/receiver");
        this.computer = null;
    }
    public void addPDAToList(NBTTagCompound nbt)
    {
        int pdaID = nbt.getInteger("ID");
        tagList[pdaID] = nbt;
    }
    public boolean checkPDA(int ID)
    {
        if (tagList[ID] != null)
            return true;
        return false;
    }
    public NBTTagCompound loadNBTFromList(int ID) 
    {
        if (checkPDA(ID))
            return tagList[ID];
        return null;
    }
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.channel = nbt.getString("Channel");
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setString("Channel", channel);
    }
}
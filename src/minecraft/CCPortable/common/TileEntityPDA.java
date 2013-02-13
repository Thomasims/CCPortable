package CCPortable.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileEntityPDA extends TileEntity implements IPeripheral {
	public IComputerAccess computer;
	public int id;
	public int pda;
	public int side = -1;

	// public static World world;

	@Override
	public String getType() {
		return "PDA_receiver";
	}

	@Override
	public String[] getMethodNames() {
		String[] methods = { "alert", "write", "setCursorPos" };
		return methods;
		// ME WANT TERM FUNCTIONS
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method,
			Object[] arg) throws Exception {

		EntityPlayer ply = (EntityPlayer) CCPortable.allPLYs.get(this.pda);
		ObjectPDA pda = (ObjectPDA) CCPortable.allPDAs.get(this.pda);
		switch (method) {
		case 0:
			if (arg.length == 2 && arg[0] instanceof Double && arg[1] instanceof Double)
				this.worldObj.addBlockEvent((int) ply.posX, (int) ply.posY,
						(int) ply.posZ, Block.music.blockID, (Integer) arg[0],
						(Integer) arg[1]);
			else
				throw new Exception("Invalid arguments to function");
		case 1:
			if (arg.length == 1 && arg[0] instanceof String) {
				pda.write((String) arg[0]);
				CCPortable.allPDAs.put(this.pda, pda);
			} else {
				throw new Exception("Invalid arguments to function");
			}
		case 2:
			if (arg.length == 2 && arg[0] instanceof Double && arg[1] instanceof Double) {
				pda.setCursorPos((Integer) arg[0], (Integer) arg[1]);
				CCPortable.allPDAs.put(this.pda, pda);
			} else {
				throw new Exception("Invalid arguments to function");
			}
		}
		return null;
	}

	@Override
	public boolean canAttachToSide(int sidee) {
		if (this.side == -1) {
			this.side = sidee;
			return true;
		}
		return this.side == sidee;
	}

	@Override
	public void attach(IComputerAccess acomputer) {
		/*this.computer = acomputer;
		this.computer.mountFixedDir("rom/apis/receiver",
				"mods/CCPortable/receiver.lua", true, 0);*/
	}

	@Override
	public void detach(IComputerAccess computer) {
		/*this.computer.unmount("rom/apis/receiver");
		this.computer = null;
		this.side = -1;*/
	}

	public int getID() {
		return this.id;
	}

	public void setPDA(int id) {
		this.pda = id;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.id = nbt.getInteger("ID");
		this.pda = nbt.getInteger("PID");
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("ID", this.id);
		nbt.setInteger("PID", this.pda);
	}
}
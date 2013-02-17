package CCPortable.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import net.minecraft.src.ModLoader;

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
		String[] methods = { "alert", "write", "setCursorPos", "getCursorPos", "getSize", "clear", "clearLine", "setTextColor", "steBackgroundColor", "isColor" };
		return methods;
		// ME WANT TERM FUNCTIONS
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method,
			Object[] arg) throws Exception {

		try {
		String ply = (String) CCPortable.allPLYs.get(this.pda);
		ObjectPDA pda = (ObjectPDA) CCPortable.allPDAs.get(this.pda);
		switch (method) {
		case 0:
			if (arg.length == 2 && arg[0] instanceof Double && arg[1] instanceof Double)
				this.worldObj.playSoundAtEntity(ModLoader.getMinecraftInstance().theWorld.getPlayerEntityByName(ply), "note.pling", ((Double) arg[0]).intValue(),
						((Double) arg[1]).intValue());
			else
				throw new Exception("Invalid arguments to function");
			break;
		case 1:
			if (arg[0] instanceof String) {
				pda.write((String) arg[0], true);
				CCPortable.allPDAs.put(this.pda, pda);
			} else {
				throw new Exception("Argument 1 is not a string!");
			}
			break;
		case 2:
			if (arg.length == 2 && arg[0] instanceof Double && arg[1] instanceof Double) {
				pda.setCursorPos(((Double) arg[0]).intValue(), ((Double) arg[1]).intValue(), true);
				CCPortable.allPDAs.put(this.pda, pda);
			} else {
				throw new Exception("Invalid arguments to function");
			}
			break;
		case 3:
		case 4:
		case 5:
			for (int i = 0; i < pda.lineNumber; i++) {
				pda.lines.put(i, "");
			}
			pda.sendAll();
			CCPortable.allPDAs.put(this.pda, pda);
		case 6:
			pda.lines.put(pda.cursorY, "");
			pda.sendAll();
			CCPortable.allPDAs.put(this.pda, pda);
		case 7:
		case 8:
		case 9:
		}
		} catch (Exception e) {e.printStackTrace();}
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
		this.computer = acomputer;
	}

	@Override
	public void detach(IComputerAccess computer) {
		this.computer = null;
		this.side = -1;
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
package CCPortable.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public class PDAPeripheral implements IHostedPeripheral {
	public IComputerAccess computer;
	public int id;
	public int pda;
	public int side = -1;
	
	@Override
	public String getType() {
		return "receiver";
	}

	@Override
	public String[] getMethodNames() {
		String[] methods = { "alert", "write", "setCursorPos" };
		return methods;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method,
			Object[] arg) throws Exception {
		try {
			EntityPlayer ply = (EntityPlayer) CCPortable.allPLYs.get(this.pda);
			ObjectPDA pda = (ObjectPDA) CCPortable.allPDAs.get(this.pda);
			switch(method) {
			case 0:
				//TileEntityPDA.world.addBlockEvent((int) ply.posX, (int) ply.posY, (int) ply.posZ, Block.music.blockID, (Integer) arg[0], (Integer) arg[1]);
			case 1:
				pda.write((String) arg[0]);
				CCPortable.allPDAs.put(this.pda, pda);
			case 2:
				pda.setCursorPos((Integer) arg[0], (Integer) arg[1]);
				CCPortable.allPDAs.put(this.pda, pda);
			}
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public boolean canAttachToSide(int sidee) {
		if (this.side == -1){
			this.side = sidee;
			return true;
		}
		return this.side == sidee;
	}

	@Override
	public void attach(IComputerAccess acomputer) {
		this.computer = acomputer;
		this.computer.mountFixedDir("rom/apis/receiver", "mods/CCPortable/receiver.lua", true, 0);
	}

	@Override
	public void detach(IComputerAccess computer) {
		this.computer.unmount("rom/apis/receiver");
		this.computer = null;
		this.side = -1;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.id = nbt.getInteger("ID");
		this.pda = nbt.getInteger("PID");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("ID", this.id);
		nbt.setInteger("PID", this.pda);
	}

}

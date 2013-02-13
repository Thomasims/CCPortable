package CCPortable.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import CCPortable.client.GuiPDA;
import dan200.computer.api.IComputerAccess;

public class ItemPDA extends Item {

	private int checkCoolDown = 0;
	public ItemPDA(int i) {
		super(i);
		maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	public String getTextureFile() {
		return "/CCPortable/Textures.png";
	}

	public void onCreated(ItemStack iS, World world, EntityPlayer player) {
	    int id = CCPortable.createPDA(new ObjectPDA("/CCPortable/pda.png", 23, 21, 187, 256, 9, 10), player);
	    NBTTagCompound nbt = new NBTTagCompound();
	    nbt.setInteger("RID", 0);
	    nbt.setInteger("ID", id);
	    iS.setTagCompound(nbt);
	}

	public boolean onItemUseFirst(ItemStack iS, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)  {
		if (iS.getTagCompound() == null)
		    return false;
		NBTTagCompound nbt = iS.getTagCompound();
		int id = nbt.getInteger("ID");
		if (world.getBlockId(x, y, z) == CCPortable.ReceiverID) {
			System.out.println("clicked");
			if (world.isRemote) {
				return true;
			}

			TileEntityPDA tePDA = (TileEntityPDA) world.getBlockTileEntity(x, y, z);
			if (tePDA != null) {
			    ObjectPDA pda = (ObjectPDA) CCPortable.allPDAs.get(id);
			    pda.setReceiver(tePDA.getID());
			    CCPortable.allPDAs.put(id, pda);
			    int rid = tePDA.getID();
			    tePDA.setPDA(id);
			    world.setBlockTileEntity(x, y, z, tePDA);
			}
		}
		this.openGUI(id, player);
		return true;
	}
	
	public void openGUI(int id, EntityPlayer player) {
		FMLCommonHandler.instance().showGuiScreen(new GuiPDA(id, player));
	}
	
	public void onUpdate(ItemStack iS, World world, Entity ply, int par4, boolean par5) {
		this.checkCoolDown++;
		if (this.checkCoolDown > 100) {
			try {
			if(iS.getTagCompound() == null) {
				int id = CCPortable.createPDA(new ObjectPDA("/CCPortable/pda.png", 23, 21, 187, 256, 9, 10), (EntityPlayer) ply);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("ID", id);
				nbt.setInteger("RID",  0);
				iS.setTagCompound(nbt);
			}
			
			NBTTagCompound nbt = iS.getTagCompound();
			ObjectPDA toU = (ObjectPDA) CCPortable.allPDAs.get(nbt.getInteger("ID"));
			if (CCPortable.allPDAs.get(nbt.getInteger("ID")) == null) {
				ObjectPDA pda = new ObjectPDA("/CCPortable/pda.png", 23, 21, 187, 256, 9, 10);
				pda.receiver = nbt.getInteger("RID");
				CCPortable.allPDAs.put(nbt.getInteger("ID"), pda);
			}
			int rID = toU.receiver;
			nbt.setInteger("RID", rID);
			} catch (Exception e) {
				System.out.println("PDA bugged");
			} finally {
				this.checkCoolDown = 0;
			}
		}
	}
}

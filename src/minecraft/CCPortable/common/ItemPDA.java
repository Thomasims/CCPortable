package CCPortable.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

	public boolean ddouble;
	public int freq;
	
	public ItemPDA(int i, boolean dd) {
		super(i);
		ddouble = dd;
		maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	public String getTextureFile() {
		return "/CCPortable/Textures.png";
	}

	public void onCreated(ItemStack iS, World world, EntityPlayer player) {
	    NBTTagCompound nbt = new NBTTagCompound();
	    nbt.setInteger("Frequency", freq);
	    iS.setTagCompound(nbt);
	}

	public boolean onItemUseFirst(ItemStack iS, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)  {
		if (iS.getTagCompound() == null)
		    return false;
		NBTTagCompound nbt = iS.getTagCompound();
		int id = nbt.getInteger("Frequency");
		if (world.getBlockId(x, y, z) == CCPortable.ReceiverID) {
			System.out.println("clicked");
			if (world.isRemote) {
				return true;
			}

			TileEntityPDA tePDA = (TileEntityPDA) world.getBlockTileEntity(x, y, z);
			if (tePDA != null) {
			    this.freq = tePDA.frequ;
			}
		}
		if (world.isRemote) {
			this.openGUI(id, iS);
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void openGUI(int id, ItemStack iS) {
		FMLCommonHandler.instance().showGuiScreen(new GuiPDA(id, iS));
	}
}

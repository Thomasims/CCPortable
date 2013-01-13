package CCPortable.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import CCPortable.client.GuiPDA;
import dan200.computer.api.IComputerAccess;

public class ItemPDA extends Item {
	public int ID = 0;
	protected Minecraft mc;
	private IComputerAccess computer;
	private int pdaID;
	private String[] lines = new String[23];
	private EntityPlayer player;

	public ItemPDA(int i) {
		super(i);
		maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabTools);
		this.pdaID = i;
	}

	public String getTextureFile() {
		return "/CCPortable/Textures.png";
	}

	public boolean getShareTag() {
		return true;
	}

	public int getUID() {
		ID++;
		return ID;
	}

	public void onCreated(ItemStack iS, World world, EntityPlayer player) {
		NBTTagCompound nbt = new NBTTagCompound();
		int pdaUID = getUID();
		CCPortable.savePDA(true, pdaUID, iS);
		nbt.setInteger("ID", pdaUID);
		nbt.setInteger("RID", 0);
		nbt.setString("Channel", "N/A");
		nbt.setString("Title", "N/A");
		nbt.setInteger("Icon", 0);
		nbt.setString("alert", "No new alert.");
		NBTTagList var2 = new NBTTagList();
		for (int var3 = 1; var3 <= 22; var3++) {
			NBTTagCompound var4 = new NBTTagCompound();
			if (lines[var3] != null && lines[var3] != "") {
				var4.setString("Line" + var3, lines[var3]);
				var2.appendTag(var4);
			} else {
				var4.setString("Line" + var3, "");
				var2.appendTag(var4);
			}
		}
		nbt.setTag("Lines", var2);
		iS.setTagCompound(nbt);
		this.player = player;
	}

	public ItemStack onItemRightClick(ItemStack iS, World world, EntityPlayer player) {
		int x = (int) player.posX;
		int y = (int) (player.posY - 1);
		int z = (int) player.posZ;
		NBTTagCompound nbt = iS.getTagCompound();
		if (world.getBlockId(x, y, z) == CCPortable.ReceiverID) {
			if (world.isRemote) {
				return iS;
			}

			TileEntityPDA tePDA = (TileEntityPDA) world.getBlockTileEntity(x, y, z);
			if (tePDA.computer != null) {
				nbt.setString("Channel", tePDA.channel);
				tePDA.addPDAToList(nbt);
				nbt.setInteger("RID", tePDA.id);
				this.computer = tePDA.computer;
				System.out.println("PDA connected");
				return iS;
			}
		}
		GuiPDA gui = new GuiPDA(iS, player);
		FMLCommonHandler.instance().showGuiScreen(gui);
		return iS;
	}

	public void doEvent(String event, Object[] args) {
		try {
			if (this.computer == null)
				return;
			this.computer.queueEvent(event, args);
		} catch (RuntimeException e) {

		}
	}
}

package CCPortable.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import net.minecraftforge.common.IArmorTextureProvider;

import org.lwjgl.opengl.GL11;

import CCPortable.client.FixedWidthFontRenderer;
import dan200.computer.api.IComputerAccess;

public class ItemPDAHelmet extends ItemArmor implements IArmorTextureProvider
{
    private NBTTagCompound thePDA;
	private int ID;
	private String[] lines;
	private IComputerAccess computer;

	public ItemPDAHelmet(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4)
    {
	super(par1, par2EnumArmorMaterial, par3, par4);
    }

    public String getTextureFile()
    {
	return "/CCPortable/Textures.png";	   
    }

    public String getArmorTextureFile(ItemStack par1)
    {
	if (par1.itemID == CCPortable.PDAHelmetID)
	{
	    return "/CCPortable/PDAHelmet.png";
	}
	return "/armor/gold_1.png";
    }

    public boolean OnTickInGame(Minecraft minecraft, float par1, boolean par2, int par3, int par4)
    {
	if(minecraft == null)
	    minecraft = ModLoader.getMinecraftInstance();
	if(minecraft.getMinecraft().thePlayer == null || minecraft.getMinecraft().theWorld == null)
	    return false;
	EntityClientPlayerMP player = minecraft.getMinecraft().thePlayer;
	ItemStack iS = player.inventory.armorItemInSlot(3);
	if (minecraft.gameSettings.thirdPersonView == 0
	    && player.inventory.armorItemInSlot(3).itemID == CCPortable.PDAHelmetID)
	{
	    this.renderHelmetVisor(minecraft, "/CCPortable/PDABlur.png", iS);
	}
	return true;
    }

    private void renderHelmetVisor(Minecraft mc, String s, ItemStack iS)
    {
	this.thePDA = iS.getTagCompound();
	ScaledResolution res = new ScaledResolution(
	    mc.getMinecraft().gameSettings,
	    mc.getMinecraft().displayWidth,
	    mc.getMinecraft().displayHeight
	);
	int i = res.getScaledWidth();
	int j = res.getScaledHeight();
	GL11.glDisable(2929 /*Gl_Depth_TEST*/);
	GL11.glDepthMask(false);
	GL11.glBlendFunc(770, 771);
	GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
	GL11.glDisable(3008);
	GL11.glBindTexture(3553, mc.getMinecraft().renderEngine.getTexture(s));
	Tessellator tessellator = Tessellator.instance;
	tessellator.startDrawingQuads();
	tessellator.addVertexWithUV(0.0D, j, -90D, 0.0D, 1.0D);
	tessellator.addVertexWithUV(i, j, -90D, 1.0D, 1.0D);
	tessellator.addVertexWithUV(i, 0.0D, -90D, 1.0D, 0.0D);
	tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
	tessellator.draw();
	FixedWidthFontRenderer fontRenderer = new FixedWidthFontRenderer(mc.gameSettings, "/font/default.png", mc.renderEngine);
	String guiTitle = "";
	if ((this.thePDA.getString("Title") == null) || (this.thePDA.getString("Title") == ""))
	    guiTitle = "No title.";
	else
	    guiTitle = this.thePDA.getString("Title");
	int k = 0;
	fontRenderer.drawString(guiTitle, i + 10, k + 10, 0xffffff);
	int c = 0;
	for (int i1 = 0; i1<=21; i1++) {
    	NBTTagList var2 = this.thePDA.getTagList("Lines");
    	NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(i1);
        String s1 = var4.getString("Line"+i1);
        fontRenderer.drawString(s1, i1 + 12, c, 0xffffff);
        c += 10;
    }
	GL11.glDepthMask(true);
	GL11.glEnable(2929);
	GL11.glEnable(3008);
	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean getShareTag()
    {
	return true;
    }

    public int getUID()
    {
	ID++;
	return ID;
    }

    public void onCreated(ItemStack iS, World world, EntityPlayer player)
    {
	NBTTagCompound nbt = iS.getTagCompound();
	int pdaUID = getUID();
	CCPortable.savePDA(true, pdaUID, iS);
	nbt.setInteger("ID",pdaUID);
	nbt.setString("Channel","N/A");
	nbt.setInteger("Icon",0);
	nbt.setString("alert","No new alert.");
	NBTTagList var2 = new NBTTagList();
	for (int var3 = 1; var3 <= 21; var3++)
	{
	    NBTTagCompound var4 = new NBTTagCompound();
	    var4.setString("Line"+var3, lines[var3]);
	    var2.appendTag(var4);
	}
	nbt.setTag("Lines", var2);
    }

    public boolean onItemUse(ItemStack iS, EntityPlayer player, World world, int x, int y, int z, int par7)
    {
	NBTTagCompound nbt = iS.getTagCompound();
	if (world.getBlockId(x,y,z) == CCPortable.ReceiverID)
	{
	    if (world.isRemote)
	    {
		return true;
	    }

	    TileEntityPDA tePDA = (TileEntityPDA)world.getBlockTileEntity(x, y, z);
	    if (tePDA.computer == null)
	    {
		return false;
	    }
	    nbt.setString("Channel",tePDA.channel);
	    tePDA.addPDAToList(nbt);
	    this.computer = tePDA.computer;
	    /*Say to player that the pda is linked*/
	    return true;
	}
	return false;
    }

    public void doEvent(String event, Object[] args)
    {
	try {
	    if (this.computer == null)
		return;
	    this.computer.queueEvent(event, args);
	}
	catch (RuntimeException e) {
	    
	}
    }
}
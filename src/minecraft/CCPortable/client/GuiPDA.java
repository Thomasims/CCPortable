package CCPortable.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.lwjgl.opengl.GL11;

import CCPortable.common.CCPortable;

public class GuiPDA extends GuiScreen
{
    private NBTTagCompound thePDA;
    private Minecraft mc;
	private EntityPlayer player;
	private ItemStack itemStack;

    public GuiPDA(ItemStack iS, EntityPlayer player)
    {
        NBTTagCompound nbt = iS.getTagCompound();
        this.thePDA = nbt;
        this.player = player;
        this.itemStack = iS;
    }

    public void initGui()
    {
        controlList.clear();
        int i = (width - 187) / 2;
        int k = (height - 256) / 2;
        controlList.add(new GuiButton(2, i + 35, k + 239, 60, 14, "Disconnect"));

        //this.itemStack.doEvent("pda_open", new Object[] { this.thePDA.id });
    }

    protected void mouseClicked(int x, int y, int par3)
    {
        int i = (width - 163) / 2;
        int k = (height - 244) / 2;
        if(x > i && x < ((width + 163) / 2) && y > k && y < ((height + 244) / 2)) {
            int charX = (int) ((double) ((x-i)/163*27)+1);
            int charY = (int) ((double) ((y-k)/244*21)+1);
            //this.thePDA.doEvent("pda_touch", new Object[] { this.thePDA.id, charX, charY });
        }
    }

    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            default:
                break;
            case 1:
                this.thePDA.setString("Channel","N/A");
                this.mc.displayGuiScreen(null);
                CCPortable.getReceiver(thePDA.getInteger("RID")).tagList[thePDA.getInteger("ID")] = null;
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
        int i = this.mc.renderEngine.getTexture("/CCPortable/pda.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (width - 187) / 2;
        int k = (height - 256) / 2;
        drawTexturedModalRect(j, k, 0, 0, 187, 256);
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        int re = mc.renderEngine.getTexture("/CCPortable/PDA.png");
        mc.renderEngine.bindTexture(re);
        int i = (width - 187) / 2;
        int k = (height - 256) / 2;
        drawTexturedModalRect(i,k,0,0,187,256);
        if (this.thePDA.getInteger("Icon") > 0) {
            drawTexturedModalRect(i + 164, k + 8, 187, 10 * (this.thePDA.getInteger("Icon") - 1), 14, 10);
        }
        if (this.thePDA.getInteger("Charge") > 0) {
            int chargeIcon = (int)(this.thePDA.getInteger("Charge")/1000);
            drawTexturedModalRect(i + 150, k + 8, 201, 10 * (chargeIcon - 1), 14, 10);
        }
        String guiTitle = "";
        if ((this.thePDA.getString("Title") == null) || (this.thePDA.getString("Title") == ""))
            guiTitle = "No title.";
        else
            guiTitle = this.thePDA.getString("Title");
        drawString(fontRenderer, guiTitle, i + 10, k + 10, 0xffffff);
        int c = k + 24;
        FixedWidthFontRenderer fontRenderer = new FixedWidthFontRenderer(mc.gameSettings, "/font/default.png", mc.renderEngine);
        for (int i1 = 0; i1<=21; i1++) {
        	NBTTagList var2 = this.thePDA.getTagList("Lines");
        	NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(i1);
            String s = var4.getString("Line"+i1);
            fontRenderer.drawString(s, i1 + 12, c, 0xffffff);
            c += 10;
        }
        super.drawScreen(par1, par2, par3);
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        /*
        if (xn.a(par1)) {
            this.thePDA.doEvent("pda_char", new Object[] { this.thePDA.id, par1 });
            this.thePDA.doEvent("pda_key", new Object[] { this.thePDA.id, par2 });
    	}
    	*/
        if(par2 == 1)
        {
            mc.displayGuiScreen(null);
        }
    }
}
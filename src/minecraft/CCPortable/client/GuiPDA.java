package CCPortable.client;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.src.ModLoader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.*;

import CCPortable.common.CCPortable;
import CCPortable.common.ObjectPDA;

@SideOnly(Side.CLIENT)
public class GuiPDA extends GuiScreen {
	protected int startXPos;
	protected int startYPos;
	protected ObjectPDA pda;
	protected EntityPlayer player;
	protected Map lineBuffer = new HashMap();
	private int id;

	public GuiPDA(int id, EntityPlayer ply) {
		this.id = id;
		this.player = ply;
		this.pda = (ObjectPDA) CCPortable.allPDAs.get(id);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
		controlList.clear();
		int i = 0;
		int k = 0;
		try {
			i = width / 2 - (this.pda.textureX / 2);
			k = height / 2 - (this.pda.textureY / 2);
		} catch (Exception e) {}
		controlList
				.add(new GuiButton(2, i + 35, k + 239, 60, 14, "Disconnect"));
		this.startXPos = i;
		this.startYPos = k;
		this.doEvent("pda_open", new Object[] {});
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		super.drawScreen(par1, par2, par3);

		String texture = "";
		try {
			texture = this.pda.texture;
		} catch (Exception e) {
		}
		this.mc.renderEngine.bindTexture(this.mc.renderEngine
				.getTexture(texture));
		this.drawTexturedModalRect(this.startXPos, this.startYPos, 0, 0, 187,
				256);
		if (!this.checkChanges()) {
			for (int i = 0; i < this.pda.lineNumber; i++) {
				String line = (String) this.lineBuffer.get(i);
				int startX = this.startXPos + this.pda.offX;
				int startY = this.startYPos + this.pda.offY + i * 10;
				this.fontRenderer.drawString(line, startX, startY, 0xFFFFFF);
			}
		}
	}

	/**
	 * Called when the mouse is clicked. par1 - x par2 - y par3 - button
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		System.out.println(par1 + ";" + par2 + ";" + par3);
		this.doEvent("mouse_click", new Object[] { par1, par2, par3 });
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			this.mc.thePlayer.closeScreen();
		}
		this.doEvent("key", new Object[] { par2 });
		this.doEvent("char", new Object[] { par1 });
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat
	 * events
	 */
	public void onGuiClosed() {
		this.doEvent("pda_closed", new Object[] {});
	}

	public void doEvent(String name, Object[] args) {
		try {
			int rID = this.pda.receiver;
			Object receiver = CCPortable.allReceivers.get(rID);
			if (receiver != null) {
				CCPortable.doEvent(rID, name, args);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	public boolean checkChanges() {
		Map nbtl = new HashMap();
		int num = 0;
		try {
			nbtl = ((ObjectPDA) CCPortable.allPDAs.get(this.id)).lines;
			num = this.pda.lineNumber;
		} catch (Exception e) {}
		boolean right = false;
		for (int i = 0; i < num; i++) {
			try {
				String line = (String) nbtl.get(i);
				if (this.lineBuffer.get(i) != line) {
					this.lineBuffer.put(i, line);
					right = true;
				}
			} catch (Exception e) {}
		}
		return right;
	}
}
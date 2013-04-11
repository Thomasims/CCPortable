package CCPortable.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import CCPortable.common.pdaParts.ItemPDABattery;
import CCPortable.common.pdaParts.ItemPDAScreen;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.*;

@Mod(modid = "CCPortable", name = "CCPortable", version = "0.3", dependencies = "required-after:ComputerCraft;after:CCTurtle")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"CCPortable" }, packetHandler = PacketHandler.class)
public class CCPortable {
	public static Map allFreq = new HashMap();
	public static int lastPDA = 0;
	public static int lastREC = 0;
	static EnumToolMaterial pdaMaterial = EnumHelper.addToolMaterial("pdaMaterial", 0, -1, 2F, 2, 14);
	public static Block receiverBlock;
	public static Item pdaItem;
	public static Item touchScreen;
	public static Item pdaBattery;
	public static Item pdaHelmet;
	public static int PDABatteryID;
	public static int PDAHelmetID;
	public static int ReceiverID;
	public static int PDAID;
	public static int TouchScreenID;
	@Instance
	public static CCPortable instance;
	@SidedProxy(clientSide = "CCPortable.client.ClientProxy", serverSide = "CCPortable.common.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();
		ReceiverID = config.getBlock("Receiver ID", 145).getInt();
		PDAID = config.getItem("Item PDA ID", 550).getInt();
		TouchScreenID = config.getItem("TouchScreen ID", 552).getInt();
		PDABatteryID = config.getItem("Battery ID", 554).getInt();
		PDAHelmetID = config.getItem("Helmet ID", 556).getInt();
		config.save();
	}

	@Init
	public void load(FMLInitializationEvent event) {
		touchScreen = (new ItemPDAScreen(TouchScreenID)).setIconIndex(17)
				.setItemName("touchScreen");
		LanguageRegistry.addName(touchScreen, "TouchScreen");
		GameRegistry.addRecipe(new ItemStack(CCPortable.touchScreen),
				new Object[] { "XXX", "SDS", " R ", 'X', Block.glass, 'S',
						Block.stone, 'D', Item.diamond, 'R', Item.redstone });

		pdaBattery = (new ItemPDABattery(PDABatteryID)).setIconIndex(18)
				.setItemName("PDABattery");
		LanguageRegistry.addName(pdaBattery, "PDA Battery");
		GameRegistry.addRecipe(new ItemStack(CCPortable.pdaBattery),
				new Object[] { " R ", "IGI", " R ", 'R', Item.redstone, 'I',
						Item.ingotIron, 'G', Item.ingotGold });

		/*pdaItem = (new ItemPDA(PDAID)).setIconIndex(16).setItemName("pdaItem");
		LanguageRegistry.addName(pdaItem, "PDA");
		GameRegistry.addRecipe(new ItemStack(CCPortable.pdaItem), new Object[] {
				" X ", "ITI", "BPB", 'X', Block.torchRedstoneActive, 'I',
				Item.ingotIron, 'T', CCPortable.touchScreen, 'B',
				Block.stoneButton, 'P', CCPortable.pdaBattery });*/

		receiverBlock = new BlockReceiver(ReceiverID, 0, Material.rock)
				.setStepSound(Block.soundStoneFootstep).setHardness(5F)
				.setResistance(5.0F).setBlockName("receiverBlock");
		LanguageRegistry.addName(receiverBlock, "Receiver");
		GameRegistry.registerBlock(receiverBlock, "CCPortable");
		GameRegistry.addRecipe(new ItemStack(CCPortable.receiverBlock),
				new Object[] { " T ", "SGS", "SRS", 'T',
						Block.torchRedstoneActive, 'S', Block.stone, 'G',
						Item.ingotGold, 'R', Item.redstone });
		GameRegistry.registerTileEntity(TileEntityPDA.class, "managerPDA");

		proxy.registerRenderThings();
	}

	public static void doEvent(int id, String name, Object[] args) {
		PDAFrequency item = (PDAFrequency) allFreq.get(id);
	    item.doEvent(name, args);
		allFreq.put(id, item);
	}

	public static PDAFrequency getFreq(int freq) {
		return (PDAFrequency) allFreq.get(freq);
	}
	
	public static void setFreq(int freq, PDAFrequency frequ) {
		allFreq.put(frequ, frequ);
	}
}

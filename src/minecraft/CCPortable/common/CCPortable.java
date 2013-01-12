package CCPortable.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
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

@Mod(modid = "CCPortable", name = "CCPortable", version = "0.3")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"CSPortablekcev","CSPortabledr","SCPortablerd","CSPortablepdc"}, packetHandler = PacketHandler.class)


public class CCPortable
{
	public static ItemStack[] allPDAs = new ItemStack[256];
	public static TileEntityPDA[] allReceivers = new TileEntityPDA[256];
	static EnumToolMaterial pdaMaterial = EnumHelper.addToolMaterial( "pdaMaterial", 0, -1, 2F, 2, 14 );
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
	static EnumArmorMaterial helmetMat = EnumHelper.addArmorMaterial("PDAArmor", 35, new int[]{0,0,0,0}, 0);

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
	    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
	    config.load();
	    int ReceiverID = config.getBlock("Receiver ID", 145).getInt();
	    int PDAID = config.getItem("Item PDA ID", 550).getInt();
	    int TouchScreenID = config.getItem("TouchScreen ID", 551).getInt();
	    int PDABatteryID = config.getItem("Battery ID", 552).getInt();
	    int PDAHelmetID = config.getItem("Helmet ID", 553).getInt();
	    config.save();
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
	    pdaHelmet = new ItemPDAHelmet(PDAHelmetID,helmetMat, ModLoader.addArmor("PDAHelmet"),0)
		.setItemName("PDAHelm")
		.setIconIndex(19);
	    LanguageRegistry.addName(pdaHelmet, "PDA Vision");
	    GameRegistry.addRecipe(new ItemStack(CCPortable.pdaHelmet), new Object[]
		{
		    " T ", "GPG", "IDI",
		    'T', Block.torchRedstoneActive,
		    'P', CCPortable.pdaItem,
		    'G', Block.glass,
		    'I', Item.ingotIron,
		    'D', Item.diamond
		});

	    receiverBlock = new BlockReceiver (ReceiverID , 0, Material.rock)
		.setStepSound(Block.soundStoneFootstep)
		.setHardness(5F)
		.setResistance(5.0F)
		.setBlockName("receiverBlock");
	    GameRegistry.registerBlock(receiverBlock);
	    LanguageRegistry.addName(receiverBlock, "Receiver");
	    GameRegistry.addRecipe(new ItemStack(CCPortable.receiverBlock), new Object[]
		{
		    " T ", "SGS", "SRS",
		    'T', Block.torchRedstoneActive,
		    'S', Block.stone,
		    'G', Item.ingotGold,
		    'R', Item.redstone
		});
	    GameRegistry.registerTileEntity( TileEntityPDA.class, "managerPDA" );

	    pdaItem = (new ItemPDA(PDAID))
		.setIconIndex(16)
		.setItemName("pdaItem"); 
	    LanguageRegistry.addName(pdaItem, "PDA");
	    GameRegistry.addRecipe(new ItemStack(CCPortable.pdaItem), new Object[]
		{
		    " X ", "ITI", "BPB",
		    'X', Block.torchRedstoneActive,
		    'I', Item.ingotIron,
		    'T', CCPortable.touchScreen, 
		    'B', Block.stoneButton,
		    'P', CCPortable.pdaBattery
		});

	    touchScreen = (new ItemPDAPart(TouchScreenID))
		.setIconIndex(17)
		.setItemName("touchScreen"); 
	    LanguageRegistry.addName(touchScreen, "TouchScreen");
	    GameRegistry.addRecipe(new ItemStack(CCPortable.touchScreen), new Object[]
		{
		    "XXX", "SDS", " R ",
		    'X', Block.glass,
		    'S', Block.stone,
		    'D', Item.diamond,
		    'R', Item.redstone
		});

	    pdaBattery = (new ItemPDAPart(PDABatteryID))
		.setIconIndex(18)
		.setItemName("PDABattery"); 
	    LanguageRegistry.addName(pdaBattery, "PDA Battery");
	    GameRegistry.addRecipe(new ItemStack(CCPortable.pdaBattery), new Object[]
		{
		    " R ", "IGI", " R ",
		    'R', Item.redstone,
		    'I', Item.ingotIron, 
		    'G', Item.ingotGold
		});

	    proxy.registerRenderThings();
	}
	
	public static ItemStack getPDA(int i)
	{
	    if (allPDAs[i] == null)
		return null;
	    return allPDAs[i];
	}
	public static void savePDA(boolean addorrem, int id, ItemStack iS)
	{
	    if (addorrem) {
		allPDAs[id] = iS;
		return;
	    }
	    allPDAs[id] = null;
	}
	public static TileEntityPDA getReceiver(int i)
	{
	    if (allReceivers[i] == null)
		return null;
	    return allReceivers[i];
	}
	public static void saveReceiver(boolean addorrem, int id, TileEntityPDA iS)
	{
	    if (addorrem) {
		allReceivers[id] = iS;
		return;
	    }
	    allReceivers[id] = null;
	}
}



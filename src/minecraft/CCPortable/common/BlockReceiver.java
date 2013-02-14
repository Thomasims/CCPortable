package CCPortable.common;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockReceiver extends Block
{
    public BlockReceiver(int i, int j, Material material)
    {
    	super(i, j, material);
    	this.setCreativeTab(CreativeTabs.tabRedstone);
    	this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    public String getTextureFile()
    {
	    return "/CCPortable/Textures.png";
    }

    public boolean hasTileEntity(int metadata)
    {
    	return true;
    }

    public TileEntity createTileEntity(World world, int metadata)
    {
    	TileEntityPDA tP = new TileEntityPDA();
    	tP.id = CCPortable.createReceiver(tP);
    	return tP;
    }
}
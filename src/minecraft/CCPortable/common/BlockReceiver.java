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
	private int side;
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

    public int quantityDropped(Random par1Random)
    {
    	return 1;
    }

    public int getSide()
    {
    	return this.side;
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	if (!world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, side))
    		return false;

    	world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, side);
		this.setBlockBounds(1F,1F,1F,1F,1F,1F);
        return true;
    }

    public boolean renderAsNormalBlock()
    {
	return false;
    }

    public boolean isOpaqueCube()
    {
    	return false;
    }

    public boolean hasTileEntity(int metadata)
    {
    	return true;
    }

    public TileEntity createNewTileEntity(World par1World)
    {
    	try
    	{
    		return (TileEntity) new TileEntityPDA();
    	}
    	catch (Exception var3)
    	{
    		throw new RuntimeException(var3);
    	}
    }
}
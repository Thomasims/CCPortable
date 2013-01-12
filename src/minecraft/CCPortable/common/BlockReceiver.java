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
    public int IDs;
	private int ID;
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
    	ID++;
    	CCPortable.saveReceiver(true, ID, (TileEntityPDA)world.getBlockTileEntity(x,y,z));
    	if (!world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, side))
    		return false;

    	world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, side);
    	this.side = side;
		float n1 = 0.0F;
		float n2 = 0.2F;
		float n3 = 0.8F;
		float n4 = 1.0F;
		switch(side)
		{
		case 0:
			this.setBlockBounds(n3,n4,n3,n2,n3,n2);
			break;
		case 1:
			this.setBlockBounds(n3,n1,n3,n2,n2,n2);
			break;
		case 2:
			this.setBlockBounds(n3,n4,n3,n2,n3,n2);
			break;
		case 3:
			this.setBlockBounds(n3,n1,n3,n2,n2,n2);
			break;
		case 4:
			this.setBlockBounds(n3,n4,n3,n2,n3,n2);
			break;
		case 5:
			this.setBlockBounds(n3,n1,n3,n2,n2,n2);
			break;
		default:
			this.setBlockBounds(n1,n1,n1,n4,n4,n4);
		}
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
    		return (TileEntity) new TileEntityPDA(this,ID);
    	}
    	catch (Exception var3)
    	{
    		throw new RuntimeException(var3);
    	}
    }
}
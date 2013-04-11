package CCPortable.common;

import CCPortable.API.EnumPDAType;
import CCPortable.API.IPDAPart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class ItemPDAPart extends Item implements IPDAPart
{
	public int subType;
	
    public ItemPDAPart(int i)
    {
        super(i);
        maxStackSize = 8;
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    public String getTextureFile()
    {
            return "/CCPortable/Textures.png";
    }

	public abstract EnumPDAType getPDAType();

	public abstract String getDisplayName();

	public abstract int getTier();

	public abstract String[] getSpecialMethods();
}
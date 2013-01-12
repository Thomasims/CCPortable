package CCPortable.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemPDAPart extends Item
{
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
}
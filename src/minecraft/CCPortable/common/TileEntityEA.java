package CCPortable.common;

import CCPortable.API.EnumPDAType;
import CCPortable.API.IPDAPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEA extends TileEntity implements IInventory {

	private ItemStack[] inventory;
	public boolean[] states;

	public TileEntityEA() {
		inventory = new ItemStack[18];
		states = new boolean[15];
		states[10] = true;
	}

	public boolean addItem(ItemStack iS, int slot) {
		Item part = iS.getItem();
		if (states[slot]) {
			if (((IPDAPart) part).getPDAType() == EnumPDAType.Case) {
				if (slot == 4) {
					states[1] = true;
					states[3] = true;
					states[5] = true;
					return true;
				} else if (slot == 10) {
					states[13] = true;
					states[6] = true;
					states[8] = true;
					return true;
				}
				return false;
			} else if (((IPDAPart) part).getPDAType() == EnumPDAType.motherBoard && slot == 13) {
				states[1] = ((IPDAPart) part).getTier() == 3
						&& inventory[4] != null;
				states[3] = ((IPDAPart) part).getTier() == 3
						&& inventory[4] != null;
				states[5] = ((IPDAPart) part).getTier() == 3
						&& inventory[4] != null;
				states[7] = true;
				states[9] = ((IPDAPart) part).getTier() == 2;
				states[11] = true;
				states[12] = ((IPDAPart) part).getTier() == 3;
				states[14] = true;
			} else if (((IPDAPart) part).getPDAType() == EnumPDAType.pivot && (slot == 6 || slot == 8)) {
				if (inventory[6] != null && slot == 8) {
					states[4] = true;
					return true;
				} else if (inventory[8] != null && slot == 6) {
					states[4] = true;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
				&& player.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}

	@Override
	public String getInvName() {
		return "CCP.ElAs";
	}
}
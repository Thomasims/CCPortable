package CCPortable.common;

import CCPortable.API.EnumPDAType;
import CCPortable.API.IPDAPart;

public class ItemPDABattery extends ItemPDAPart {

	public ItemPDABattery(int i) {
		super(i);
	}

	@Override
	public EnumPDAType getPDAType() {
		return EnumPDAType.Battery;
	}

	@Override
	public String getDisplayName() {
		return "Battery";
	}

	@Override
	public int getTier() {
		return 1;
	}

	@Override
	public String[] getSpecialMethods() {
		return new String[] {};
	}

}

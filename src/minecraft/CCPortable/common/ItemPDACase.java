package CCPortable.common;

import CCPortable.API.EnumPDAType;

public class ItemPDACase extends ItemPDAPart {

	public ItemPDACase(int i) {
		super(i);
	}

	@Override
	public EnumPDAType getPDAType() {
		return EnumPDAType.BaseCase;
	}

	@Override
	public String getDisplayName() {
		return "Casing";
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

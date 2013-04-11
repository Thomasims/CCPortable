package CCPortable.common.pdaParts;

import CCPortable.API.EnumPDAType;

public class ItemPDABoard extends ItemPDAPart {

	public ItemPDABoard(int i) {
		super(i);
	}

	@Override
	public EnumPDAType getPDAType() {
		return EnumPDAType.MotherBoard;
	}

	@Override
	public String getDisplayName() {
		switch(subType) {
		case 0:
			return "Mother Board MK I";
		case 1:
			return "Mother Board MK II";
		case 2:
			return "Mother Board MK III";
		case 3:
			return "Mother Board MK IV";
		default:
			return null;
		}
	}

	@Override
	public int getTier() {
		return subType + 1;
	}

	@Override
	public String[] getSpecialMethods() {
		return new String[] {};
	}

}

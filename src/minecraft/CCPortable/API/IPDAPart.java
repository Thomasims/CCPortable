package CCPortable.API;

public interface IPDAPart {

    /* return the type of the part
     *
     * @return EnumPDAType type
     */

    public EnumPDAType getPDAType();

    /* return the name that should be added on the list of peripherals of the PDA
     *
     * @return String descName
     */

    public String getDisplayName();

    /* return the tier of the part (used to unlock some slots with the motherboard for example,
     * tier 3 is required to unlock all electronic slots
     *
     * @return int tier
     */

    public int getTier();

    /* return the methods names that this part could add
     * NOT YET IMPLEMTED
     *
     * @return String[] function names
     */

    public String[] getSpecialMethods();
}
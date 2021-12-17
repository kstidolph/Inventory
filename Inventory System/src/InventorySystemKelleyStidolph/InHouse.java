package InventorySystemKelleyStidolph;

/**
 * This class is for parts made in-house. It stores the name of the part, its price, its ID, the amount in stock, the minimum and maximum we can have of the part, and the ID of the machine that produces it.
 */
public class InHouse extends Part {
    private int machineID;

    /**
     * RUNTIME ERROR: I made the same mistake here as in the Outsourced class. I had declared all class variables within this class, including those inherited from the Part class. This led to duplicate variables with the same name, and the forms updating one of the two variables but the TableViews showing the other for each pair. This was fixed by not declaring inherited variables, and using the superclass's constructor.
     * @param newID
     * @param newName
     * @param newPrice
     * @param newStock
     * @param newMin
     * @param newMax
     * @param newMachineID
     */
    public InHouse(int newID, String newName, double newPrice, int newStock, int newMin, int newMax, int newMachineID) {
        super(newID, newName, newPrice, newStock, newMin, newMax);
        this.setMachineID(newMachineID);
        Inventory.addPart(this);
    }


    public void setMachineID (int newMachineID) {
        machineID = newMachineID;
    }

    public int getMachineID() {
        return machineID;
    }



}


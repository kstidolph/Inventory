package InventorySystemKelleyStidolph;

/**
 * This class is for parts not produced by the company. It stores the name of the part, its price, its ID, the amount in stock, the minimum and maximum we can have of the part, and the name of the company that produces it.
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * RUNTIME ERROR: I had declared the class variables within this class, including those inherited from the Part class. This led to duplicate variables with the same name, and the forms updating one of the two variables but the TableViews showing the other for each pair. This was fixed by not declaring inherited variables, and using the superclass's constructor.
     * @param newID
     * @param newName
     * @param newPrice
     * @param newStock
     * @param newMin
     * @param newMax
     * @param companyName
     */
    public Outsourced(int newID, String newName, double newPrice, int newStock, int newMin, int newMax, String companyName) {
        super(newID, newName, newPrice, newStock, newMin, newMax);
        this.setCompanyName(companyName);
        Inventory.addPart(this);
    }

    public void setCompanyName (String newName) {
        companyName = newName;
    }

    public String getCompanyName() {
        return companyName;
    }



}

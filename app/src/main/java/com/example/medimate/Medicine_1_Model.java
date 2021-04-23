package com.example.medimate;

public class Medicine_1_Model {
    String MedicineName;
    String MG;
    String ExpiryDate;
    String Quantity;
    String Image;
    String ID;
    String MedicineId;

    public Medicine_1_Model(String medicineName, String MG, String expiryDate, String quantity, String image, String ID, String medicineId) {
        MedicineName = medicineName;
        this.MG = MG;
        ExpiryDate = expiryDate;
        Quantity = quantity;
        Image = image;
        this.ID = ID;
        MedicineId = medicineId;
    }

    public Medicine_1_Model() {
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getMG() {
        return MG;
    }

    public void setMG(String MG) {
        this.MG = MG;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

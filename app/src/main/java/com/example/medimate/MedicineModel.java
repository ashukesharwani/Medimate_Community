package com.example.medimate;

public class MedicineModel {
    String MedicineName;
    String MG;
    String ExpiryDate;
    String Quantity;
    String Image;
    String ID;

    public MedicineModel(String medicineName, String MG, String expiryDate, String quantity, String image, String ID) {
        MedicineName = medicineName;
        this.MG = MG;
        ExpiryDate = expiryDate;
        Quantity = quantity;
        Image = image;
        this.ID = ID;
    }

    public MedicineModel() {
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public void setMG(String MG) {
        this.MG = MG;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public String getMG() {
        return MG;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getImage() {
        return Image;
    }

    public String getID() {
        return ID;
    }
}

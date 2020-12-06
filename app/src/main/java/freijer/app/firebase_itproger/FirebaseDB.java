package freijer.app.firebase_itproger;

public class FirebaseDB {
    private String emael, pass, name, phone;
        public FirebaseDB(){}

    public FirebaseDB(String emael, String pass, String name, String phone) {
        this.emael = emael;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
    }

    public String getEmael() {
        return emael;
    }
    public void setEmael(String emael) {
        this.emael = emael;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


}


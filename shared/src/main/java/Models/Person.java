package Models;

public class Person {
    /**
     * Person object with personID (unique string), descendant (referencing username),
     * first_name, last_name (String), gender (string), father (references personID),
     * mother (references personID), spouse (references personID)
     */
    String personID;
    String descendant;
    String firstName;
    String lastName;
    String gender;
    String father;
    String mother;
    String spouse;

    public Person(String pID, String descd, String fname, String lname,
                  String gend, String f, String m, String sp){

        personID = pID;
        descendant = descd;
        firstName = fname;
        lastName = lname;
        gender = gend;
        father = f;
        mother = m;
        spouse = sp;
    }

    public Person(String pID, String descd, String fname, String lname,
                  String gend){

        personID = pID;
        descendant = descd;
        firstName = fname;
        lastName = lname;
        gender = gend;

    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}

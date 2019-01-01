package DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import Models.AuthToken;
import Models.Person;

import static org.junit.Assert.*;

public class PersonDAOTest {
    DataBase db;

    @Before
    public void setUp() throws Exception {
        db = new DataBase();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Test finished");
        try{
            db.openConnection();
            db.clearAll();
            db.closeConnection(true);
        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    public ArrayList<Person> createPeople(){
        String pID1 = UUID.randomUUID().toString();
        String pID2 = UUID.randomUUID().toString();
        String pID3 = UUID.randomUUID().toString();
        String pID4 = UUID.randomUUID().toString();
        String desc = "Brody";
        String n1 = "Brad";
        String n2 = "Joe";
        String n3 = "Aaron";
        String n4 = "Jess";
        String last = "Williams";
        String m = "m";
        String f = "f";
        String father = "Jack";
        String mother = "Jill";

        ArrayList<Person> list = new ArrayList<>();

        list.add(new Person(pID1, desc, n1, last, m, father, mother, null));
        list.add(new Person(pID2, desc, n2, last, m, father, mother, null));
        list.add(new Person(pID3, desc, n3, last, m, father, mother, null));
        list.add(new Person(pID4, desc, n4, last, f, father, mother, null));

        return list;

    }

    @Test
    public void createPerson() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            ArrayList<Person> people = createPeople();

            PersonDAO pDAO = db.getPerson();

            for (Person p: people){
                pDAO.createPerson(p);
                Person testP = pDAO.getPerson(p.getPersonID());
                assertNotNull(testP);
                assertEquals(p.getPersonID(), testP.getPersonID());
                assertEquals(p.getDescendant(), testP.getDescendant());
                assertEquals(p.getFather(), testP.getFather());
                assertEquals(p.getLastName(), testP.getLastName());
                assertEquals(p.getFirstName(), testP.getFirstName());
                assertEquals(p.getMother(), testP.getMother());
                assertEquals(p.getGender(), testP.getGender());
            }



            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void getPerson() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            ArrayList<Person> people = createPeople();

            PersonDAO pDAO = db.getPerson();

            Person p = people.get(0);

            pDAO.createPerson(p);
            Person testP = pDAO.getPerson(p.getPersonID());
            assertNotNull(testP);
            assertEquals(p.getPersonID(), testP.getPersonID());
            assertEquals(p.getDescendant(), testP.getDescendant());
            assertEquals(p.getFather(), testP.getFather());
            assertEquals(p.getLastName(), testP.getLastName());
            assertEquals(p.getFirstName(), testP.getFirstName());
            assertEquals(p.getMother(), testP.getMother());
            assertEquals(p.getGender(), testP.getGender());

            // get person not in database
            Person psn = pDAO.getPerson("3994");
            assertNull(psn);


            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void getAllPeople() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            ArrayList<Person> people = createPeople();

            PersonDAO pDAO = db.getPerson();


            for (Person p: people){
                pDAO.createPerson(p);
            }

            ArrayList<Person> allPeople = pDAO.getAllPeople("Brody");

            assertNotNull(allPeople);
            assertEquals(allPeople.size(), 4);

            for(int i = 0; i < allPeople.size(); i++){
                assertEquals(allPeople.get(i).getPersonID(), people.get(i).getPersonID());
                assertEquals(allPeople.get(i).getDescendant(), people.get(i).getDescendant());
                assertEquals(allPeople.get(i).getFather(), people.get(i).getFather());
                assertEquals(allPeople.get(i).getLastName(), people.get(i).getLastName());
                assertEquals(allPeople.get(i).getFirstName(), people.get(i).getFirstName());
                assertEquals(allPeople.get(i).getMother(), people.get(i).getMother());
                assertEquals(allPeople.get(i).getGender(), people.get(i).getGender());
            }


            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void deleteByUser() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            ArrayList<Person> people = createPeople();

            PersonDAO pDAO = db.getPerson();

            // create person with different descendant
            String pID1 = UUID.randomUUID().toString();
            String desc = "Johnny";
            String n1 = "Tony";
            String last = "Thompson";
            String m = "m";
            String father = "Jack";
            String mother = "Jill";

            // add extra person
            Person psn = new Person(pID1, desc, n1, last, m, father, mother, null);
            pDAO.createPerson(psn);

            for (Person p: people){
                pDAO.createPerson(p);
            }

            Person testP1 = pDAO.getPerson(psn.getPersonID());

            // make sure extra person was added
            assertEquals(testP1.getPersonID(), testP1.getPersonID());

            // delete extra person
            pDAO.deleteByUser(psn.getDescendant());

            // make sure it was deleted
            Person test = pDAO.getPerson(psn.getPersonID());
            assertNull(test);

            // delete user not in database
            ArrayList<Person> allP1 = pDAO.getAllPeople("Brody");
            assertEquals(allP1.size(), 4);
            pDAO.deleteByUser("Tom");
            ArrayList<Person> testAllP = pDAO.getAllPeople("Brody");
            assertEquals(testAllP.size(), 4);

            // delete the rest
            pDAO.deleteByUser("Brody");
            ArrayList<Person> allP2 = pDAO.getAllPeople("Brody");

            assertNull(allP2);


            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void clear() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            ArrayList<Person> people = createPeople();

            PersonDAO pDAO = db.getPerson();

            for(Person p: people){
                pDAO.createPerson(p);
            }

            ArrayList<Person> listP = pDAO.getAllPeople("Brody");

            assertEquals(listP.size(), 4);

            pDAO.clear();

            ArrayList<Person> allP = pDAO.getAllPeople("Brody");
            assertNull(allP);


            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }

}
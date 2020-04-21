package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
    private static Faker faker = new Faker();

    private static Person randomPerson() {
        Person person = new Person();

        person.setName(faker.name().fullName());
        person.setGender(faker.options().option(Person.Gender.class));
        person.setProfession(faker.company().profession());
        person.setEmail(faker.internet().emailAddress());

        Date date = faker.date().birthday();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        person.setDob(localDate);

        Address address = new Address();
        address.setCountry(faker.address().country());
        address.setState(faker.address().state());
        address.setCity(faker.address().city());
        address.setStreetAddress(faker.address().streetAddress());
        address.setZip(faker.address().zipCode());
        person.setAddress(address);

        return person;

    }
    private static void createPeople(int number) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            for(int i=0; i < number; i++){
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        }finally{
            em.close();
        }
    }

    private static List<Person> getPeople() {
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class).getResultList();
        }finally{
            em.close();
        }
    }

    public static void main(String[] args) {
        createPeople(10);
        getPeople().forEach(System.out::println);
        emf.close();
    }
}

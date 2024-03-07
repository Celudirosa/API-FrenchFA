/**
 * Segun el enfoque: Una prueba unitaria se divide en tres partes
 *
 * 1. Arrange: Setting up the data that is required for this test case
 * 2. Act: Calling a method or Unit that is being tested.
 * 3. Assert: Verify that the expected result is right or wrong.
 *
 * Segun el enfoque BDD (Behaviour Driven Development). 'Given-When-Then' como lenguaje comun con BDD
* 
* Para definir los casos BDD para una historia de usuario se deben definir bajo el patrón "Given-When-Then"
* , que se define como sigue:
 *
 * 1. given (dado) : Se especifica el escenario, las precondiciones.
 * 2. when (cuando) : Las condiciones de las acciones que se van a ejecutar.
 * 3. then (entonces) : El resultado esperado, las validaciones a realizar.
*
* Un ejemplo practico seria:
*
* Given: Dado que el usuario no ha introducido ningun dato en el formulario.
* When: Cuando se hace click en el boton de enviar.
* Then: Se deben de mostrar los mensajes de validación apropiados.
*
* "Role-Feature-Reason" como lenguaje común con BDD
*
* Este patrón se utiliza en BDD para ayudar a la creación de historias de usuarios. Este se define como:
*
* As a "Como" : Se especifica el tipo de usuario.
* I want "Deseo" : Las necesidades que tiene.
* So that "Para que" : Las caracteristicas para cumplir el objetivo.
*
* Un ejemplo práctico de historia de usuario sería: Como cliente interesado, deseo ponerme en contacto mediante formulario, 
* para que atiendan mis necesidades. 
*
* Parece que BDD y TDD son la misma cosa, pero la principal diferencia entre ambas esta en el alcance. TDD es una practica de desarrollo 
* (se enfoca en como escribir el codigo y como deberia trabajar ese codigo) mientras que BDD es una metodologia de equipo (Se enfoca
* en porque debes escribir ese codigo y como se deberia de comportar ese codigo)
*
* En TDD el desarrollador escribe los tests mientras que en BDD el usuario final (o PO o analista) en conjunto con los testers escriben
* los tests (y los Devs solo generan el codigo necesario para ejecutar dichos tests)
*
* Tambien existe ATDD (Acceptance Test Driven Development), que es mas cercana a BDD ya que no es una practica,
* sino una metodologia de trabajo, pero la diferencia esta nuevamente en el alcance, a diferencia de BDD, ATDD se extiende aun 
* mas en profundizar la búsqueda de que lo que se esta haciendo no solo se hace de forma correcta, sino que tambien 
* es lo correcto a hacer.
*
 */

 package com.example.repository;

 import static org.assertj.core.api.Assertions.assertThat;
 
 import java.util.List;
 import java.util.Optional;
 
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
 import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
 import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
 
 import com.example.user.Role;
 import com.example.user.User;
 import com.example.user.UserRepository;
 
 @DataJpaTest
 @AutoConfigureTestDatabase(replace = Replace.NONE)
 public class UserRepositoryTests {
 
     @Autowired
     private UserRepository userRepository;
 
     private User user0;
 
     @BeforeEach
     void setUp() {
         user0 = User.builder()
                 .firstName("AdminName")
                 .lastName("AdminLastName")
                 .password("Aaaaaa1!")
                 .email("admin@blue.com")
                 .role(Role.ADMINISTRATOR)
                 .build();
     }
 
     // Test para agregar un user
     @Test
     @DisplayName("Test Add user")
     public void testAddUser() {
 
         // given
         userRepository.save(user0);
 
         // when
         User userAdded = userRepository.save(user0);
 
         // then
         assertThat(userAdded).isNotNull(); // Ensure that the added user is not null
         assertThat(userAdded.getId()).isGreaterThan(0L); // Ensure valid user ID and database save
         assertThat(userAdded).isEqualTo(user0); // Verify object integrity post-save
 
     }
 
     @DisplayName("User listing test.")
     @Test
     public void testFindAllUsers() {
 
         // given
         User user1 = User.builder()
                 .firstName("TrainerName")
                 .lastName("TrainerLastName")
                 .password("Bbbbbb1!")
                 .email("trainer@blue.com")
                 .role(Role.TRAINER)
                 .build();
 
         userRepository.save(user0);
         userRepository.save(user1);
 
         // when
         List<User> foundUsers = userRepository.findAll();
 
         // then
         assertThat(foundUsers)
                 .isNotNull() // Check if foundUsers list is not null
                 .hasSize(2) // Check if the list has size 2
                 .contains(user0, user1); // Check if a collection contains specific elements 
 
     }
 
     @Test
     @DisplayName("Retrieve a user by ID.")
     public void findUserById() {
 
         // given
 
         userRepository.save(user0);
 
         // when
 
         User user = userRepository.findById(user0.getId()).get();
 
         // then
         assertThat(user.getId()).isNotEqualTo(0L);
 
     }
 
     @Test
     @DisplayName("User update test")
     public void testUpdateUser() {
 
         // given
 
         userRepository.save(user0);
 
         // when
         User userSaved = userRepository.findByEmail(user0.getEmail()).get();
 
         userSaved.setFirstName("AdminNameUp");
         userSaved.setLastName("AdminLastNameUp");
         userSaved.setEmail("adminup@blue.com");
 
         User userUpdated = userRepository.save(userSaved);
 
         // then
         assertThat(userUpdated).isNotNull(); // Check updated user is not null
         assertThat(userUpdated.getId()).isEqualTo(userSaved.getId()); // Check userUpdated has the same ID than userSaved
         assertThat(userUpdated.getEmail()).isEqualTo("adminup@blue.com"); //Check that the email has been updated correctly
         assertThat(userUpdated.getFirstName()).isEqualTo("AdminNameUp"); //Check that the firstName has been updated correctly
         assertThat(userUpdated.getLastName()).isEqualTo("AdminLastNameUp"); //Check that the lastName has been updated correctly
 
     }
 
     @DisplayName("Test to delete an user ")
     @Test
     public void testDeleteUser() {
 
         // given
         userRepository.save(user0);
 
         // when
         userRepository.delete(user0);
         Optional<User> userDeleted = userRepository.findByEmail(user0.getEmail());
 
         // then
         assertThat(userDeleted).isEmpty();
     }
 }
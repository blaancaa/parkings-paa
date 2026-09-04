package paa.parking;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;      
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import paa.parking.model.Parking;
import paa.parking.repository.IParkingRepository;

@SpringBootTest // ó @DataJpaTest para no cargar toda la infraestructura Spring
public class RepositoryTest 
{
	 @Autowired //para que Spring inyecte el objeto con la implementación del repositorio JPA en el atributo
	 private IParkingRepository repository;
	 
	 @Test
	 public void tryCreateParking() 
	 {
		 Parking parking = new Parking(null, "name", "address", 10, 5, 0.0, 0.0);
		 parking = repository.save(parking);
		
		 assertNotNull(parking.getId()); // Comprobación básica
		
		 // Comprobación con búsqueda y comparación de campos
		 Optional<Parking> found = repository.findById(parking.getId());
		 assertTrue(found.isPresent());
		 assertThat(found.get())
		 .usingRecursiveComparison()
		 .ignoringFields("reservations") // Evitar LazyInitializationException
		 .isEqualTo(parking);
	 }
}
 

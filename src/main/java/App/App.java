package App;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;

import Entities.Elemento;
import Entities.Libro;
import Entities.Periodicita;
import Entities.Prestito;
import Entities.Rivista;
import Entities.Utente;
import Utils.CatalogoDAO;

public class App {
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("CatalogoBibliografico");
	private static Logger log = Logger.getLogger(App.class);

	public static void main(String[] args) {
		EntityManager em = emf.createEntityManager();
		CatalogoDAO ctg = new CatalogoDAO(em);

		// -----------------------SAVE-------------------------------------
		Utente persona1 = new Utente("032659", "Mario", "Rossi", LocalDate.of(1980, 05, 15));
		Utente persona2 = new Utente("045948", "Laura", "Verdi", LocalDate.of(1992, 11, 28));

		Libro nuovo = new Libro(1234567890L, "Il signore degli Anelli", 2007, 662, "Tolkien", "Fantasy");
		Libro nuovo1 = new Libro(1234567891L, "Il signore degli Anelli2", 2009, 789, "Tolkien", "Fantasy");

		Rivista nuovo2 = new Rivista(1234567892L, "Focus", 1998, 45, Periodicita.SETTIMANALE);
		Rivista nuovo3 = new Rivista(1234567893L, "Vivair", 1994, 33, Periodicita.SEMESTRALE);

		ctg.addUser(persona1);
		ctg.addUser(persona2);

		ctg.addItem(nuovo);
		ctg.addItem(nuovo1);
		ctg.addItem(nuovo2);
		ctg.addItem(nuovo3);

		ctg.findByIsbnAndDelete(1234567893L);

		Elemento item = ctg.ricercaPerISBN(1234567893L);
		log.info(item);

		List<Elemento> elementiAnno = ctg.ricercaPerAnnoPubblicazione(2007);
		log.info(elementiAnno);

		List<Elemento> elementiAutore = ctg.ricercaPerAutore("Tolkien");
		log.info(elementiAutore);

		List<Elemento> elementiTitolo = ctg.ricercaPerTitolo("Focus");
		log.info(elementiTitolo);

		Prestito user = new Prestito(persona2, nuovo3, LocalDate.of(2022, 5, 30), LocalDate.of(2022, 7, 30),
				LocalDate.of(2022, 9, 10));
		ctg.addPrestito(user);

		List<Prestito> prestitiUtente = ctg.ricercaPrestitiUtente("032659");
		log.info(prestitiUtente);

		List<Prestito> prestitiScadutiNonRestituiti = ctg.ricercaPrestitiScaduti();
		log.info(prestitiScadutiNonRestituiti);

		em.close();
		emf.close();
	}
}

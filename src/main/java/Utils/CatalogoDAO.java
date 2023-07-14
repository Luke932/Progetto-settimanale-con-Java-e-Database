package Utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import Entities.Elemento;
import Entities.Prestito;
import Entities.Utente;

public class CatalogoDAO {
	private final EntityManager em;
	private static Logger log = Logger.getLogger(CatalogoDAO.class);

	public CatalogoDAO(EntityManager em) {
		this.em = em;
	}

	public void addItem(Elemento e) {
		EntityTransaction t = em.getTransaction();
		t.begin();

		em.persist(e);

		t.commit();
		log.info("Elemento salvato correttamente");
	}

	public void addUser(Utente e) {
		EntityTransaction t = em.getTransaction();
		t.begin();

		em.persist(e);

		t.commit();
		log.info("Utente salvato correttamente");
	}

	public void addPrestito(Prestito s) {
		EntityTransaction t = em.getTransaction();
		t.begin();

		// Salvataggio dell'istanza di Elemento se non è stata salvata precedentemente
		if (!em.contains(s.getElemento())) {
			em.persist(s.getElemento());
		}

		em.persist(s);

		t.commit();
		log.info("Prestito salvato correttamente");
	}

	public void findByIsbnAndDelete(Long isbn) {
		Elemento found = em.find(Elemento.class, isbn);
		if (found != null) {
			EntityTransaction t = em.getTransaction();

			t.begin();

			em.remove(found);

			t.commit();
			log.info("Elemento eliminato correttamente");
		} else {
			log.error("Elemento non trovato");
		}
	}

	public Elemento ricercaPerISBN(Long isbn) {
		return em.find(Elemento.class, isbn);
	}

	public List<Elemento> ricercaPerAnnoPubblicazione(int anno) {
		TypedQuery<Elemento> query = em.createQuery("SELECT e FROM Elemento e WHERE e.annoPubblicazione = :anno",
				Elemento.class);
		query.setParameter("anno", anno);
		return query.getResultList();
	}

	public List<Elemento> ricercaPerAutore(String autore) {
		TypedQuery<Elemento> query = em.createQuery("SELECT e FROM Libro e WHERE e.autore = :autore", Elemento.class);
		query.setParameter("autore", autore);
		return query.getResultList();
	}

	public List<Elemento> ricercaPerTitolo(String titolo) {
		TypedQuery<Elemento> query = em.createQuery("SELECT e FROM Elemento e WHERE e.titolo LIKE :titolo",
				Elemento.class);
		query.setParameter("titolo", "%" + titolo + "%");
		return query.getResultList();
	}

	public List<Prestito> ricercaPrestitiUtente(String numeroTessera) {
		TypedQuery<Prestito> query = em.createQuery("SELECT p FROM Prestito p WHERE p.utente.numeroTessera = :tessera",
				Prestito.class);
		query.setParameter("tessera", numeroTessera);
		return query.getResultList();
	}

	public List<Prestito> ricercaPrestitiScaduti() {
		TypedQuery<Prestito> query = em.createQuery(
				"SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < CURRENT_DATE AND p.dataRestituzioneEffettiva IS NULL",
				Prestito.class);
		return query.getResultList();
	}
}

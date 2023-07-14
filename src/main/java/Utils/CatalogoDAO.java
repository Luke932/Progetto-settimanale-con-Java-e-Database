package Utils;

import javax.persistence.EntityManager;

public class CatalogoDAO {
	private final EntityManager em;

	public CatalogoDAO(EntityManager em) {
		this.em = em;
	}

}

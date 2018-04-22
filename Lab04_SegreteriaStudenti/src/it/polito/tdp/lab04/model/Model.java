package it.polito.tdp.lab04.model;

import java.util.List;

import it.polito.tdp.lab04.DAO.CorsoDAO;
import it.polito.tdp.lab04.DAO.StudenteDAO;

public class Model {

	private StudenteDAO studenteDao;
	private CorsoDAO corsoDao;

	public Model() {

		studenteDao = new StudenteDAO();
		corsoDao = new CorsoDAO();
	}

	public Studente getStudente(int matricola) {

		return studenteDao.getStudente(matricola);
	}

	public List<Studente> studentiIscrittiAlCorso(Corso corso) {
		
		return corsoDao.getStudentiIscrittiAlCorso(corso);
	}

	public List<Corso> cercaCorsiDatoStudente(Studente studente) {

		return studenteDao.getCorsiFromStudente(studente);
	}

	/*
	 * Ritorna TRUE se lo studente è iscritto al corso, FALSE altrimenti
	 */
	public boolean isStudenteIscrittoACorso(Studente studente, Corso corso) {

		return studenteDao.isStudenteIscrittoACorso(studente, corso);
	}

	/*
	 * Iscrivi una studente ad un corso. Ritorna TRUE se l'operazione va a buon fine.
	 */
	public boolean inscriviStudenteACorso(Studente studente, Corso corso) {

		return corsoDao.inscriviStudenteACorso(studente, corso);
	}

	/*
	 * Ritorna tutti i corsi
	 */
	public List<Corso> getTuttiICorsi() {

		return corsoDao.getTuttiICorsi();
	}

}

package it.polito.tdp.lab04.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.lab04.model.Corso;
import it.polito.tdp.lab04.model.Model;
import it.polito.tdp.lab04.model.Studente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SegreteriaStudentiController {

	private Model model;
	List<Corso> corsi;

	@FXML
	private ComboBox<Corso> comboCorso;

	@FXML
	private Button btnCercaIscrittiCorso;

	@FXML
	private Button btnCercaCorsi;

	@FXML
	private Button btnCercaNome;

	@FXML
	private TextArea txtResult;

	@FXML
	private Button btnIscrivi;

	@FXML
	private TextField txtMatricola;

	@FXML
	private Button btnReset;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtCognome;

	public void setModel(Model model) {

		this.model = model;
		setComboItems();
	}

	private void setComboItems() {

		// Ottieni tutti i corsi dal model
		corsi = model.getTuttiICorsi();

		// Aggiungi tutti i corsi alla ComboBox
		Collections.sort(corsi);
		comboCorso.getItems().addAll(corsi);
	}

	@FXML
	void doReset(ActionEvent event) {

		txtMatricola.clear();
		txtResult.clear();
		txtNome.clear();
		txtCognome.clear();
		comboCorso.getSelectionModel().clearSelection();
	}

	/*
	 * Data la matricola, cerca nome e cognome
	 */
	@FXML
	void doCercaNome(ActionEvent event) {

		txtResult.clear();
		txtNome.clear();
		txtCognome.clear();

		try {

			int matricola = Integer.parseInt(txtMatricola.getText());
			Studente studente = model.getStudente(matricola);

			if (studente == null) {
				txtResult.appendText("Nessun risultato: matricola inesistente");
				return;
			}

			txtNome.setText(studente.getNome());
			txtCognome.setText(studente.getCognome());

		} catch (NumberFormatException e) {
			txtResult.setText("Inserire una matricola nel formato corretto.");
		} catch (RuntimeException e) {
			txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE!");
		}
	}

	/*
	 * Data una matricola devo stampare tutti i corsi a cui lo studente è iscritto.
	 */
	@FXML
	void doCercaCorsi(ActionEvent event) {

		txtResult.clear();

		try {

			int matricola = Integer.parseInt(txtMatricola.getText());

			Studente studente = model.getStudente(matricola);
			if (studente == null) {
				txtResult.appendText("Nessun risultato: matricola inesistente");
				return;
			}

			List<Corso> corsi = model.cercaCorsiDatoStudente(studente);

			StringBuilder sb = new StringBuilder();

			for (Corso corso : corsi) {
				sb.append(String.format("%-8s ", corso.getCodins()));
				sb.append(String.format("%-4s ", corso.getCrediti()));
				sb.append(String.format("%-45s ", corso.getNome()));
				sb.append(String.format("%-4s ", corso.getPd()));
				sb.append("\n");
			}
			txtResult.appendText(sb.toString());

		} catch (NumberFormatException e) {
			txtResult.setText("Inserire una matricola nel formato corretto.");
		} catch (RuntimeException e) {
			txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE!");
		}
	}

	/*
	 * Dato un corso, devo stamapre le informazioni su tutti gli studenti.
	 */
	@FXML
	void doCercaIscrittiCorso(ActionEvent event) {

		txtResult.clear();
		txtNome.clear();
		txtCognome.clear();

		try {

			Corso corso = comboCorso.getValue();
			if (corso == null) {
				txtResult.setText("Selezionare un corso.");
				return;
			}

			List<Studente> studenti = model.studentiIscrittiAlCorso(corso);

			StringBuilder sb = new StringBuilder();

			for (Studente studente : studenti) {

				sb.append(String.format("%-10s ", studente.getMatricola()));
				sb.append(String.format("%-20s ", studente.getCognome()));
				sb.append(String.format("%-20s ", studente.getNome()));
				sb.append(String.format("%-10s ", studente.getCds()));
				sb.append("\n");
			}

			txtResult.appendText(sb.toString());

		} catch (RuntimeException e) {
			txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE!");
		}
	}

	@FXML
	void doIscrivi(ActionEvent event) {

		txtResult.clear();

		try {

			if (txtMatricola.getText().isEmpty()) {
				txtResult.setText("Inserire una matricola.");
				return;
			}

			if (comboCorso.getValue() == null) {
				txtResult.setText("Selezionare un corso.");
				return;
			}

			// Prendo la matricola in input
			int matricola = Integer.parseInt(txtMatricola.getText());

			// (opzionale)
			// Inserisco il Nome e Cognome dello studente nell'interfaccia
			Studente studente = model.getStudente(matricola);
			if (studente == null) {
				txtResult.appendText("Nessun risultato: matricola inesistente");
				return;
			}

			txtNome.setText(studente.getNome());
			txtCognome.setText(studente.getCognome());

			// Ottengo il nome del corso
			Corso corso = comboCorso.getValue();

			// Controllo se lo studente è già  iscritto al corso
			if (model.isStudenteIscrittoACorso(studente, corso)) {
				txtResult.appendText("Studente già  iscritto a questo corso");
				return;
			}

			// Iscrivo lo studente al corso.
			// Controllo che l'inserimento vada a buon fine
			if (!model.inscriviStudenteACorso(studente, corso)) {
				txtResult.appendText("Errore durante l'iscrizione al corso");
				return;
			} else {
				txtResult.appendText("Studente iscritto al corso!");
			}

		} catch (NumberFormatException e) {
			txtResult.setText("Inserire una matricola nel formato corretto.");
		} catch (RuntimeException e) {
			txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE!");
		}
	}

	@FXML
	void initialize() {
		assert comboCorso != null : "fx:id=\"comboCorso\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert btnCercaIscrittiCorso != null : "fx:id=\"btnCercaIscrittiCorso\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert btnCercaCorsi != null : "fx:id=\"btnCercaCorsi\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert btnCercaNome != null : "fx:id=\"btnCercaNome\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert txtCognome != null : "fx:id=\"txtCognome\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert btnIscrivi != null : "fx:id=\"btnIscrivi\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert txtMatricola != null : "fx:id=\"txtMatricola\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";
		assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'SegreteriaStudenti.fxml'.";

		// Utilizzare questo font per incolonnare correttamente i dati
		txtResult.setStyle("-fx-font-family: monospace");
	}

}

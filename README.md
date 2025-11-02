# Gestore di Videoteca Personale

## 📚 Descrizione del Progetto

**Gestore di Videoteca Personale** è un'applicazione Java sviluppata per aiutare gli utenti a gestire una collezione personale di films. Utilizza Java 17 in versione "vanilla" e Swing per l'interfaccia grafica. Il progetto implementa numerosi design pattern per garantire una struttura solida, estendibile e manutenibile.

## 🎯 Obiettivi

* Gestione completa di una videoteca virtuale personale
* Interfaccia grafica intuitiva e responsive
* Persistenza dei dati su file (JSON, CSV)
* Applicazione dei principali design pattern
* Testing completo delle funzionalità principali

---

## 📊 Architettura e Design Pattern

Il progetto segue il pattern **MVC (Model-View-Controller)** e fa uso dei seguenti design pattern:

* **Singleton**: per la gestione centralizzata dei dati
* **Command**: per le operazioni Undo/Redo
* **Strategy**: per l'ordinamento dinamico dei films
* **DAO**: per la persistenza dei dati in diversi formati (JSON, CSV)

---

## 🔍 Funzionalità Principali

### 📖 Gestione Films

* Aggiunta, modifica e rimozione di un film
* Attributi: titolo, regista, anno di uscita, genere, valutazione (1-5 stelle), stato di visione (Visto, Da vedere, In visione)
* Validazione dei dati in input

### 🔎 Ricerca, Filtri e Ordinamento

* Ricerca per titolo, regista
* Filtri per genere, regista, stato visione e valutazione, anno di uscita
* Ordinamento per titolo (A-Z/Z-A), regista e valutazione

### 📀 Persistenza Dati

* Salvataggio e caricamento in formato JSON e CSV
* Gestione robusta degli errori di I/O

### 💻 Interfaccia Grafica (Swing)

* GUI user-friendly e responsive
* Feedback visivo per le operazioni

### 🔄 Undo/Redo

* Sistema completo di annullamento/ripetizione per tutte le operazioni

### ✅ Testing

* Test unitari con JUnit 5
* Test per Command, Controller, DAO, Model, Strategy
* Classe `RunAllTests.java` per eseguire l'intera suite

---

## 📂 Struttura dei Package

```
src
├── model
│   ├── Film.java
│   └── StatoVisione.java
├── view
│   ├── FilmView.java
│   └── DialogAggiungiModificaFilm.java
├── controller
│   ├── GestoreVideoteca.java
│   └── FilmController.java
├── dao
│   ├── FilmDAO.java
│   ├── JsonFilmDAO.java
│   └── CsvFilmDAO.java
├── command
│   ├── Command.java
│   ├── CommandManager.java
│   ├── AggiungiFilmCommand.java
│   ├── ModificaFilmCommand.java
│   └── EliminaFilmCommand.java
├── strategy
│   ├── OrdinatoreFilmStrategy.java
│   ├── OrdinaTitoloAZStrategy.java
│   ├── OrdinaTitoloZAStrategy.java
│   ├── OrdinaRegistaAZStrategy.java
│   ├── OrdinaRegistaZAStrategy.java
│   ├── OrdinaValutazioneAscStrategy.java
│   └── OrdinaValutazioneDescStrategy.java
├── test
│   ├── command/
│   ├── controller/
│   ├── dao/
│   ├── model/
│   ├── strategy/
│   └── RunAllTests.java
└── Videoteca.java
```

---

## 🛠️ Requisiti Tecnici

* Java 17 (Standard Edition)
* IDE consigliato: IntelliJ IDEA, Eclipse o NetBeans
* Librerie: Solo API Java standard (JUnit 5 per i test)

---

## ✨ Come Eseguire il Progetto

### 1. Clona il repository

```bash
git clone https://github.com/g-zangara/Videoteca.git
cd Videoteca
```

### 2. Importa il progetto in IntelliJ IDEA (o altro IDE)

### 3. Aggiungi JUnit 5 al progetto - Su IntelliJ IDEA

Per usare JUnit in un progetto Java vanilla su IntelliJ IDEA, **non serve installare un plugin separato**: JUnit è già supportato nativamente da IntelliJ. Devi solo aggiungere la libreria JUnit al tuo progetto.


Se **non** stai usando Maven o Gradle (Java vanilla):

* Vai su: `File > Project Structure > Modules > Dependencies`
* Clicca su `+` > `Library` > `From Maven...`
* Cerca:

  ```
  org.junit.jupiter:junit-jupiter:5.9.0
  ```
* IntelliJ scaricherà la libreria e la aggiungerà al classpath

### 4. Compila ed esegui l'applicazione

Esegui `Videoteca.java` per avviare l'interfaccia grafica principale.

### 5. Esegui i test

Puoi eseguire i test in due modi:

* Cliccando sull'icona verde accanto al metodo `@Test` o alla classe di test
* Oppure: tasto destro sulla classe di test > `Run 'RunAllTests.main()'`

Oppure esegui direttamente:

```bash
java test.RunAllTests
```

---

## 📅 Stato di Visione e Valutazione

* **Stati possibili**: `Visto`, `In visione`, `Da vedere`
* **Valutazione**: da 1 a 5 stelle con feedback visivo nella GUI

---

## 📄 Licenza

Questo progetto è distribuito sotto licenza **MIT**. Sentiti libero di utilizzarlo, modificarlo e condividerlo.

---

## 👤 Autori

Progetto sviluppato a scopo didattico come esercitazione di progettazione software in Java. Include best practice OOP, uso avanzato di Swing e design pattern.

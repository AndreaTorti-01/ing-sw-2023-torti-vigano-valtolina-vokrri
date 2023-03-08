# Progettazione UML

## UML Model
Per la progettazione del diagramma delle classi UML, abbiamo deciso di implementare una classe astratta globale **GameObject** che astrae tutti gli elementi di gioco. I probabili attributi height e width, in cm, verranno lavorati tramite scaling per ottenere la lunghezza in pixel.

Questa classe viene implementata dalle classi Tile (che rappresenta tutte le tipologie di tessere/carte), Board (che rappresenta il soggiorno in cui vengono posizionate le tessere oggetto) Bag (da cui vengono estratte le tessere in maniera randomica) e Shelf (che rappresenta la libreria in cui vengono inserite le tessere oggetto). 

A sua volta **Tile** rappresenta la super classe di tutte le tipologie di carte a disposizione nel gioco, tra cui:
- CommonGoalCard, ovvero le carte obbiettivo comune;
- PersonalGoalCard, ovvero le carte obiettivo personale;
- ItemCard, ovvero le tessere oggetto da posizionare nella libreria.

Le **ItemCard** possono a loro volta essere di diverse tipologie, come Cats, Games, Books, Trophies, Frames e Plants, da cui la decisione di creare un'enumerazione che ne indichi la tipologia.

Anche le **CommonGoalCard** possono essere di diversi tipi, elencati nella enumeration appropriata.
Il punteggio assegnato al raggiungimento di ciascun common goal si ottiene poppando o peekando la carta stessa, che implementa lo stack di punteggi

La **Board** è implementata tramite due matrici: una è la maschera delle caselle valide, l'altra contiene ItemCard e null.

Lo **Shelf** è implementato tramite una matrice, che contiene ItemCard e null.

La **PersonalGoalCard** non è altro che una matrice di ItemCard e null, proprio come shelf. Probabilmente verranno richieste dal controller e verranno confrontate con lo shelf del giocatore che le possiede.

Il **Bag** è implementato tramite un set di ItemCard (tutti puntatori diversi), e ha la funzione pop che estrae una carta o lancia eccezione se son finite le carte.

*Mancano tutte le relazioni che legano la classe player alle altre classi.*

---

DOMANDE:
- la funzione pop() nella classe Bag modifica lo stato della classe
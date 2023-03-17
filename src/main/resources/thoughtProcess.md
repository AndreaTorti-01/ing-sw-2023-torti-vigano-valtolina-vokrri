## UML - Model
Per la progettazione del diagramma delle classi UML del Model, abbiamo deciso d'implementare una classe astratta globale **GameObject** che astrae tutti gli elementi di gioco. I probabili attributi height e width, in cm, verranno lavorati tramite scaling per ottenere la lunghezza in pixel.

Questa classe viene ereditata da tutte le classi rappresentanti gli oggetti di gioco:

- ItemCard, ovvero le tessere oggetto da posizionare nella libreria.
- CommonGoalCard, ovvero le carte obbiettivo comune
- Board, che rappresenta il soggiorno in cui vengono posizionate le tessere oggetto
- Shelf, che rappresenta la libreria in cui vengono inserite le tessere oggetto 
- PersonalGoalCard, ovvero le carte obiettivo personale
- Bag, da cui vengono estratte le tessere in maniera randomica

Le **ItemCard** possono a loro volta essere di diverse tipologie, come Cats, Games, Books, Trophies, Frames e Plants, da cui la decisione di creare un'enumerazione che ne indichi la tipologia.

Anche le **CommonGoalCard** possono essere di diversi tipi, elencati nella enumeration appropriata.
Il punteggio assegnato al raggiungimento di ciascun common goal si ottiene poppando o peekando la carta stessa, che implementa lo stack di punteggi

La **Board** è implementata tramite due matrici: una è la maschera delle caselle valide, l'altra contiene ItemCard e null.

Lo **Shelf** è implementato tramite una matrice, che contiene ItemCard e null.

La **PersonalGoalCard** non è altro che una matrice di ItemCard e null, proprio come shelf. Probabilmente verranno richieste dal controller e verranno confrontate con lo shelf del giocatore che le possiede.

Il **Bag** è implementato tramite un set di ItemCard (tutti puntatori diversi), e ha la funzione pop che estrae una carta o lancia eccezione se son finite le carte.

*Mancano tutte le relazioni che legano la classe player alle altre classi.*

---

### Thought process di Burro e Fabio del 17 marzo sul Game (classe wrapper di model) e sul controller.

La classe Game, all'interno del model, è necessaria poiché conterrà le istanze di tutti gli oggetti di gioco: controller NON deve mai istanziare all'interno di se degli elementi di gioco al di fuori di game, ma si occuperà di chiamare i metodi appropriati per gestirli.

Controller "dà gli ordini" a Game: Game li esegue, sposta gli oggetti, li crea e li distrugge.

Come attributi, Game possiederà gli elementi di gioco, occupandosi di istanziarli, e informazioni sulla partita attuale, come un int che indichi di quale giocatore è attualmente il turno. I metodi saranno all'incirca gli stessi degli elementi di gioco che gestisce.

Potrebbe esistere un solo controller globale, che controlla più sessioni di gioco contemporanee, avendo incluso nei pacchetti di rete delle informazioni su quale sessione si sta cercando di controllare. Tra gli attributi di controller per ora immagino solo una lista di Game.

NOTA: probabilmente bisogna togliere personalgoalcard da player: non sono i player a "possedere" gli elementi di gioco (tra cui anche la shelf), ma è il game a sapere cosa appartiene a chi tramite dei banali indici. Shelf[0] apparterrà a Player[0] e così via
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

Come attributi, Game possiederà gli elementi di gioco, occupandosi di istanziarli. I metodi saranno all'incirca gli stessi degli elementi di gioco che gestisce.

Potrebbe esistere un solo controller globale, che controlla più sessioni di gioco contemporanee, avendo incluso nei pacchetti di rete delle informazioni su quale sessione si sta cercando di controllare. Tra gli attributi di controller per ora immagino solo una lista di Game. Eventualmente potremmo avere anche un controller per ogni sessione, e quindi l'elenco delle sessioni in main...

> - (ESEGUITO) probabilmente bisogna togliere personalgoalcard da player: non sono i player a "possedere" gli elementi di gioco (tra cui anche la shelf), ma è il game a sapere cosa appartiene a chi tramite dei banali indici. Shelf[0] apparterrà a Player[0] e così via

---

### Implementazione di Game - Diego
 
### Attributi

- **int playersNum** -> indica il numero di player che partecipano alla partita. passato come parametro da controller, e utilizzato per istanziare a dovere gli array e la board
- (NO) **Player[] players** -> possibile implementazione tramite array come suggerito da burro e fabio
- **Shelf[] shelves** -> indicizzato come a players
- **int[] scores** -> indicizzato come a players
- **PersonalGoalCard[] pgCards** -> indicizzato come a players // si istanziano solo le 2,3 o 4 che servono
- **Board board** -> // 
- **Bag bag** -> //
- il set di ItemCards disponibili è già nella bag (e anche rappresentato dalla bag) -> non aggiungo
- **CommonGoalCards[] cgCards** -> sarà un array di 2 elementi. //**NB**: ogni player può completare un obiettivo comune una sola volta.
- **boolean[][] cgAchieved** -> pensato come attributo per stabilire se un player abbia o meno completato un CommonGoal.(nella seconda implementazione il controllo è fatto tramite 2 booleani come attributi di player)
- **boolean[] pgAchieved** -> serve un array, non una matrice, essendoci solamente un CommonGoal per player

### Metodi

- **SetScore(int score, Player player)** nel caso decidessimo di metterlo come attributo di game
- **GetScore(int score, Player player)**
- **RefillBoard()** 
- **others**  -> getter e setter

> - (1) (ESEGUITO) andrebbero quindi cancellati gli attributi booleani di player che fanno riferimento al compimento degli obiettivi
> - (2) (ESEGUITO) questa implementazione utilizza degli array per memorizzare i player, e degli array indicizzati nello stesso modo per memorizzare punteggi, shelves e tessere personali.

Diego proponeva una seconda possibile implementazione in cui Player rivestiva un ruolo più importante, ma è stata scartata per le presunte difficoltà di passaggio parametri tra Game, Player e le classi possedute da Player come Shelf.

### Cri e Burro - 20 marzo

> - è stato rimosso Player poichè era rimasta una stringa
> - GameObject rimane un'incognita
> - rimane da decidere se i metodi di game devono astrarre gli oggetti di gioco o renderli trasparenti al controller
> - l'implementazione trasparente è banale: basta mettere un getter per ogni attributo (il setter non serve perchè degli attributi cambiano solo i loro attributi, non gli attributi stessi... del tipo non cambierò mai bag, il bag sarà sempre quello, sarà quello che c'è all'interno di bag a cambiare)
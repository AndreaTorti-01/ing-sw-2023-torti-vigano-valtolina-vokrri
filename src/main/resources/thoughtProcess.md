## UML - Model

Per la progettazione del diagramma delle classi UML del Model, abbiamo deciso d'implementare una classe astratta globale
**GameObject** che astrae tutti gli elementi di gioco. I probabili attributi height e width, in cm, verranno lavorati
tramite scaling per ottenere la lunghezza in pixel.

Questa classe viene ereditata da tutte le classi rappresentanti gli oggetti di gioco:

- ItemCard, ovvero le tessere oggetto da posizionare nella libreria.
- CommonGoalCard, ovvero le carte obbiettivo comune
- Board, che rappresenta il soggiorno in cui vengono posizionate le tessere oggetto
- Shelf, che rappresenta la libreria in cui vengono inserite le tessere oggetto
- PersonalGoalCard, ovvero le carte obiettivo personale
- Bag, da cui vengono estratte le tessere in maniera randomica

Le **ItemCard** possono a loro volta essere di diverse tipologie, come Cats, Games, Books, Trophies, Frames e Plants, da
cui la decisione di creare un'enumerazione che ne indichi la tipologia.

Anche le **CommonGoalCard** possono essere di diversi tipi, elencati nella enumeration appropriata.
Il punteggio assegnato al raggiungimento di ciascun common goal si ottiene poppando o peekando la carta stessa, che
implementa lo stack di punteggi

La **Board** è implementata tramite due matrici: una è la maschera delle caselle valide, l'altra contiene ItemCard e
null.

Lo **Shelf** è implementato tramite una matrice, che contiene ItemCard e null.

La **PersonalGoalCard** non è altro che una matrice di ItemCard e null, proprio come shelf. Probabilmente verranno
richieste dal controller e verranno confrontate con lo shelf del giocatore che le possiede.

Il **Bag** è implementato tramite un set di ItemCard (tutti puntatori diversi), e ha la funzione pop che estrae una
carta o lancia eccezione se son finite le carte.

*Mancano tutte le relazioni che legano la classe player alle altre classi.*

---

### Thought process di Burro e Fabio del 17 marzo sul Game (classe wrapper di model) e sul controller.

La classe Game, all'interno del model, è necessaria poiché conterrà le istanze di tutti gli oggetti di gioco: controller
NON deve mai istanziare all'interno di se degli elementi di gioco al di fuori di game, ma si occuperà di chiamare i
metodi appropriati per gestirli.

Controller "dà gli ordini" a Game: Game li esegue, sposta gli oggetti, li crea e li distrugge.

Come attributi, Game possiederà gli elementi di gioco, occupandosi di istanziarli. I metodi saranno all'incirca gli
stessi degli elementi di gioco che gestisce.

Potrebbe esistere un solo controller globale, che controlla più sessioni di gioco contemporanee, avendo incluso nei
pacchetti di rete delle informazioni su quale sessione si sta cercando di controllare. Tra gli attributi di controller
per ora immagino solo una lista di Game. Eventualmente potremmo avere anche un controller per ogni sessione, e quindi
l'elenco delle sessioni in main...

> - (ESEGUITO) probabilmente bisogna togliere personalgoalcard da player: non sono i player a "possedere" gli elementi
    di gioco (tra cui anche la shelf), ma è il game a sapere cosa appartiene a chi tramite dei banali indici. Shelf[0]
    apparterrà a Player[0] e così via

---

### Implementazione di Game - Diego

### Attributi

- **int playersNum** -> indica il numero di player che partecipano alla partita. passato come parametro da controller, e
  utilizzato per istanziare a dovere gli array e la board
- (NO) **Player[] players** -> possibile implementazione tramite array come suggerito da burro e fabio
- **Shelf[] shelves** -> indicizzato come a players
- **int[] scores** -> indicizzato come a players
- **PersonalGoalCard[] pgCards** -> indicizzato come a players // si istanziano solo le 2,3 o 4 che servono
- **Board board** -> //
- **Bag bag** -> //
- il set di ItemCards disponibili è già nella bag (e anche rappresentato dalla bag) -> non aggiungo
- **CommonGoalCards[] cgCards** -> sarà un array di 2 elementi. //**NB**: ogni player può completare un obiettivo comune
  una sola volta.
- **boolean[][] cgAchieved** -> pensato come attributo per stabilire se un player abbia o meno completato un
  CommonGoal.(nella seconda implementazione il controllo è fatto tramite 2 booleani come attributi di player)
- **boolean[] pgAchieved** -> serve un array, non una matrice, essendoci solamente un CommonGoal per player

### Metodi

- **SetScore(int score, Player player)** nel caso decidessimo di metterlo come attributo di game
- **GetScore(int score, Player player)**
- **RefillBoard()**
- **others**  -> getter e setter

> - (1) (ESEGUITO) andrebbero quindi cancellati gli attributi booleani di player che fanno riferimento al compimento
    degli obiettivi
> - (2) (ESEGUITO) questa implementazione utilizza degli array per memorizzare i player, e degli array indicizzati nello
    stesso modo per memorizzare punteggi, shelves e tessere personali.

Diego proponeva una seconda possibile implementazione in cui Player rivestiva un ruolo più importante, ma è stata
scartata per le presunte difficoltà di passaggio parametri tra Game, Player e le classi possedute da Player come Shelf.

### Cri e Burro - 20 marzo

> - è stato rimosso Player poiché era rimasta una stringa
> - GameObject rimane un'incognita
> - rimane da decidere se i metodi di game devono astrarre gli oggetti di gioco o renderli trasparenti al controller
> - l'implementazione trasparente è banale: basta mettere un getter per ogni attributo (il setter non serve perchè degli
    attributi cambiano solo i loro attributi, non gli attributi stessi... del tipo non cambierò mai bag, il bag sarà
    sempre quello, sarà quello che c'è all'interno di bag a cambiare)
> - il giorno dopo, con Fabio, è stato deciso di optare per l'implementazione che non astrae gli oggetti di gioco, ma ne
    usa i relativi metodi perché i metodi che astraggono sono già stati implementati nei relativi oggetti e non avrebbe
    senso aggiungere un secondo livello di astrazione.

### Idea di Andrea del 26 marzo

Per risolvere il "problema" delle sprite dei gatti, si potrebbe prendere in modulo l'hash dell'oggetto, l'output del
"tostring" insomma. Non so se va bene che non ci sia lo stesso numero di tutti i tipi di gatti, ma essendo qualcosa
di legato solo alla view, potrebbero non esserci problemi.

---

### Cristiano vi spiega le sue terribili implementazioni dei sottocasi del metodo checkCommonGoalCardPattern in GameController - 29 marzo

> - SIX_PAIRS (probabilmente il più complesso di tutti ma a me piace)

L'idea di base si basa sul creare per ogni itemType una maschera diversa e contare per ogni mask il numero di coppie e
sommarle tutte.

(devo prima introdurvi il concetto di coppia: dopo un'attenta analisi con dede delle discussioni su slack siamo giunti
alla conclusione che conta come coppia ogni gruppo di tessere adiacenti dello stesso tipo di dimensione >=2 o almeno
così ho capito io e spero sia giusto se no anche questo è tutto da rifare.)

Una volta chiarito ciò ho implementato il metodo chiamato *number_of_pair_in_mask* che funziona nel seguente modo:

modifica un attributo di mask chiamato *matrix_adjacent* che é una matrice dove gli elementi di mask adiacenti prendo un
numero venendo numerati per gruppetti così che il numero di gruppi sia il numero di coppie valide

ho bisgono di voi però perche il codice non mi da errori però sono consapevole che nel momento di test ci dara errore
perche io lavoro con il concetto di adiacenza che sono le tessere sopra-sott-destra-sinistra ma le celle sui bordi delle
matrici non hanno tutte e 4 le adiacenze e vorrei capire come gestire il ritorno
quando un elemento della matrice confronta uno "out of bound" va beh chiamatemi quando avete il tempo che vi espongo
meglio il dubbio

> - CROSS (credo corretto)

Semplicemente esamino la presenza di una croce supponendo che l'elemento da esaminare sia quello in posizione centrale
della croce
così da non dover esaminare i bordi della matrice e scorro tutti gli elementi della matrice(esclusi i bordi appunto)
controllando i 4 elementi diagonali a quello in esamine abbiano il suo stesso tipo

> - DIAGONAL_FIVE (corretto ma si può scrivere più carino)

semplicemente partendo dai due elementi centrali per cui passano le diagonali ispeziono le 4 possibili diagonali
controllando se almeno una di esse sia composta da elementi dello stesso tipo

> - TWO_SQUARES (era bello ma da rifare)

> - FOUR_LINES_MAX_THREE_TYPES (ancora da fare)

> - EQUAL_CORNERS (fatto)

mooolto semplice controlla che gli elementi ai 4 angoli della shelf siano dello stesso tipo

> - TWO_RAINBOW_COLUMNS (fatto)

controlla che esistano almeno due colonne con un elemento per ogni tipo creando un set con gli itemtype e rimuovendo
quelli incontrati controllando alla fine di averne rimossi 6 e refillando il set ogni volta che cambio colonna da
controllare

> - TWO_RAINBOW_LINES (fatto)

stesso concetto del *TWO_RAINBOW_COLUMNS* ma implementato sulle righe

> - THREE_COLUMNS_MAX_THREE_TYPES (da fare)

> - EIGHT_EQUAL (fatto credo si possa fare più carino)

io ho banalmente creato 6 contatori uno per tipo, scorro tutta la shelf incrementando i contatori e alla fine controllo
se qualcuno è arrivato a 8

> - STAIR (fatto)

creato degli array in cui inserito tutte le celle che formano le scale e le corrispettive sopra
e controllo che quelle sopra siano vuote e quelle che formano le scale piene

---

### Burro è tornato: 3 aprile

A mente lucida, leggendo la review del nostro uml e anche il model inviatoci, capisco che il progetto può essere reso
più object-oriented.

### Il grande refactoring del model

- Fabio sta implementando la classe **player** e facendo modifiche a **shelf**, che sposterà i possedimenti di ogni
  giocatore da **game** a **player**,
  e contribuirà ad alleggerire **game**
- Io mi occupo di spostare il codice di check delle CommonGoalCard in 12 classi separate, seguendo lo _Strategy
  Pattern_, in modo da alleggerire il **gamecontroller**. Ho reso CommonGoalCardStrat come un'interfaccia che implementa
  come "default default" (visibilità default, implementazioni default) alcuni metodi di utilità usati dagli altri
  ragazzi.
- Il context è CommonGoalCard, la Strategy è CommonGoalCardStrat, le concretes sono 12 classi, e Client è Game. Lascio
  irrisolto al momento il problema di istanziare una strategia random da parte di Game per essere assegnata a
  CommonGoalCard, e anche la riscrittura di test strutturati...
- **Il giorno dopo** (4 aprile) ho istanziato la strategia random in game tramite l'uso di Class.forName e .newInstance
  e l'ho passata a CommonGoalCard, rimane da scrivere il test. Finalmente è chiaro come raggruppare gli algoritmi delle
  commongoalcards.

## Gli algoritmi delle common goal card vengono riscritti da capo

Finalmente si decide di ricominciare a scrivere gli algoritmi a partire dall'idea di trovare pattern ricorrenti: Stair,
Cross, Diagonal_Five, Equal_Corners e Two_Squares vengono riuniti sotto classe **SHAPE**, e la *shape* da ricercare
viene salvata sotto un file .txt codificato come segue:

```
width height
SHAPE_MATRIX
mirror_hor num_of_shapes
```

L'algoritmo è debitamente commentato, non verrà rispiegato qui, la classe Shape è un record ed è stato rimosso l'elenco
di CGCs da constants...

Sto pensando di rimuovere getValues da CommonGoalCardType per ridondanza... anche GameObject lol

---

### PRIMISSIMA BOZZA DEL CONTROLLER

>COSE DA DECIDERE:
> capire se la gestione delle iscrizioni dei giocatori alla partita e la creazione della board viene gestita dal client o 
> controller

- Prima di ogni turno:

   _ identificare il giocatore del turno corrente e stabilire se sia o meno l'ultimo turno da giocare

- Durante il turno:

   _ stabilire le tessere prendibili (sia sulla base della board che della shelf del giocatore in turno)
   
   _ giocatore seleziona le tessere

   _ le tessere vengono rimosse dalla board

   _ stabilire le colonne della shelf del giocatore corrente in cui si possono inserire le tessere scelte

   _ il giocatore sceglie la colonna e le tessere vengono inserite in quest'ultima

   _ invocazione metodi check common goal card

   _ in caso di CgC raggiunto aggiornare punteggi

- Alla fine di ogni turno:

  _ controllare se qualcuno ha riempito completamente la board

  _ controllare se era l'ultimo turno

  _ controllare se bisogna refillare la board e nel caso refillarla

- Se stabilito che era l'ultimo turno:

  _ assegnare i punteggi contando PersonalGoalCard, primo a finire, CommonGoalCard, points adjacent

  _ decretare vincitore/podio






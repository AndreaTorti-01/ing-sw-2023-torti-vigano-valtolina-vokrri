# Peer-Review 1: UML

Andrea Torti, Cristiano Valtolina, Diego Viganò, Fabio Vokrri

Gruppo GC33

Valutazione del diagramma UML delle classi del gruppo GC23.

## Lati positivi

Il gruppo revisionato secondo il nostro parere ha suddiviso molto bene le classi, in modo tale che ognuna di esse si occupi di una piccolo aspetto del gioco. Inoltre è stato fatto buon uso dei modelli di design pattern studiati a lezione: si è fatto un ottimo uso del pattern Factory per la creazione della board di gioco a inizio partita e del pattern Strategy per la suddivisione dei metodi di check dei diversi delle CommonGoalCard.

Abbiamo apprezzato molto l'idea d'implementare una enumerazione che tenga descriva lo stato in cui si trova il gioco e allo stesso modo l'enumerazione che definisce lo stato di ogni cella della board centrale (libera, occupata o disabilitata).
Nel caso di aggiunta di stati di gioco o di una variazione nelle dinamiche di funzionamento delle celle, è possibile infatti apportare modifiche al codice in maniera molto semplice.

## Lati negativi

Le classi presentano molti metodi che, a parer nostro, non dovrebbero far parte della sezione model: vengono infatti implementati metodi che si addicono maggiormente a controller e view.

Inoltre sono presenti alcune classi che a parer nostro complicano l'implementazione rendendola troppo verbosa, come ad esempio:

- EndGameToken
- ScoringToken
- Square

I primi due possono essere eliminati in quanto la classe Player già tiene conto del punteggio all'interno di un proprio attributo.
La classe Square invece può essere rimpiazzata direttamente con la classe Tile all'interno della board, semplificandone l'implementazione.

## Confronto tra le architetture

Mettendo a confronto le due architetture, la nostra implementazione del model risulta essere più semplice e leggibile poiché racchiude i soli metodi essenziali con il compito di esporre lo stato corrente del gioco e, solo in alcuni rari casi, di modificarlo. Tutta la logica più complessa verrà implementata invece all'interno del controller, che si occuperà di modificare lo stato del nostro model e di osservare la view. Nel fare ciò il controller disporrà di metodi molto più articolati, ognuno dei quali farà uso di più metodi del model. 

Sicuramente un punto di forza del gruppo revisionato sta nell'aver modellizzato ogni singolo aspetto di gioco utilizzando classi apposite, attenendosi maggiormente ai concetti della programmazione ad oggetti. 
Diversamente, nella nostra implementazione si è deciso, per questioni di efficienza e immediatezza nella gestione dei parametri, di condensare alcuni attributi propri di player all'interno della classe Game, modellizzando shelf, punteggi, personal e common goal cards tramite array unidimensionali.

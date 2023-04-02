# Peer-Review 1: UML

Andrea Torti, Cristiano Valtolina, Diego Viganò, Fabio Vokrri

Gruppo GC33

Valutazione del diagramma UML delle classi del gruppo GC23.

## Lati positivi

Il gruppo revisionato secondo il nostro parere ha suddiviso molto bene le classi facendo in modo che ognuna di esse si
occupi di una piccolo aspetto del gioco.

É stato inoltre fatto ottimo uso dei modelli di design pattern studiati a lezione: del pattern **Factory** per la
creazione
della board di gioco a inizio partita e del pattern **Strategy** per la suddivisione dei metodi di check dei diversi
delle `CommonGoalCard`s.

Abbiamo altresì apprezzato sia l'idea d'implementare una enumerazione che descriva a runtime lo stato di evoluzione del
gioco, che l'enumerazione descrivente lo stato di ogni cella della board centrale (libera, occupata o disabilitata). Nel
caso di aggiunta di stati di gioco o di una variazione nelle dinamiche di funzionamento delle celle, è possibile infatti
apportare modifiche al codice in maniera molto semplice, aggiungendo o modificando le entry della enumerazione.

## Lati negativi

Le classi presentano molti metodi che, a parer nostro, non dovrebbero far parte della sezione model: ne vengono infatti
implementati alcuni che si addicono maggiormente a controller e view.

Inoltre sono presenti alcune classi che complicano l'implementazione rendendola troppo verbosa o ridondante, come ad
esempio:

- `EndGameToken`, ovvero la classe che rappresenta il punto aggiuntivo assegnato al primo giocatore che completa la
  propria shelf;
- `ScoringToken`, ovvero la classe che rappresenta il punteggio di una carta obiettivo comune e obiettivo personale;
- `Square`, ovvero la classe che rappresenta un singolo quadrato della board.

La prima classe può essere eliminata in quanto l'informazione che rappresenta può essere inserita all'interno delle
classi che modellano le carte obiettivo comune e le carte obiettivo personale: infatti, la classe citata contiene al suo
interno solamente un attributo di tipo intero, che descrive il punteggio, e il relativo getter.

A supporto della nostra tesi, la classe `Player` implementa sia un attributo `score`, che tiene conto del punteggio del
giocatore in questione, sia un attributo `listScoringToken`, che tiene conto dei vari token ottenuti dal giocatore,
rendendo ridondante uno dei due attributi.

La classe `EndGameToken`, invece, potrebbe a sua volta essere eliminata perché il punto aggiuntivo assegnato al primo
giocatore che completa la propria shelf dovrebbe essere assegnato dal controller, rendendone vano l'utilizzo.

Infine, la classe `Square` può essere rimpiazzata direttamente con la classe `Tile` all'interno della board,
semplificandone l'implementazione.

## Confronto tra le architetture

Mettendo a confronto le due architetture, la nostra implementazione del model risulta essere più semplice e leggibile
poiché racchiude i soli metodi essenziali con il compito di esporre lo stato corrente del gioco e, solo in alcuni rari
casi, di modificarlo. Tutta la logica più complessa verrà implementata invece all'interno del controller, che si
occuperà di modificare lo stato del nostro model e di osservare la view. Nel fare ciò il controller disporrà di metodi
molto più articolati, ognuno dei quali farà uso di più metodi del model.

Sicuramente un punto di forza del gruppo revisionato sta nell'aver modellato ogni singolo aspetto di gioco
utilizzando classi apposite, attenendosi maggiormente ai concetti della programmazione ad oggetti.
Diversamente, nella nostra implementazione si è deciso, per questioni di efficienza e immediatezza nella gestione dei
parametri, di condensare alcuni attributi propri di player all'interno della classe `Game`, modellando `Shelf`,
punteggi,`PersonalGoalCard`s e `CommonGoalCard`s tramite array unidimensionali.

Ci piacerebbe, quindi, modellare il giocatore tramite una specifica classe `Player`, che tenga conto delle interazioni
fra l'entità in questione e gli oggetti di gioco ad essa relativa. 

La nostra idea sul funzionamento del protocollo di rete è la seguente:
il giocatore avvia la classe ServerApp che ha il solo compito di creare un nuovo Server, il cui compito è quello di
accettare nuovi client in connessione. Inoltre, il giocatore avvia la classe ClientApp a cui è delegato il compito di
creare un nuovo Client.

La classe Client istanzia una nuova View (TUI o GUI) e richiede una connessione al server; il server accetta la
richiesta del client e lo inserisce all'interno di una lobby di giocatori (dai due ai quattro), se esistente, altrimenti
ne istanzia una nuova. La lobby ha il compito di istanziare il model (nel nostro caso la classe Game) e il controller (
GameController) per quella sessione di partita.

Oltre ad accettare il client e inserirlo dentro la lobby, il server ha il compito di istanziare anche un nuovo
ClientHandler, per ogni client che richiede connessione, che ha il compito di comunicare con il client assegnatogli e
inviare .
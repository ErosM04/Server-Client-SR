# Server-Client-SR

## Materia
Sistemi e Reti Laboratorio


## Consegna
La consegna richiede di implementare un’applicazione che gestisca la comunicazione tra Client e Server sulla stessa
rete, finalizzata alla compilazione di un elenco di nominativi di persone, a cui viene aggiunto il numero di telefono,
fornito dal Server.
Il primo dei due attori è il **Client**, il quale deve compilare un file di testo **DatiClient.csv** che contiene
l’elenco degli
utenti, identificati da un numero di matricola, il nome e il cognome.
Formato della riga con i dati dell’utente: ``matricola;cognome;nome``
L’informazione da aggiungere è il numero di telefono, che va richiesto al Server attraverso dei pacchetti che utilizzano
il protocollo TCP e contengono il numero di matricola dell’utente.
Il Server risponderà poi con il numero di telefono, se presente, altrimenti, se non presente, restituirà ``null``.
I dati degli utenti già in possesso vanno aggiunti in un nuovo file di testo, **DatiUtenti.csv**, insieme ai numeri di
telefono degli utenti, man mano che questi ultimi vengono ricevuti.
Formato della riga con i dati completi dell’utente: matricola;cognome;nome;numero di telefono
Una volta compilato il file dei dati degli utenti per intero, il Client termina la sua esecuzione.
Per evitare che il Client rimanga in attesa inutilmente nel caso in cui il Server sia assente o termini improvvisamente,
è necessario che dopo 30 secondi di attesa interrompa l’esecuzione.

Il secondo attore è il **Server** che una volta eseguito si mette in attesa del collegamento da parte di uno o più Client.
Una volta collegato, il Server riceve le richieste del Client, contenenti le matricole degli utenti, e successivamente
per ogni matricola cerca nel file di testo **DatiServer.csv** la ricorrenza e risponde con il corrispondente numero di
telefono, se presente, mentre se non presente risponde con ``null``.
Formato della riga con i dati dell’utente: ``matricola;numero di telefono``
Per evitare che il Server rimanga in attesa inutilmente nel caso in cui il Client sia assente o termini improvvisamente,
è necessario che dopo 3 minuti senza ricezione di richieste il Server arresti l’esecuzione.


## TCP
Per la trasmissione di tipo **connection oriented** è stato utilizzato uno stream socket che sfrutta il protocollo 
**TCP**.
Nato nel 1970 grazie al lavoro di un gruppo di ricerca del Dipartimento di Difesa Statunitense, il **Transmission
Control
Protocol** è un protocollo di rete di Layer 4, layer di trasporto, del protocollo ISO/OSI.
Il suo compito è controllare la trasmissione tra gli host è sopperire alle mancanze del protocollo IP di Layer 3, il
quale essendo best-effort non fornisce affidabilità riguardante l’arrivo dei pacchetti.
Il TCP è un protocollo connection-oriented, ovvero che necessita di stabilire una preconnessione per far comunicare 2
host;
la connessione viene mantenuta aperta anche in assenza di dati e viene poi chiusa quando non più necessaria.
Il protocollo, oltre a permettere la comunicazione contemporanea di entrambi i dispositivi coinvolti, ovvero in **Full
Duplex**, fornisce anche un controllo di errore sui pacchetti grazie al campo checksum presente nel PDU di Layer 4
(segment).
La sua caratteristica primaria rimane comunque la consegna dei segmenti attraverso il meccanismo di **acknowledgement**, che
permette di garantire la consegna dei pacchetti.
L’acknowledgement è un segnale inviato in risposta alla ricezione completa di un'informazione, che informa il
dispositivo trasmittente che la comunicazione è andata a buon fine, se così non è il dispositivo reinvia i pacchetti.
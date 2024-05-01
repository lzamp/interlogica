# PhoneValidator
Il programma permette di passare in input un file csv e ritorna in output una tabella con i numero suddivisi in validi, coretti e invalidi.
Il file deve essere così formato: un identificativo univoco e numeri di cellulare, separati da virgola. 
Inoltre è possibile insierire un unico numero di telefono e capire se valido o meno. 
I criteri sono che il numero deve avere lunghezza 11 e iniziare con 27 in questo caso, ma può essere adattato il codice in base al prefisso di ogni paese. 

## Struttura progetto 
Il DB utilizzato è MySQL che è stato istanziato in un'immagine Docker che vedremo in seguito. Si è prevista la presenza di due tabelle
- daticsv -> Contiene tutti i dati originali più una colonna di data che permette di sapere quando quel determinato dato è stato caricato. I dati salvati qui sono sia proveniente da eventuali csv sia i singoli numeri di telefono.
- phone_numbers -> Contiene tutti i dati modificati e non. Qui le colonno sono id, phone_number, status e sempre una data che tiene lo storico.
La prima tabella si poteva anche non creare elaborando subito i dati e salvandoli solo nella seconda tabella. La prima è un backup per vedere se gli stati sono corretti. Inoltre perché, da requisito, viene richiesto "Visualizzare i risultati nel modo seguente: dividere gli stati come segue: a. Visualizzazione accettabile numeri
                      b. Numeri corretti + cosa è stato modificato
                      c. Numeri errati."
Per rendere il tutto più veloce e leggero a livello applicativo, è preferibile eseguire delle query semplici su tabelle contenti già i dati come me li aspetto in output.

Per la parte di backend è stato utilizzato SpringBoot dove troviamo le seguenti cartelle
- config-> main standard per effettuare la chiamata al progetto
- controller -> E' presente una sola classe PhoneNumberController dove sono presente 3 metodi
  - uploadCSVFile(@RequestParam("file") MultipartFile file)  -> POST che prende in input il file csv grazie alla dipendenza org.springframework.web.multipart.MultipartFile. Il file viene scorso e ogni valore salvato in un oggetto DataEntry di tipo entity. Il                   contenuto di questo oggetto viene poi salvato a DB utilizzando "dataEntryRepo.save(record);".
  - controllAndsavePhoneNumber(@PathVariable String phoneNum) -> GET che viene utilizzata per salvare ed elaborare il singolo numero utilizzando phoneNumberService.saveNumber(phoneNum). Viene inultre richiamato un secondo metodo di tipo service 
                "phoneNumberService.extractElaboratedNumber(phoneNum);" per estrarre il singolo numero elaborato e ritornarlo in output sempre come requisito.
  - extractAll() -> GET utilizzata dopo la riusccita del primo servizio POST per restituire tutti i dati caricati in output rispettando la suddivisione richiesta.
- service -> Sono presenti 5 metodi differenti con le logicche di elaborazione del dato o di semplice chiamata ai metodi DAO.
  - PhoneNumberValidationResult validateAndCorrect(String phoneNumber); -> E presente la logica per validare i numeri di telefono. Una volta elaborati vengono salvati nell'oggetto PhoneNumberValidationResult di tipo DTO.
  - boolean saveNumbers() -> Viene utilizzato per salvare i dati passati tramite csv nella tabella phone_numbers. Come prima cosa richiama phoneNumberDAO.extractPhoneNumber(); che va ad estrarre tutti i dati presenti nella tabella daticsv. Per completezza qui 
             si dovrebbe utilizzare la colonna di data per non estrarre i dati già elaborati oppure l'ultimo id salvato in phone_numbers. Pe com'è al moemnto il codice quest non è gestito. Li salva nell'oggetto DataEntry che viene succcessivamente scorso e richiamato il              metodo presente sopra. Viene poi popolato l'oggetto PhoneNumber, sempre di tipo Entity e richiamato il corrispettivo oggetto repository per caricare i dati a DB.
  - boolean saveNumber(String phoneNumber) -> Metodo simile a quello sopra solo che qui si va ad utilizzare questa logica di DAO phoneNumberDAO.saveNumber(phoneNum); per verificare se il numero è stato salvato correttamente in daticsv, se si             
              phoneNumberDAO.extractPhoneNumberInput(phoneNum); per estrarre solo i record corrispodenti a quel nuemro di telefono. L'elaborazione sotto è la stessa.
  - List<PhoneNumber> extractElaboratedNumber(String phoneNum) -> Va a richiamare quetso metodo phoneNumberDAO.extractElaboratedNumber(phoneNum) che estrae solo le info dalla tabella phone_numbers relative al numero passato in input 
  - PhoneNumberExtraction validatePhoneNumbers() -> richiama i seguenti metodi: phoneNumberDAO.fetchAcceptableNumbers() ,phoneNumberDAO.fetchCorrectedNumbers(), phoneNumberDAO.fetchIncorrectNumbers(). Serve per andare a popolare l'oggetto PhoneNumberExtraction 
              utilizzato per l'output di extractAll().
- dao -> Sono presenti 9 metodi che vanno ad eseguire le query per l'estrazione dei dati. 
  -  List<DataEntry> extractPhoneNumber() - > Va ad estrarre tutti i record presenti nella tabella daticsv utilizzato poi nel service saveNumbers(). 
  - List<DataEntry> extractPhoneNumberDate(Date dateLoad) -> Non viene utilizzato ed è pensato per un eventuale logica di estrazione da daticsv di soli dati a partire da una certa data
  - Date extractLastDate(); -> Non viene utilizzato. Pesato per estrarre l'ultima data scritta su daticsv da passare al metodo sopra. Si poteva sintetizzare il tutto in un'unica query in relatà.
  - List<DataEntry> extractPhoneNumberInput(String phoneNumber) -> Va ad estrarre da daticsv tutti i record salvati a partire dal numero di telefono passato in input.
  - List<PhoneNumber> extractElaboratedNumber(String phoneNum) -> Va ad estrarre da phone_numbers in join con daticsv tutti i record salvati a partire dal numero di telefono passato in input. La join serve perché se il numero di telefono è stato modificato, il 
            sistema non lo sa quindi per poter estrarre qualcosa devo passare da daticsv che contiene i dati orginali.
  - boolean saveNumber(String phoneNum) -> Utilzzato per eseguire una insert nella tabella phone_numbers per salvare il singolo record passato. QUi essendo il record insierito singolarmente é presente una logica di creazione della chiave id. 
            Va a prendere il max id presente nella tabella dticsv e incremnta di 1 il valore.
  - List<String> fetchAcceptableNumbers(), Map<String, String> fetchCorrectedNumbers(), List<String> fetchIncorrectNumbers() -> Questi 3 metodi hanno uan logica simile che si diversifica solo per il filtro passato a status. Sono utilizzati per estrarre tutti i dati che 
            hanno corrispettivamente lo stato a invalid, acceptable e corrected 

Nella cartella "resources" sono prsenti due cartelle "static" e "templates" che contengono l'interfaccia grafica per che richiama le API. In "templates" troviamo la pagina html che da la struttura al frontend e richaiam al suo interno delle funzioni in javaScript che vanno ad eseguire le chiamate vere e proprie delle API. Lo script js lo si trova nella cartella static.js dove vengono richiamati di metodi controller ed elaborati gl output in taballe .


## Docker 
Il file dockercompose contiene le informazioni specifiche per la creazione sia dell'immagine del DB sia della parte di API in uno stesso container Docker. 
Le connessionei al DB sono contenute in un file apposito per non scriverle in chiaro a codice. Il file docker-compose.yaml, .env e Dockerfile devono essere nella stessa cartella.

I comandi devo essere eseguiti nella cartella dove si trovano i file di Docker necessari per creare il container e relative immagini, che sono:
`docker-compose build `
Una volta che la build è andata a buon fine si avviano con
`docker-compose up`


Per poter accedere all'interfaccia web basta accedere all'url http://localhost:8080/


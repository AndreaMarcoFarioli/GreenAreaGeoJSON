Andrea Marco Farioli

# Aree Verdi - Torino



## Indice

1. Introduzione al lavoro
2. Analisi dei dati
3. Analisi del programma
4. Presentazione delle classi
5. Gestione della grafica
6. Conclusione



## 1. Introduzione al lavoro

1) A partire dai siti di ARPA Piemonte o Torino Respira, cercare degli "open data" riguardo a qualità dell'aria o su argomenti che riguardano i cambiamenti climatici o l'ambiente in generale. Cercare i dati in un formato che sia csv oppure json o xml. Comprendere bene il significato dei dati e il loro formato.
Per fare questo vi sono materiali online ed esempi già postati su classroom.

2) Progettare e scrivere un programma java che, tramite interfaccia GUI legga il file con i dati, li elabori e visualizzi in una forma grafica i dati stessi, in modo da poterli interpretare e poi discutere.
La lettura da file può essere utilizzata per inserire i vari dati in un vettore (in modalità pila o coda a vostra scelta). Potete usare sia la casse Vector che ArrayList a scelta.
Avete ormai la conoscenza degli strumenti e delle metodologie per cimentarvi in questo progetto.



## 2. Analisi dei dati

Il dato utilizzato mappa, logicamente, le aree verdi di Torino dividendole per **Lotto, Cit., Ambito, Gestione (Comunale o non), Tipo (che indica la tipologia di verde) e Denominazione**.

Questi dati sono rappresentati seguendo un formato usato spesso per la cartografia, il **.geojson**, questo particolare formato è utile in ambito web, affiancato a JavaScript, perché ti permette di costruire forme per la divisione in aree e ad ogni forma puoi dare un particolare significato, un insieme di proprietà (properties), che lo distingue da altre aree.

La gestione delle feature, nome dato ad ogni singolo oggetto che poi conterrà le coordinate, è molto comoda e intuitiva: 

- Si definisce la tipologia della feature
- Si descrivono le proprietà che ha la feature
- Si descrive la Geometria della feature, ovvero:
  - Per ogni geometria è necessario indicare che tipologia è (Poligono, Punto, Segmento, ecc.)
  - A seconda della geometria selezionata la matrice che contiene le coordinate può variare di dimensione
    - Per esempio per il poligono, come in questo caso, le dimensioni sono 3, la prima contenitore, la seconda che definisce la forma primaria e la terza che indica l'esclusione insiemistica.

Il geojson principalmente si divide allora in 4 classi, di cui 1 non standardizzata:

- **GeoJSON**: La classe generica che racchiude tutto il file
- **GeoJSONFeature**: La classe che conterrà ogni singola feature del file
- **GeoJSONGeometry**: La classe che sarà contenitore delle coordinate
- E la classe contenuta dentro le properties che è di definizione non generalizzata

```json
{
  "type": "FeatureCollection",
  "features": []
}
```

Esempio di geojson vuoto

```json
{
  "type": "FeatureCollection",
  "features": [
      {
          "type": "Feature",
          "properties": {
              //Green_Area
          },
          "geometry":{
              "type": "Polygon",
              "coordinates": [
             	[
                    [
                        8.578948974609375,
                        44.88603949514269
                    ],
                    [
                        8.61328125,
                        44.89187715629887
                    ],
                    [
                        8.606414794921875,
                        44.90841397875737
                    ],
                    [
                        8.5748291015625,
                        44.90063253713748
                    ],
                    [
                        8.578948974609375,
                        44.88603949514269
                    ]
                ]
              ]
          }
      }
  ]
}
```

Esempio di geojson di un'area quadrilatera nelle vicinanze di Alessandria. Provare ed incollare [qui](http://geojson.io/#map=10/44.8330/8.6620)

Tuttavia non basta tradurre solo ogni feature, perciò e' necessario definire la struttura delle properties e creare una classe Java equivalente.

```json
"properties":{
    "CIT": 0,
    "DENOMINAZIONE": "CORSO REGIO PARCO",
    "AMBITO": "CORSO REGIO PARCO",
    "TIPO": "BANCHINA ALBERATA",
    "LOTTO": "6",
    "GESTIONE": "COMUNALE"
}
```

Esempio di Properties usato nel geojson delle Aree Verdi di Torino



## 3. Analisi del programma

Il programma deve permettere:

- Importazione dei dati in formato **GeoJSON** con estensione file .**geojson**
  - Da **file** o da **URL**
- Conversione dei dati in classi utilizzabili da **Java**
  - Utilizzare la libreria di Google **GSON**
- Tabella per la visualizzazione dei dati, possibilità di **filtrare** i contenuti
- **Colonna** dei **filtri**
  - Creazione di una **HideList** per poter nascondere contenuti alla **AbstractTable** della Tabella.
- Possibilità di **ricerca**
  - In **tutti** i **campi**, in futuro aggiunta selezione dei campi
- **Modifica** della **riga** della **tabella**, con modifica effettiva anche nel modello
- Sincronizzazione model, controller e view (**MVC**)
- **Esportazione** dei dati in **geojson**
- Creazione di un **grafico** basato sul numero delle **Aree** **Verdi** per **Lotto**
  - **Esportazione** del grafico in **png**


Per l'importazione dei file tramite URL ho dovuto usare la classe URL contenuta in **java.net**, all'inizializzazione essa ritorna una eccezione MalformedURLException che estende le IOException.

```java
/**
* MVC: Model
* Downloads a file from the web, the method uses bufferObject to return the results
* and the errorBuffer to return an error
* @param u url file
* @return answer type. 0 ok, -1 Net error, -2 MalformedURLException or IOException
*/
public short getFileUrl(String u){
    short return_value = 0;
    try {
        URL url = new URL(u);
        String s = url.openConnection().getHeaderField(0);
        if(Objects.equals(s, "HTTP/1.1 200 OK"))
            bufferObject = url.openStream();
        else{
            errorBuffer = s;
            return_value = -1;
        }
    } catch (IOException e) {
        errorBuffer = e.toString();
        return_value = -2;
    }
    return return_value;
}
```

Per l'importazione tramite file mi sono servito di JFileChooser un JDialog che permette di navigare attraverso il file system, la chiamata al metodo e' sincrona quindi si stoppa il Thread che sta mostrando la finestra. Il metodo showOpenDialog(Frame frame) ritorna un File, quindi quando si genera il FileInputStream bisogna comunque gestire l'eccezione IOException

```java
/**
* MVC: View
* Opens a JFileChooser Dialog to get the file
* @return null or File selected
*/
public File GeoJSONPicker(){
    FileNameExtensionFilter filter = new FileNameExtensionFilter("GeoJSON type", "geojson");
    chooser.setFileFilter(filter);
    return chooser.showOpenDialog(frame) == 
        JFileChooser.APPROVE_OPTION chooser.getSelectedFile():null;
}
```



## 4. Presentazione delle classi

Per il GeoJSON si e' dovuto fornire una classe da cui GSON deve prendere le informazioni:

```java
package Utilities.JSON;

import java.util.Vector;

/**
 * Classe GeoJSON per la conversione da JSON source
 * @param <T> contenuto nelle Feature Properties
 */
public class GeoJSON<T> {
    private String type;
    private Vector<GeoJSONFeature<T>> features;


    public String getType() {
        return type;
    }

    public Vector<GeoJSONFeature<T>> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "GeoJSON{" +
                "type='" + type + '\'' +
                ", features=" + features +
                '}';
    }
}
```

Di conseguenza:

```java
package Utilities.JSON;

/**
 * Classe GeoJSON per la conversione da JSON source, rappresenta una Feature
 * @param <T> contenuto nelle Feature Properties
 */
public class GeoJSONFeature<T> {
    private String type;
    private T properties;
    private GeoJSONGeometry geometry;

    public String getType() {
        return type;
    }

    public GeoJSONGeometry getGeometry() {
        return geometry;
    }

    public T getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "GeoJSONFeature{" +
                "type='" + type + '\'' +
                ", properties=" + properties +
                ", geometry=" + geometry +
                '}';
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }
}
```

Per cui:

```java
package Utilities.JSON;

import java.util.Vector;

/**
 * Classe GeoJSON per la conversione da JSON source, rappresenta la geometry
 */
public class GeoJSONGeometry{
    private String type;
    private Vector<Vector<Vector<Double>>> coordinates;

    public Vector<Vector<Vector<Double>>> getCoordinates() {
        return coordinates;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "GeoJSONGeometry{" +
                "type='" + type + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
```

Adesso manca solo la classe T che andrà inserita in properties:

```java
package Program.Model;

import Utilities.UI.Tables.TableObject;
import com.google.gson.annotations.SerializedName;

/**
 * Semplice POJO
 */
public class Green_Area implements TableObject {
    @SerializedName(value = "CIT")
    private String cit;
    @SerializedName(value = "DENOM")
    private String denominazione;
    @SerializedName(value = "AMBITO")
    private String ambito;
    @SerializedName(value = "TIPO")
    private String tipo;
    @SerializedName(value = "LOTTO")
    private String lotto;
    @SerializedName(value = "GESTIONE")
    private String gestione;

    public Green_Area(){}

    public Green_Area(Green_Area green_area){
        cit=green_area.cit;
        denominazione=green_area.denominazione;
        ambito=green_area.ambito;
        tipo=green_area.tipo;
        lotto=green_area.cit;
        gestione=green_area.gestione;
    }

    @Override
    public String toString() {
        return "Green_Area{" +
                "Cit='" + cit + '\'' +
                ", Denominazione='" + denominazione + '\'' +
                ", Ambito='" + ambito + '\'' +
                ", Tipo='" + tipo + '\'' +
                ", Lotto='" + lotto + '\'' +
                ", Gestione='" + gestione + '\'' +
                '}';
    }

    public static Object[] getHeader() {
        return new Object[]{"Cit", "Denominazione", "Ambito", "Tipo", "Lotto", "Gestione"};
    }

    @Override
    public Object[] getContent() {
        return new Object[]{cit, denominazione, ambito, tipo, lotto, gestione};
    }

    public String getAmbito() {
        return ambito;
    }

    public Integer getCit() {
        return Integer.parseInt(cit);
    }

    public String getDenominazione() {
        return denominazione;
    }

    public String getGestione() {
        return gestione;
    }

    public String getLotto() {
        return lotto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public void setCit(String cit) {
        this.cit = cit;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public void setGestione(String gestione) {
        this.gestione = gestione;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
```

Allora avendo una String contenente tutto il GeoJSON e avendo queste classi il procedimento e' il seguente:

```java
public GeoJSON<T> convertFromGeoJSON<T>(String geojson){
    Type type = new com.google.gson.reflect.TypeToken<GeoJSON<T>>{}.getType();
    var gson = new com.google.gson.GSON();
    return (GeoJSON<T>) gson.fromJson(geojson, type);
} 
```

Allora noi possiamo accedere ai contenuti:

```java
GeoJSON<Green_Area> greenAreaGJ = convertFromGeoJSON<Green_Area>(json);
GeoJSONFeature feature1 = greenAreaGJ.getFeatures().get(0);
GeoJSONGeometry geometry = feature1.getGeometry();
Green_Area area = feature1.getProperties();
```

Generalmente questo descrive il funzionamento generale della struttura del programma.



## 5. Gestione della grafica

La grafica e' stata gestita tramite swing e awt, il primo e' stato utilizzato per la costruzione tecnica della finestra (JFrame), la seconda e' stata usata indirettamente e ogni tanto direttamente quando si usavano i parametri Frame, Container, Component e Graphics.

Per non andare troppo nel dettaglio in questa parte, che risulterebbe lunga e contorta, faro' una spiegazione generale delle principali tecniche da me adottate.

La prima di tutte e' la tecnica **MVC** che mi ha permesso di staccare i metodi e i dati dalla grafica, rendendo piu intuitiva la modifica del codice. La scelta di MVC e' un metodo avanzato di programmazione usato specialmente in ambito Web, nulla vieta di portarla anche su una Desktop app. Il **Modello**, che puo' essere un **POJO** o una classe di getter e setter e manipolazione dei dati, e' la classe che gestisce i dati, stream, database e interazioni con l'esterno di tipo informazioni.

Questo puo' essere direttamente connesso alla View per i getter, uso sconsigliato perche' permetterebbe ad un'altra classe l'accesso diretto ai suoi metodi. Invece la classe che si occupa di fare da interlocutore, che contiene le associazioni Listeners Methods, e' il Controller. Al suo interno il controller gestisce sia la View, classe contenente tutti i metodi per la gestione della grafica, sia il Model, e poi da Model passa alla View i dati che vanno ad aggiornare la videata mentre la View e' direttamente connessa ai metodi del controller che gestiranno il modello o che gestiranno altri metodi della View. 

Tuttavia esiste una parte macchinosa della gestione MVC ed e' la preparazione dell'ambiente. Ciò che non deve essere usato non deve in nessun modo essere accessibile, quindi se voglio condividere i listeners devo fare un metodo che aggiunga il Listener, o lo tolga, solo a quell'oggetto. E' possibile farlo anche in altri metodi piu' performanti, magari Enumerando i vari componenti e dividendo per macro compiti (ButtonAction, CheckBoxChange ecc ecc).

```java
class View{
    JButton button1 = new JButton();
    
    public void initializeWindow(){...}
    
    public void showWindow(){...}
    public void hideWindow(){...}
    
    public void button1AddActionListener(ActionListener listener){
        button1.addActionListener(listener);
    }
    
    public void setButton1Text(String text){
        button1.setText(text);
        refreshComponent(button1);
    }
    
    private void refreshComponent(Component component){
        component.repaint();
        component.validate();
    }
}

class Model{
    private String name = "button1";
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}

class Controller{
    private View view = new View();
    private Model model = new Model();
    
    public void run(){
        view.initializeWindow();
        initializeListeners();
        view.showWindow();
    }
    
    private void initializeListeners(){
        view.button1AddActionListener(actionListener->button1Method());
        view.button1AddActionListener(this::button1Method2);
    }
    
    private void button1Method(){
        System.out.println("Action Performed");
        model.setName("Button2");
        
        //Questa cosa andrebbe fatta tramite evento
        view.setButtonText(model.getName());
    }
    
    private void button1Method2(ActionListener listener){
        button1Method();
    }
}
```



### 5.1 TableObject

Per avere una interfaccia univoca nella scrittura degli oggetti su tabella ho creato una @FunctionalInterface chiamata TableObject. Questa interfaccia semplicemente deve essere implementata in un oggetto che possiede dei valori da interpretare in qualche tabella, utile per modificare l'output a piacimento.

```java
interface TableObject{
    Object[] getContents();
}

class POJO implements TableObject{
    private int a, b, c;
    
    @Override
    public Object[] getContents(){
        return new Object[]{a,b,c};
    }
}
```

Ancora non ho trovato un metodo elegante e generale per definire l'header senza averlo su tutte le istanze di TableObject.



### 5.2 HideList

Le HideList sono state utilizzate per avere la tabella che cancellava le colonne senza modificarne la struttura. Utili principalmente in campo grafico, le HideList sono ArrayList che possiedono una contro lista booleana che indica lo stato di visibilità o meno. Mi sono servite per evitare che rendendo invisibile una tabella ne perdessi anche il suo header, senza poterlo ripristinare. Tuttavia sono state anche utili per gli elementi della tabella, che dovevano essere tolti dall'Object[] di getContents (Altrimenti il modello della tabella leggeva sempre gli elementi 0,1,2,..., n senza considerare quanto l'header fosse modificato).

 ```java
package Utilities.UI;

import Utilities.UI.Tables.TableObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * HideList permette di creare una lista i quali valori possono essere semplicemente resi invisibili, 
 * non modificando la struttura
 * l'implementazione di TableObject permette di tornare un Array di oggetti della dimensione degli 
 * elementi visibili
 * @param <E>
 * @author Andrea Marco Farioli
 */
public class HideList<E> extends ArrayList<E> implements TableObject {
    private ArrayList<Boolean> booleans = new ArrayList<>();

    public HideList(){super();}

    public HideList(Collection<? extends E> c) {
        super(c);
        for(int i =0; i<c.size(); i++)
            booleans.add(true);
    }

    @Override
    public boolean add(E t) {
        booleans.add(true);
        return super.add(t);
    }

    @Override
    public E remove(int index) {
        booleans.remove(index);
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        booleans.remove(indexOf(o));
        return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public Object[] getContent() {
        ArrayList<Object> objects =  new ArrayList<>();
        for(int i=0;i<booleans.size();i++){
            if(!booleans.get(i))
                continue;
            objects.add(get(i));
        }
        return objects.toArray();
    }

    public void setVisible(int index) {
        booleans.set(index, true);
    }

    public void setInvisible(int index) {
        booleans.set(index, false);
    }

}
 ```

La gestione della contro lista deve ancora essere ottimizzata.



### 5.3  CheckBoxGroup

I CheckBoxGroup sono classi contentenitori di CheckBox, come i ButtonGroup offrono una gestione e mantengono un rigore formale ai CheckBox. Usato principalmente per gestire il Comun Filler, e per far si che almeno 1 rimanesse selezionato. 

In questa classe ho usato la gestione degli EventListenerList, ho creato un nuovo evento e un nuovo Action (usato per indicare un generale ActionPerformed di uno dei CheckBox interni).

```java
package Utilities.UI.Layout;

import Utilities.UI.Events.MultipleActionEvent;
import Utilities.UI.Events.MultipleActionListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.util.ArrayList;

/**
 * Classe per la gestione delle JCheckBox, funzionamento abbastanza simile al ButtonGroup
 */
public class CheckBoxGroup {
    private ArrayList<JCheckBox> list = new ArrayList<>();
    private boolean atLeastOne, initial;
    private short counterSelected;
    private EventListenerList listenerList = new EventListenerList();


    public CheckBoxGroup(boolean atLeastOne) {
        this.atLeastOne = atLeastOne;
        counterSelected = atLeastOne? (short)1:(short)0;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public void setAtLeastOne(boolean atLeastOne) {
        this.atLeastOne = atLeastOne;
    }

    public boolean add(JCheckBox jCheckBox) {
        if(list.size() == 0 && atLeastOne)
            jCheckBox.setSelected(true);
        else{
            jCheckBox.setSelected(initial);
            if(initial)
                counterSelected++;
        }

        int m = list.size();

        jCheckBox.addActionListener(actionEvent -> {
            if(jCheckBox.isSelected())
                counterSelected++;
            else
                counterSelected--;

            if(atLeastOne && counterSelected == 0){
                jCheckBox.setSelected(true);
                counterSelected = 1;
            }

            fireMultipleAction(jCheckBox, m);
        });

        return list.add(jCheckBox);
    }

    public void addMultipleActionListener(MultipleActionListener multipleActionListener){
        listenerList.add(MultipleActionListener.class, multipleActionListener);
    }

    public void removeMultipleActionListener(MultipleActionListener multipleActionListener){
        if(multipleActionListener != null)
            listenerList.remove(MultipleActionListener.class, multipleActionListener);
    }

    public void fireMultipleAction(Object source,int id){
        MultipleActionListener[] l = listenerList.getListeners(MultipleActionListener.class);
        for(int i = l.length -1; i >= 0; i-=2){
            l[i].multipleActionPerformed(new MultipleActionEvent(source, id,""));
        }
    }

    public void setAll(){
        list.forEach(jCheckBox -> jCheckBox.setSelected(true));
        counterSelected = (short)(list.size() -1);
    }

    public void unsetAll(){
        boolean selection = false;
        for(int i = 0; i < list.size(); i++){
            JCheckBox c = list.get(i);
            if(atLeastOne && c.isSelected() && !selection){
                selection = true;
                break;
            }
            c.setSelected(false);
        }

        if(!selection && list.get(0) != null)
            list.get(0).setSelected(true);
    }
}
```

MultipleActionListener:

```java
package Utilities.UI.Events;

import java.util.EventListener;

@FunctionalInterface
public interface MultipleActionListener extends EventListener {
    void multipleActionPerformed(MultipleActionEvent multipleActionEvent);
}
```

MultipleActionEvent:

```java
public class MultipleActionEvent extends ActionEvent {
    public MultipleActionEvent(Object source, int id, String command) {
        super(source, id, command);
    }
}
```



## 6. Conclusione

Con questo termino la mia Analisi riguardo al programma dell'esercizio 20. Prima di terminare vorrei fare un piccolo commento sui dati da me recuperati.

Torino possiede un gran numero di aree verdi, principalmente di uso comunale, dai dati da me in possesso, risalente alla suddivisione in Lotti del 2012, le aree verdi contate erano **4327**. Le aree verdi comprendono anche le aree dei corsi camminabili, quindi le **Banchine pedonali**. Le aree verdi considerate non sono il totale complessivo delle zone verdi torinesi. Torino e' la citta' piu' verde di Italia, di 130km^2, 21 sono solo di verde.

Concludo lasciando la visione di questo articolo:

[Torino Today](http://www.torinotoday.it/cronaca/perche-torino-citta-piu-verde-italia-.html)



## Fonti

[DatiOpen.it](datiopen.it)
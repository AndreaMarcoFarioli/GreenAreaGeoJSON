package Utilities.UI;

import Utilities.UI.Tables.TableObject;

/**
 * Semplici metodi per la gestione della UI
 * @author Andrea Marco Farioli
 */
public class UIMethods {
    /**
     * Utile metodo per la conversione di un array di oggetti in un data object leggibile dalle tabelle
     * @param tableObject array di oggetti che implementano TableObject
     * @return matrice bidimensionale che rimpiazza tableObject con Object[]
     */
    public static Object[][] tableObjectToData(TableObject[] tableObject){
        Object[][] t = new Object[tableObject.length][];
        for(int i  =0 ; i < tableObject.length; i++){
            t[i] = tableObject[i].getContent();
        }
        return t;
    }
}

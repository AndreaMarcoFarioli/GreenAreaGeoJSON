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



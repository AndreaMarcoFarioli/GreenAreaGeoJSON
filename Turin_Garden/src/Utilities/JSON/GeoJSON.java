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

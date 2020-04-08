package Utilities.JSON;

import java.util.Vector;

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


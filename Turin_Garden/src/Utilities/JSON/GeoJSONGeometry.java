package Utilities.JSON;

import java.util.Vector;

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

package Program.Model;

import Utilities.JSON.GeoJSON;

import Utilities.UI.HideList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class ProgramModel {
    private String filePath, fileContent, errorBuffer;
    private Green_Area[] green_areas;
    private GeoJSON<Green_Area> green_areaGeoJSON;
    private Object bufferObject;
    public static String geoJSONViewerGitHub = "https://raw.githubusercontent.com/AndreaMarcoFarioli/AndreaMarcoFarioli/master/aree_verdi_geo.geojson";

    public static final short CIT = 0, DENOMINAZIONE = 1, AMBITO = 2, TIPO = 3, LOTTO = 4, GESTIONE = 5;

    public short getFileUrl(String u){
        try {
            URL url = new URL(u);
            String s = url.openConnection().getHeaderField(0);
            if(Objects.equals(s, "HTTP/1.1 200 OK"))
                bufferObject = url.openStream();
            else{
                errorBuffer = s;
                return -1;
            }
        } catch (IOException e) {
            errorBuffer = e.toString();
            return -2;
        }
        return 0;
    }
    public void loadFile(InputStream stream){
        try {
            fileContent = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson g = new Gson();

        green_areaGeoJSON = g.fromJson(fileContent, new TypeToken<GeoJSON<Green_Area>>(){}.getType());

        green_areas = new Green_Area[green_areaGeoJSON.getFeatures().size()];

        for(int i = 0; i < green_areas.length; i++){
            green_areas[i] = green_areaGeoJSON.getFeatures().get(i).getProperties();
        }
    }

    public String saveGeoJSON(){
        Gson g = new Gson();
        return g.toJson(green_areaGeoJSON, new TypeToken<GeoJSON<Green_Area>>(){}.getType());
    }

    public void orderBy(short by){
        switch (by){
            case CIT:
                Arrays.sort(green_areas, Comparator.comparing(Green_Area::getCit));
                break;
            case AMBITO:
                Arrays.sort(green_areas, Comparator.comparing(Green_Area::getAmbito));
                break;
            case LOTTO:
                Arrays.sort(green_areas, Comparator.comparing(Green_Area::getLotto));
                break;
            case TIPO:
                Arrays.sort(green_areas, Comparator.comparing(Green_Area::getTipo));
                break;
            case DENOMINAZIONE:
                Arrays.sort(green_areas, Comparator.comparing(Green_Area::getDenominazione));
                break;
            case GESTIONE:
                Arrays.sort(green_areas, Comparator.comparing(Green_Area::getGestione));
                break;
        }
    }

    public void fireFeatureChanged(int index){
        green_areaGeoJSON.getFeatures().get(index).setProperties(green_areas[index]);
    }

    public String getFilePath() {
        return filePath;
    }
    public String getFileContent() {
        return fileContent;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public Object getBufferObject() {
        return bufferObject;
    }
    public void setRow(int index, Green_Area area){
        green_areas[index] = area;
        fireFeatureChanged(index);
    }
    public Green_Area[] getTableObjects(){
        return green_areas;
    }

    public HideList<Object[]> search(String search){
        HideList<Object[]> objects = new HideList<>();
        for(Green_Area area : green_areas){
            if (
                    area.getAmbito().toLowerCase().contains(search.toLowerCase()) ||
                    area.getLotto().toLowerCase().contains(search.toLowerCase())  ||
                    area.getCit().toString().toLowerCase().contains(search.toLowerCase()) ||
                    area.getDenominazione().toLowerCase().contains(search.toLowerCase()) ||
                    area.getGestione().toLowerCase().contains(search.toLowerCase()) ||
                    area.getTipo().toLowerCase().contains(search.toLowerCase())){
                objects.add(area.getContent());
            }
        }
        return objects;
    }
}

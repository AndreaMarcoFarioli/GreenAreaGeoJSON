package Utilities.UI;

import Utilities.UI.Tables.TableObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * HideList permette di creare una lista i quali valori possono essere semplicemente resi invisibili, non modificando la struttura
 * l'implementazione di TableObject permette di tornare un Array di oggetti della dimensione degli elementi visibili
 * @param <E>
 * @author Andrea Marco Farioli
 */
public class HideList<E> extends ArrayList<E> implements TableObject {
    private ArrayList<Boolean> booleans = new ArrayList<>();

    public HideList(){super();}

    public HideList(Collection<? extends E> c){
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

    public void setVisible(int index)
    {
        booleans.set(index, true);
    }

    public void setInvisible(int index){
        booleans.set(index, false);
    }

}

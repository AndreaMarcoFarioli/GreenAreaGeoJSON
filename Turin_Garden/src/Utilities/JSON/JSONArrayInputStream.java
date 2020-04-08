package Utilities.JSON;

import java.io.*;
import java.util.Stack;

/**
 * Streamer per JSON Array
 * Non usato nella nuova versione del programma
 */

public class JSONArrayInputStream extends InputStream{
    private InputStream stream;
    public enum types {JSON_ARRAY, JSON_OBJECT};
    private static final short OPEN_ARRAY = 1, CLOSE_ARRAY = -1, OPEN_OBJECT = 2, CLOSE_OBJECT = -2, OPEN_STRING = 3, CLOSE_STRING = -3;
    private static final char OPN_OBJ = '{', CLS_OBJ = '}', OPN_ARR = '[', CLS_ARR = ']', STR_OPN = '"';
    private Stack<Short> flowBracketsStack = new Stack<>();
    private boolean inString = false;
    private boolean EOF = false;

    public JSONArrayInputStream(InputStream stream) throws IOException{
        this.stream = stream;
        read();
    }

    public JSONArrayInputStream(InputStream stream, char point) throws IOException{
        this.stream = stream;
        while (read() != point){}
    }

    public String readJSON() throws IOException, EOFException{
        String json = "";
        char character;
        int c = read();
        character = (char)c;
        if(c == -1)
            throw new EOFException("END JSON FILE");
        while ((character != ',' && character != ']') || flowBracketsStack.size() != 0){
            json += character;
            flowBracketsManager(character);
            character = (char)read();
        }

        return json;
    }


    private void flowBracketsManager(char t){


        switch (t){
            case OPN_ARR:
                if(!inString)
                    flowBracketsStack.push(OPEN_ARRAY);
                break;
            case OPN_OBJ:
                if(!inString)
                    flowBracketsStack.push(OPEN_OBJECT);
                break;
            case CLS_ARR:
                if((flowBracketsStack.lastElement() + CLOSE_ARRAY == 0) && !inString && flowBracketsStack.size() != 0)
                    flowBracketsStack.pop();
                break;
            case CLS_OBJ:
                if((flowBracketsStack.lastElement() + CLOSE_OBJECT == 0) && !inString);
                    flowBracketsStack.pop();
                break;
            case STR_OPN:
                inString = flowBracketsStack.lastElement() + CLOSE_STRING != 0;
                if(inString)
                    flowBracketsStack.push(OPEN_STRING);
                else
                    flowBracketsStack.pop();
                break;
        }
    }

    @Override
    public int read() throws IOException {
        int tmp;
        do {
            tmp = stream.read();
            if(tmp == -1)
                EOF = true;
        }while (tmp < 33 && tmp > -1 && !inString);
        return tmp;
    }

    @Override
    public void close() throws IOException{
        stream.close();
    }

    public boolean getEOF(){
        return EOF;
    }



}

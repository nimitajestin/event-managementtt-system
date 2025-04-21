package model;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    public abstract boolean isValid();
    
    @Override
    public abstract String toString();
}

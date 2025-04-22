package model;
// store the attributes of classes in moedls
// create class with construcotr and getter and setters, everytime im making an object of this class 
//itll translate to one user in my db
import java.io.Serializable;
// serilalisable converts obj ito byte stream so it can easily be saved as a file and trasnmitted
public abstract class BaseModel implements Serializable {
    public abstract boolean isValid();
    
    @Override
    public abstract String toString();
}

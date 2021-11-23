
import org.springframework.core.NestedRuntimeException;

public class MissingAttributeException extends NestedRuntimeException{
    
    public MissingAttributeException(String message){
        super(message);
    }

}

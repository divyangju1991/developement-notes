import com.google.gson.stream.MalformedJsonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.fineract.infrastructure.core.DataValidation;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataValidation {

   private final Validator validator;

    public void validateData(String response, DataPojo dataObj) throws MalformedJsonException {
        final var validateResponse = validator.validate(dataObj);
        if(!validateResponse.isEmpty()){
            final var msg = new StringBuilder();
            validateResponse.stream().forEach( cv ->
                    msg.append(String.format(" %s %s", cv.getPropertyPath(), cv.getMessage()))
            );
            log.error(msg.toString());
            throw new MalformedJsonException(msg.toString());
        }
        
    }
}

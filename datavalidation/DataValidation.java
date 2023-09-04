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
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.MalformedJsonException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void allFieldsRequiredValidateWithSerializedName(Class<?> data, String responseJson) throws MalformedJsonException {
        if(responseJson == null || responseJson.isEmpty())
            throw new MalformedJsonException("response should not be null");
        final var fields = data.getDeclaredFields();
        final var missedFields = new ArrayList<>();
        Arrays.stream(fields).filter(field -> field.isAnnotationPresent(SerializedName.class) && !responseJson.contains(field.getAnnotation(SerializedName.class).value())).forEach(field -> missedFields.add(field.getAnnotation(SerializedName.class).value()));
        if(!missedFields.isEmpty())
            throw new MalformedJsonException("Missing fields in response "+ missedFields);
    }
}

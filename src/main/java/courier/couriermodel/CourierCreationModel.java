
package courier.couriermodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
public class CourierCreationModel {
    private String login;
    private String password;
    private String firstName;
}

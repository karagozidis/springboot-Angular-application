package crm.cloudApp.backend.dto.users;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import lombok.experimental.Accessors;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class UserWithKeyDTO extends UserDTO {

  private Boolean hasEnabledKey;

}

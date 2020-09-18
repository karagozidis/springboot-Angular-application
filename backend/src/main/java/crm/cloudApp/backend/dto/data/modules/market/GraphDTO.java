package crm.cloudApp.backend.dto.data.modules.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class GraphDTO {

    private String mode;
    private String name;
    private String type;
    private String uid;
    private List<String> x = new ArrayList<>();
    private List<String> y = new ArrayList<>();
    private List<String> hovertext = new ArrayList<>();
    private String hoverinfo = "text";
    private Dictionary<String,Object> marker = new Hashtable();
    private String yaxis;
    private int csvIndex;


    public Boolean checkIfYAxesAreAllNumeric() {
        for (String yVal : y) {
            if (yVal.matches("[a-zA-Z]+")) {
                return false;
            }
        }
        return true;
    }

}

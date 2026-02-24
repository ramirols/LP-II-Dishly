package dto;

import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlatoDTO {

    private Integer id;

    private String nombrePlato;

    private BigDecimal precio;

    private Integer idCategoria;

    private String imagenUrl;       // conserva la imagen actual al editar

    private MultipartFile imagenFile; // nueva imagen a subir
}

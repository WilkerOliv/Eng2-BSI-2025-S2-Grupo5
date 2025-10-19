package projeto.salf.controller.dto;

import java.util.List;

public class AtribuirVoluntariosDTO {
    public List<Item> voluntarios;

    public static class Item {
        public String cpfVoluntario;
        public String cargo; //
    }
}

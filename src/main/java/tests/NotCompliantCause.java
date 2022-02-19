package tests;

import java.util.Arrays;
import java.util.Optional;


public enum NotCompliantCause {
    UNDEFINED(-1),
    FRAUDE_IDENTITAIRE(1),
    CNI_ENFANT_ILLISIBLE(2),
    CNI_PHOTOCOPIE(3),
    DOCUMENT_IDENTITE_NON_ACCEPTE_REFUS_OUVERTURE(4),
    MAUVAISE_QUALITE_SCAN(5),
    LIVRET_DE_FAMILLE_PHOTOCOPIE(6),
    LIVRET_DE_FAMILLE_ILLISIBLE(7);

    private final int id;

    NotCompliantCause(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Optional<NotCompliantCause> fromId(Integer id) {
        if (id == null)
            return Optional.empty();

        return Arrays.stream(values())
                .filter(notCompliantCause -> notCompliantCause.id == id)
                .findAny();
    }
}

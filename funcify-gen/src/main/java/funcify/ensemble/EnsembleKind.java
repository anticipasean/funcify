package funcify.ensemble;

/**
 * @author smccarron
 * @created 2021-05-19
 */
public enum EnsembleKind {

    SOLO(1,
         "Solo"),
    DUET(2,
         "Duet"),
    TRIO(3,
         "Trio"),
    QUARTET(4,
            "Quartet"),
    QUINTET(5,
            "Quintet"),
    SEXTET(6,
           "Sextet"),
    SEPTET(7,
           "Septet"),
    OCTET(8,
          "Octet"),
    NONET(9,
          "Nonet"),
    DECET(10,
          "Decet"),
    ENSEMBLE11(11,
               "Ensemble11"),
    ENSEMBLE12(12,
               "Ensemble12"),
    ENSEMBLE13(13,
               "Ensemble13"),
    ENSEMBLE14(14,
               "Ensemble14"),
    ENSEMBLE15(15,
               "Ensemble15"),
    ENSEMBLE16(16,
               "Ensemble16"),
    ENSEMBLE17(17,
               "Ensemble17"),
    ENSEMBLE18(18,
               "Ensemble18"),
    ENSEMBLE19(19,
               "Ensemble19"),
    ENSEMBLE20(20,
               "Ensemble20"),
    ENSEMBLE21(21,
               "Ensemble21"),
    ENSEMBLE22(22,
               "Ensemble22");


    private final int numberOfValueParameters;
    private final String simpleClassName;

    private EnsembleKind(final int numberOfValueParameters,
                         final String simpleClassName) {
        this.numberOfValueParameters = numberOfValueParameters;
        this.simpleClassName = simpleClassName;
    }

    public int getNumberOfValueParameters() {
        return numberOfValueParameters;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }
}

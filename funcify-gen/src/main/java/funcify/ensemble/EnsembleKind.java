package funcify.ensemble;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author smccarron
 * @created 2021-05-19
 */
public enum EnsembleKind {

    SOLO(1, "Solo"), DUET(2, "Duet"), TRIO(3, "Trio"), QUARTET(4, "Quartet"), QUINTET(5, "Quintet"), SEXTET(6,
                                                                                                            "Sextet"), SEPTET(7,
                                                                                                                              "Septet"), OCTET(
            8,
            "Octet"), NONET(9, "Nonet"), DECET(10, "Decet"), ENSEMBLE11(11, "Ensemble11"), ENSEMBLE12(12,
                                                                                                      "Ensemble12"), ENSEMBLE13(13,
                                                                                                                                "Ensemble13"), ENSEMBLE14(
            14,
            "Ensemble14"), ENSEMBLE15(15, "Ensemble15"), ENSEMBLE16(16, "Ensemble16"), ENSEMBLE17(17,
                                                                                                  "Ensemble17"), ENSEMBLE18(18,
                                                                                                                            "Ensemble18"), ENSEMBLE19(
            19,
            "Ensemble19"), ENSEMBLE20(20, "Ensemble20"), ENSEMBLE21(21, "Ensemble21"), ENSEMBLE22(22, "Ensemble22");


    private final int numberOfValueParameters;
    private final String simpleClassName;

    EnsembleKind(final int numberOfValueParameters,
                 final String simpleClassName) {
        this.numberOfValueParameters = numberOfValueParameters;
        this.simpleClassName = simpleClassName;
    }

    public static Map<Integer, EnsembleKind> getEnsembleKindByNumberOfValueParameters() {
        return MapHolder.INSTANCE.getEnsembleKindByNumberOfValueParameters();
    }

    public int getNumberOfValueParameters() {
        return numberOfValueParameters;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    private static enum MapHolder {
        INSTANCE(Arrays.stream(EnsembleKind.values()).reduce(new HashMap<Integer, EnsembleKind>(), (m, ek) -> {
            m.put(ek.getNumberOfValueParameters(), ek);
            return m;
        }, (m1, m2) -> {
            m1.putAll(m2);
            return m1;
        }));

        private final Map<Integer, EnsembleKind> ensembleKindByNumberOfValueParameters;

        MapHolder(final Map<Integer, EnsembleKind> ensembleKindByNumberOfValueParameters) {
            this.ensembleKindByNumberOfValueParameters = Collections.unmodifiableMap(ensembleKindByNumberOfValueParameters);
        }

        public Map<Integer, EnsembleKind> getEnsembleKindByNumberOfValueParameters() {
            return ensembleKindByNumberOfValueParameters;
        }
    }
}

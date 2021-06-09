package funcify.factory.generation;

import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.generation.TypeGenerationTemplate;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-28
 */
public interface EnsembleTypeGenerationFactory extends TypeGenerationTemplate<EnsembleTypeGenerationSessionWT>,
                                                       EnsembleMethodGenerationFactory {

    static EnsembleTypeGenerationFactory of() {
        return DefaultEnsembleTypeGenerationFactory.of();
    }

    @AllArgsConstructor(staticName = "of")
    static class DefaultEnsembleTypeGenerationFactory implements EnsembleTypeGenerationFactory {

    }

}

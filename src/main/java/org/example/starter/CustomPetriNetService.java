package org.example.starter;

import com.netgrif.application.engine.auth.domain.LoggedUser;
import com.netgrif.application.engine.configuration.ElasticCaseSearchConfiguration;
import com.netgrif.application.engine.importer.service.throwable.MissingIconKeyException;
import com.netgrif.application.engine.petrinet.domain.PetriNet;
import com.netgrif.application.engine.petrinet.domain.dataset.Field;
import com.netgrif.application.engine.petrinet.domain.throwable.MissingPetriNetMetaDataException;
import com.netgrif.application.engine.petrinet.service.PetriNetService;
import com.netgrif.application.engine.workflow.domain.eventoutcomes.petrinetoutcomes.ImportPetriNetEventOutcome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@Primary
public class CustomPetriNetService extends PetriNetService {

    @Autowired
    private ElasticCaseSearchConfiguration elasticCaseSearchConfiguration;

    @Override
    protected ImportPetriNetEventOutcome importPetriNet(InputStream xmlFile, LoggedUser author, String uriNodeId, Map<String, String> params, Function<PetriNet, PetriNet> existenceCheck) throws IOException, MissingPetriNetMetaDataException, MissingIconKeyException {
        ImportPetriNetEventOutcome outcome = super.importPetriNet(xmlFile, author, uriNodeId, params, existenceCheck);
        PetriNet net = outcome.getNet();
        if (net == null) {
            return outcome;
        }
        net.getDataSet().values().stream().filter(Field::isImmediate).forEach(field -> {
            // TODO: should be fixed by NAE-2469 or manually create fulltext map in runner
            String fieldKey = "dataSet." + field.getImportId() + ".fulltextValue";
            elasticCaseSearchConfiguration.getFullTextFieldMap().putIfAbsent(fieldKey, 1f);
        });
        return outcome;
    }
}

package datawave.microservice.querymetric.config;

import datawave.marking.MarkingFunctions;
import datawave.microservice.querymetric.QueryMetricFactory;
import datawave.microservice.querymetric.factory.QueryMetricQueryLogicFactory;
import datawave.microservice.querymetric.handler.ContentQueryMetricsIngestHelper;
import datawave.microservice.querymetric.handler.LocalShardTableQueryMetricHandler;
import datawave.microservice.querymetric.handler.QueryMetricCombiner;
import datawave.microservice.security.util.DnUtils;
import datawave.query.language.parser.jexl.LuceneToJexlQueryParser;
import datawave.services.common.connection.AccumuloConnectionPool;
import datawave.webservice.query.result.event.EventBase;
import datawave.webservice.query.result.event.FieldBase;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class AlternateShardTableQueryMetricHandler extends LocalShardTableQueryMetricHandler<AlternateQueryMetric> {
    
    public AlternateShardTableQueryMetricHandler(QueryMetricHandlerProperties queryMetricHandlerProperties,
                    @Qualifier("warehouse") AccumuloConnectionPool connectionPool, QueryMetricQueryLogicFactory logicFactory, QueryMetricFactory metricFactory,
                    MarkingFunctions markingFunctions, QueryMetricCombiner queryMetricCombiner, LuceneToJexlQueryParser luceneToJexlQueryParser,
                    DnUtils dnUtils) {
        super(queryMetricHandlerProperties, connectionPool, logicFactory, metricFactory, markingFunctions, queryMetricCombiner, luceneToJexlQueryParser,
                        dnUtils);
    }
    
    @Override
    public ContentQueryMetricsIngestHelper getQueryMetricsIngestHelper(boolean deleteMode) {
        return new AlternateContentQueryMetricsIngestHelper(deleteMode);
    }
    
    @Override
    public AlternateQueryMetric toMetric(EventBase event) {
        AlternateQueryMetric queryMetric = super.toMetric(event);
        List<FieldBase> fields = event.getFields();
        fields.forEach(f -> {
            if (f.getName().equals("EXTRA_FIELD")) {
                queryMetric.setExtraField(f.getValueString());
            }
        });
        return queryMetric;
    }
}

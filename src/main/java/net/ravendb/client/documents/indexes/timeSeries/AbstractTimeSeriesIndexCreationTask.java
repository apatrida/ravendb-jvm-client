package net.ravendb.client.documents.indexes.timeSeries;

import net.ravendb.client.documents.conventions.DocumentConventions;

public class AbstractTimeSeriesIndexCreationTask extends AbstractGenericTimeSeriesIndexCreationTask {

    protected String map;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    @Override
    public TimeSeriesIndexDefinition createIndexDefinition() {
        if (conventions == null) {
            conventions = new DocumentConventions();
        }

        TimeSeriesIndexDefinitionBuilder indexDefinitionBuilder = new TimeSeriesIndexDefinitionBuilder(getIndexName());
        indexDefinitionBuilder.setIndexesStrings(indexesStrings);
        indexDefinitionBuilder.setAnalyzersStrings(analyzersStrings);
        indexDefinitionBuilder.setMap(map);
        indexDefinitionBuilder.setReduce(reduce);
        indexDefinitionBuilder.setStoresStrings(storesStrings);
        indexDefinitionBuilder.setSuggestionsOptions(indexSuggestions);
        indexDefinitionBuilder.setTermVectorsStrings(termVectorsStrings);
        indexDefinitionBuilder.setSpatialIndexesStrings(spatialOptionsStrings);
        indexDefinitionBuilder.setOutputReduceToCollection(outputReduceToCollection);
        indexDefinitionBuilder.setPatternForOutputReduceToCollectionReferences(patternForOutputReduceToCollectionReferences);
        indexDefinitionBuilder.setPatternReferencesCollectionName(patternReferencesCollectionName);
        indexDefinitionBuilder.setAdditionalSources(getAdditionalSources());
        indexDefinitionBuilder.setConfiguration(getConfiguration());

        return indexDefinitionBuilder.toIndexDefinition(conventions);
    }
}
/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.utilities;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.stream.Collectors;

public class HateoasUtils {
    /**
     * This method extracts entities from a collection model
     * @param collectionModel the collection model
     * @param <T>             the type of the entity
     * @return the list of entities
     */
    public static <T> List<T> extractEntities(HttpEntity<CollectionModel<EntityModel<T>>> collectionModel) {

        return collectionModel.getBody().getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());
    }
}

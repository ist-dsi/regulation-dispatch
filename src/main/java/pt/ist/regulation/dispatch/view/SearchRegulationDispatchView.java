package pt.ist.regulation.dispatch.view;

import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import module.regulation.dispatch.domain.RegulationDispatchSystem;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SearchRegulationDispatchView {

    public static JsonArray search(final String search) {
        final RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
        return system.getActiveProcessesSet().stream().filter(p -> matchesSearch(p, search))
                .map(SearchRegulationDispatchView::toJsonObject).collect(toJsonArray());
    }

    private static JsonObject toJsonObject(final RegulationDispatchWorkflowMetaProcess p) {
        final JsonObject object = new JsonObject();
        object.addProperty("id", p.getExternalId());
        object.addProperty("reference", p.getReference());
        object.addProperty("emissionDate", p.getEmissionDate().toString("yyyy-MM-dd"));
        object.addProperty("instanceDescription", p.getInstanceDescription());
        final User user = p.getRequestorUser();
        final UserProfile profile = user.getProfile();
        object.addProperty("requestorUser", user.getUsername());
        object.addProperty("requestorUserName", profile == null ? user.getUsername() : profile.getDisplayName());
        object.addProperty("requestorUserAvatarUrl", profile == null ? null : profile.getAvatarUrl());
        object.addProperty("regulationReference", p.getRegulationReference());
        object.addProperty("hasMainDocument", p.getMainDocument() != null);
        return object;
    }

    public static <T extends JsonElement> Collector<T, JsonArray, JsonArray> toJsonArray() {
        return Collector.of(JsonArray::new, (array, element) -> array.add(element), (one, other) -> {
            one.addAll(other);
            return one;
        }, Characteristics.IDENTITY_FINISH);
    }

    private static boolean matchesSearch(final RegulationDispatchWorkflowMetaProcess dispatch, final String search) {
        final String sSearch = search.toLowerCase();
        return matches(dispatch.getReference(), sSearch) || matches(dispatch.getEmissionDate().toString("yyyy-MM-dd"), sSearch)
                || matches(dispatch.getInstanceDescription(), sSearch)
                || matches(dispatch.getRequestorUser().getPerson().getName(), sSearch)
                || matches(dispatch.getRegulationReference(), sSearch);
    }

    private static boolean matches(final String string, final String search) {
        return string != null && string.toLowerCase().indexOf(search) > -1;
    }

}

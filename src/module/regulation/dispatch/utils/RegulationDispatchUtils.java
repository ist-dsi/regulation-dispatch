package module.regulation.dispatch.utils;

import myorg.domain.RoleType;
import myorg.domain.User;

public class RegulationDispatchUtils {

    public static boolean isMyOrgManager(final User user) {
	return user.hasRoleType(RoleType.MANAGER);
    }
}

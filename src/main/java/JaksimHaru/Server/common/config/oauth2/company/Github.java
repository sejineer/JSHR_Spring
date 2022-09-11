package JaksimHaru.Server.common.config.oauth2.company;

import JaksimHaru.Server.common.config.oauth2.OAuth2UserInfo;
import JaksimHaru.Server.member.domain.Provider;

import java.util.Map;

public class Github extends OAuth2UserInfo {

    public Github(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }

    @Override
    public String getProvider() {
        return Provider.github.toString();
    }
}

package za.ac.mxolisi.prayer.PersonalManagementApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import za.ac.mxolisi.prayer.PersonalManagementApplication.model.User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        userService.findByEmail(email).orElseGet(() -> {
            // register new user if not exists
            User user = new User();
            user.setEmail(email);
            user.setUsername(oauth2User.getAttribute("name"));
            user.setPassword(""); // no password for OAuth users
            return userService.saveUser(user);
        });

        return oauth2User;
    }
}


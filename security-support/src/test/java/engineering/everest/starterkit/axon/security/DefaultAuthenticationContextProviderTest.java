package engineering.everest.starterkit.axon.security;

import engineering.everest.starterkit.axon.common.domain.User;
import engineering.everest.starterkit.axon.security.userdetails.AdminUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAuthenticationContextProviderTest {

    private static final String USER_NAME = "foo@example.com";
    private static final User USER = new User(randomUUID(), randomUUID(), USER_NAME, "");
    private static final AdminUserDetails ADMIN_USER_DETAILS = new AdminUserDetails(USER);

    @Mock
    private SecurityContext securityContext;
    @Mock
    private OAuth2Authentication oAuth2Authentication;

    private DefaultAuthenticationContextProvider defaultAuthenticationContextProvider;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        defaultAuthenticationContextProvider = new DefaultAuthenticationContextProvider();
    }

    @Test
    void willGetUSer() {
        when(oAuth2Authentication.isAuthenticated()).thenReturn(true);
        when(oAuth2Authentication.getPrincipal()).thenReturn(ADMIN_USER_DETAILS);
        when(securityContext.getAuthentication()).thenReturn(oAuth2Authentication);

        assertEquals(USER, defaultAuthenticationContextProvider.getUser());
    }

    @Test
    void willThrowAuthenticationFailureException_WhenAuthenticationIsNotOfTypeOAuth2Authentication() {
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        assertThrows(AuthenticationFailureException.class, () -> defaultAuthenticationContextProvider.getUser());
    }

    @Test
    void willThrowAuthenticationFailureException_WhenIsNotAuthenticated() {
        when(oAuth2Authentication.isAuthenticated()).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(oAuth2Authentication);
        assertThrows(AuthenticationFailureException.class, () -> defaultAuthenticationContextProvider.getUser());
    }

    @Test
    void willThrowAuthenticationFailureException_WhenPrincipleIsNotOfTypeAdminUserDetails() {
        when(oAuth2Authentication.isAuthenticated()).thenReturn(true);
        when(oAuth2Authentication.getPrincipal()).thenReturn(new Object());
        when(securityContext.getAuthentication()).thenReturn(oAuth2Authentication);
        assertThrows(AuthenticationFailureException.class, () -> defaultAuthenticationContextProvider.getUser());
    }
}

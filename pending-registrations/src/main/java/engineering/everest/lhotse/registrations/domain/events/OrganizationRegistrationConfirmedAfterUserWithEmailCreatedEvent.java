package engineering.everest.lhotse.registrations.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.serialization.Revision;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Revision("0")
public class OrganizationRegistrationConfirmedAfterUserWithEmailCreatedEvent {
    private UUID registrationConfirmationCode;
    private UUID organizationId;
    private UUID registeringUserId;
    private String registeringUserEmail;
}

package io.axoniq.labs.chat.query.rooms.messages;

import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChatMessageProjection {

    private final ChatMessageRepository repository;
    private final QueryUpdateEmitter updateEmitter;

    public ChatMessageProjection(ChatMessageRepository repository, QueryUpdateEmitter updateEmitter) {
        this.repository = repository;
        this.updateEmitter = updateEmitter;
    }

    // TODO: Create some event handlers that update this model when necessary.
    @EventHandler
    public void handle(MessagePostedEvent event, @Timestamp Instant timestamp) {
        repository.save(new ChatMessage(
                event.getParticipant(),
                event.getRoomId(),
                event.getMessage(),
                timestamp.toEpochMilli()
        ));
    }

    // TODO: Create the query handler to read data from this model.

    // TODO: Emit updates when new message arrive to notify subscription query by modifying the event handler.
}

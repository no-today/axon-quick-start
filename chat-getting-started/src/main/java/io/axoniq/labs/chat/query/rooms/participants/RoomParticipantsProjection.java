package io.axoniq.labs.chat.query.rooms.participants;

import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomParticipantsQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomParticipantsProjection {

    private final RoomParticipantsRepository repository;

    public RoomParticipantsProjection(RoomParticipantsRepository repository) {
        this.repository = repository;
    }

    // TODO: Create some event handlers that update this model when necessary.
    @EventHandler
    public void event(ParticipantJoinedRoomEvent event) {
        repository.save(new RoomParticipant(event.getRoomId(), event.getParticipant()));
    }

    @EventHandler
    public void event(ParticipantLeftRoomEvent event) {
        repository.deleteByParticipantAndRoomId(event.getParticipant(), event.getRoomId());
    }

    // TODO: Create the query handler to read data from this model.
    @QueryHandler
    public List<String> query(RoomParticipantsQuery query) {
        return repository.findRoomParticipantsByRoomId(query.getRoomId()).stream()
                .map(RoomParticipant::getParticipant)
                .collect(Collectors.toList());
    }
}

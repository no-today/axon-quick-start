package io.axoniq.labs.chat.query.rooms.summary;

import io.axoniq.labs.chat.coreapi.AllRoomsQuery;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomSummaryProjection {

    private final RoomSummaryRepository roomSummaryRepository;

    public RoomSummaryProjection(RoomSummaryRepository roomSummaryRepository) {
        this.roomSummaryRepository = roomSummaryRepository;
    }

    // TODO: Create some event handlers that update this model when necessary.
    @EventHandler
    public void event(RoomCreatedEvent event) {
        roomSummaryRepository.save(new RoomSummary(event.getRoomId(), event.getName()));
    }

    @EventHandler
    public void event(ParticipantJoinedRoomEvent event) {
        roomSummaryRepository.findById(event.getRoomId())
                .ifPresent(RoomSummary::addParticipant);
    }

    @EventHandler
    public void event(ParticipantLeftRoomEvent event) {
        roomSummaryRepository.findById(event.getRoomId())
                .ifPresent(RoomSummary::removeParticipant);
    }

    // TODO: Create the query handler to read data from this model.
    @QueryHandler
    public List<RoomSummary> query(AllRoomsQuery query) {
        return roomSummaryRepository.findAll();
    }
}

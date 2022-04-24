package io.axoniq.labs.chat.commandmodel;

import io.axoniq.labs.chat.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

@Aggregate
public class ChatRoom {

    private static final Logger log = LoggerFactory.getLogger(ChatRoom.class);

    @AggregateIdentifier
    private String roomId;
    private Set<String> members;

    // TODO: This class has just been created to make the test compile. It's missing, well, everything...

    public ChatRoom() {
    }

    @CommandHandler
    public ChatRoom(CreateRoomCommand command) {
        AggregateLifecycle.apply(new RoomCreatedEvent(command.getRoomId(), command.getName()));
    }

    @CommandHandler
    public void handle(JoinRoomCommand command) {
        if (members.contains(command.getParticipant())) {
            return;
        }

        AggregateLifecycle.apply(new ParticipantJoinedRoomEvent(command.getRoomId(), command.getParticipant()));
    }

    @CommandHandler
    public void handle(LeaveRoomCommand command) {
        if (!members.contains(command.getParticipant())) {
            return;
        }

        AggregateLifecycle.apply(new ParticipantLeftRoomEvent(command.getRoomId(), command.getParticipant()));
    }

    @CommandHandler
    public void handle(PostMessageCommand command) {
        if (!members.contains(command.getParticipant())) {
            throw new IllegalStateException("Participant " + command.getParticipant() + " is not a member of room " + roomId);
        }

        AggregateLifecycle.apply(new MessagePostedEvent(command.getRoomId(), command.getParticipant(), command.getMessage()));
    }

    @EventSourcingHandler
    public void on(RoomCreatedEvent event) {
        log.info("[On-Event] RoomCreatedEvent: {}", event);

        this.roomId = event.getRoomId();
        this.members = new LinkedHashSet<>();
    }

    @EventSourcingHandler
    public void on(ParticipantJoinedRoomEvent event) {
        log.info("[On-Event] ParticipantJoinedRoomEvent: {}", event);

        members.add(event.getParticipant());
    }

    @EventSourcingHandler
    public void on(ParticipantLeftRoomEvent event) {
        log.info("[On-Event] ParticipantLeftRoomEvent: {}", event);

        members.remove(event.getParticipant());
    }
}

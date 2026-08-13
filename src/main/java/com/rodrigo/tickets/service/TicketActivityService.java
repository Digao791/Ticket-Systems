package com.rodrigo.tickets.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.tickets.dto.CreateTicketActivityRequest;
import com.rodrigo.tickets.dto.TicketActivityView;
import com.rodrigo.tickets.entity.Ticket;
import com.rodrigo.tickets.entity.TicketActivity;
import com.rodrigo.tickets.entity.TicketStatus;
import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.exception.NoTicketActivityException;
import com.rodrigo.tickets.exception.TicketNotFoundException;
import com.rodrigo.tickets.repository.TicketActivityRepository;
import com.rodrigo.tickets.repository.TicketRepository;
import com.rodrigo.tickets.repository.UserRepository;


@Service
public class TicketActivityService {

    private final TicketActivityRepository activityRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketActivityService(TicketActivityRepository activityRepository, TicketRepository ticketRepository, UserService userService, UserRepository userRepository){
        this.activityRepository = activityRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketActivityView> findByTicketId(Long ticketId){
        return activityRepository
        .findByTicketIdOrderByCreatedAtAsc(ticketId)
        .stream()
        .map(this::toView)
        .toList();
    }

    @Transactional
    public void create(Long ticketId, CreateTicketActivityRequest request, String author){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));

        TicketStatus oldStatus = ticket.getStatus();
        TicketStatus newStatus = request.getStatus();

        User oldOwner = ticket.getOwner();
        User newOwner = userRepository
        .findById(request.getOwnerId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid Owner"));

        boolean statusChanged =
            oldStatus != newStatus;

        boolean ownerChanged =
            !Objects.equals(
                oldOwner.getId(),
                newOwner.getId()
            );

        boolean hasComment =
            request.getComment() != null
            && !request.getComment().isBlank();


        if (!hasComment
                && !statusChanged
                && !ownerChanged) {

            throw new NoTicketActivityException();
        }

        ticket.setStatus(newStatus);
        ticket.setOwner(newOwner);

        TicketActivity activity = new TicketActivity(
            ticket,
            author, 
            normalizeComment(request.getComment()), 
            oldStatus, 
            newStatus, 
            oldOwner, 
            newOwner, 
            LocalDateTime.now()
        );

        activityRepository.save(activity);
    }

    private String normalizeComment(String comment){
        if(comment == null | comment.isBlank()){
            return null;
        }

        return comment.trim();
    }

    private TicketActivityView toView(TicketActivity activity){
        boolean statusChanged = activity.getOldStatus() != activity.getNewStatus();
        boolean ownerChanged = !Objects.equals(activity.getOldOwner(), activity.getNewOwner());

        return new TicketActivityView(
            activity.getAuthor(),
            activity.getComment(),
            activity.getOldStatus().getLabel(),
            activity.getNewStatus().getLabel(),
            activity.getOldOwner() == null ? null : activity.getOldOwner().getName(),
            activity.getNewOwner() == null ? null : activity.getNewOwner().getName(),

            statusChanged,
            ownerChanged,

            activity.getCreatedAt()
        );
    }
}

package com.rodrigo.tickets.entity;

public enum TicketStatus {
    OPEN("Aberto", "open"),
    IN_PROGRESS("Em Progresso", "in-progress"),
    RESOLVED("Resolvido", "resolved"),
    CLOSED("Fechado", "closed");

    private final String label;
    private final String cssClass;

    TicketStatus(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel(){
        return label;
    }

    public String getCssClass(){
        return cssClass;
    }
}

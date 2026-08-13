package com.rodrigo.tickets.entity;

public enum TicketCriticality {
    LOW("Baixa", "low"),
    MEDIUM("Média", "medium"),
    HIGH("Alta", "high"),
    CRITICAL("Crítica", "critical");

    private final String label;
    private final String cssClass;

    TicketCriticality(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }
}

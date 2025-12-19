package com.bank.transfers.initiation.domain;

public enum RailType {
    INTERNAL,  // Same bank, book transfer
    INTERAC,   // Interac e-Transfer
    ACH,       // Automated Clearing House
    WIRE       // Wire transfer
}


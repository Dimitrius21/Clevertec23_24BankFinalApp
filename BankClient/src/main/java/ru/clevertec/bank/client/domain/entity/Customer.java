package ru.clevertec.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Customer {
    private UUID customer_id; //": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
    private String customer_type; //" : "LEGAL/PHYSIC",
    private int unp; //": "Только для LEGAL",
    private LocalDate register_date; //": "dd.MM.yyyy",
    private String email; //'": "example@email.com",
    private String phoneCode; //": "37529",
    private String phoneNumber; //": "1112233",
    private String customer_fullname; //": "Иванов Иван Иванович"
}

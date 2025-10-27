@startuml
hide methods
skinparam classAttributeIconSize 0


enum StatusRezerwacji {
+ POTWIERDZONA
+ ODWOŁANA
}

enum StatusLotu {
+ ZAPLANOWANY
+ OPÓŹNIONY
+ W_BOARDINGU
+ ODLECIAŁ
}

enum StatusBagażu {
+ ZAREJESTROWANY
+ W_KONTROLI
+ ZAŁADOWANY
+ WYŁADOWANY
+ ZAGUBIONY
}

enum WynikSkanu {
+ ZGODNY
+ ODRZUCONY
+ WYMAGA_MANUALNEGO_SPRAWDZENIA
}

enum StatusBoardingu {
+ OCZEKUJĄCY
+ W_TRAKCIE
+ ZAMKNIĘTY
}

class Pasażer {
- id: Integer <<PK>>
--
+ imie: String
+ nazwisko: String
+ dataUrodzenia: Date
+ numerDokumentu: String
+ email: String
+ nrTelefonu: String
}

class Lot {
- id: Integer <<PK>>
--
+ nrLotu: String
+ czasOdlotuPlanowany: Date
+ czasOdlotuAktualny: Date
+ czasPrzylotuPlanowany: Date
+ czasPrzylotuAktualny: Date
+ statusLotu: StatusLotu
}

class Rezerwacja {
- id: Integer <<PK>>
+ numerRezerwacji: String
+ dataZakupu: Date
+ cenaCałkowita: double
+ statusRezerwacji: StatusRezerwacji
}


class KartaPokładowa {
- id: Integer <<PK>>
--
+ kodQR: String
+ numerMiejsca: String
+ bramka_nr: String <<FK>>
+ czasBoardingu: Date
}


class Bagaż {
- id: Integer <<PK>>
--
+ etykieta: String
+ waga: double
+ typBagażu: String
+ statusBagażu: StatusBagażu
}

class Boarding {
- id: Integer <<PK>>
--
+ numerBramki: String
+ poczatekBoardingu: Date
+ koniecBoardingu: Date
+ status: StatusBoardingu
+ liczbaOdprawionych: Integer
}



class SkanBezpieczeństwa {
- id: Integer <<PK>>
--
+ wynikSkanu: WynikSkanu
+ uwagi: String
}


class Lotnisko {
- id: Integer <<PK>>
--
+ nazwa: String
+ kod: String
+ miasto: String
+ kraj: String
}




Pasażer "1" - "0..*" Rezerwacja : jest właścicielem

Rezerwacja "0..*" -- "1..*" Pasażer : obejmuje

(Pasażer, Rezerwacja) .. KartaPokładowa

Lot "1" - "1" Lotnisko : odlatuje z

Lot "1" - "1" Lotnisko : przylatuje do

Lot "1" -- "0..*" Rezerwacja : dotyczy

Bagaż "1" -- "0..1" SkanBezpieczeństwa : jestWynikiemSkanu

Lot "1" -- "1" Boarding : ma

KartaPokładowa "1" -- "0..*" Bagaż : posiada

Boarding "1" -- "0..*" KartaPokładowa : dotyczy

@enduml
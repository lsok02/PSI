@startuml
hide methods
skinparam classAttributeIconSize 0


enum RodzajDokumentu {
+ PASZPORT
+ DOWOÓD_OSOBISTY
  }

enum StatusRezerwacji {
+ POTWIERDZONA
+ ODWOŁANA
  }

enum StatusLotu {
+ ZAPLANOWANY
+ OPÓŹNIONY
+ ODWOŁANY
+ W_BOARDINGU
+ W_POWIETRZU
+ WYLĄDOWAŁ
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

class LiniaLotnicza {
- id: Integer <<PK>>
  --
+ nazwa: String
  }

class Pasażer {
- id: Integer <<PK>>
  --
+ imie: String
+ nazwisko: String
+ dataUrodzenia: Date
+ rodzajDokumentu: String
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


class Odprawa {
- id: Integer <<PK>>
  --
+ czasOdprawy: Date
  }


class KartaPokładowa {
- id: Integer <<PK>>
  --
+ numerKarty: String
+ numerMiejsca: String
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

class Pracownik {
- id: Integer <<PK>>
+ numerIdentyfikacyjny: String
+ imie: String
+ nazwisko: String
+ stanowisko: String
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

Rezerwacja "1" -- "1..*" Pasażer : obejmuje

Rezerwacja "1" -- "1" Odprawa : posiada

Odprawa "1" -- "0..*" KartaPokładowa : generuje

KartaPokładowa "1" -- "1" Pasażer : dotyczy

Pracownik "0..1" -- "0..*" Odprawa : wykonuje


Lot "1" - "1" Lotnisko : odlatuje z
Lot "1" - "1" Lotnisko : przylatuje do
Lot "1" -- "0..*" Rezerwacja : dotyczy
Bagaż "1" -- "0..1" SkanBezpieczeństwa : przechodzi
Lot "1" -- "1" Boarding : posiada
KartaPokładowa "1" -- "0..*" Bagaż : ma
Boarding "1" -- "0..*" KartaPokładowa : dotyczy

Pracownik "1" -- "0..*" SkanBezpieczeństwa : nadzoruje
Pracownik "1" -- "0..*" Boarding : nadzoruje
Lot "1" -- "1" LiniaLotnicza : obsługuje
@enduml
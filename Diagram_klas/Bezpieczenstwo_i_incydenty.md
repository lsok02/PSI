@startuml
hide methods
skinparam classAttributeIconSize 0

enum TypIncydentu {
MEDYCZNY
TECHNICZNY
ZAGROŻENIE_BEZPIECZEŃSTWA
POŻAROWY
INNY
}

enum PriorytetIncydentu {
KRYTYCZNY
WYSOKI
NORMALNY
NISKI
}

enum StatusIncydentu {
NOWY
PRZYDZIELONY
W_TOKU
ROZWIĄZANY
ZAMKNIĘTY
}

enum ZrodloZgloszenia {
MANUALNE
SYSTEM_PPOZ
KONTROLA_DOSTEPU
CCTV
}

enum StatusZespolu {
DOSTĘPNY
ZAJĘTY
NIEDOSTĘPNY
}

enum StatusObecnosci {
ZAPLANOWANA
OBECNY
NIEOBECNY
ZWOLNIENIE
}

class Incydent {
- id: Integer <<PK>>
--
+ numerZgloszenia: String
+ czasZgloszenia: DateTime
+ czasZamkniecia: DateTime
+ opis: String
+ typ: TypIncydentu
+ priorytet: PriorytetIncydentu
+ status: StatusIncydentu
+ zrodlo: ZrodloZgloszenia
  }

class Pracownik {
- id: Integer <<PK>>
--
+ imie: String
+ nazwisko: String
+ numerSluzbowy: String
  }

class Dyspozytor {
}

class CzłonekZespoluInterwencyjnego {
+ numerRadiotelefonu: String
}

class ManagerBezpieczenstwa {
}

class ZespolInterwencyjny {
- id: Integer <<PK>>
--
+ nazwaZespolu: String
+ specjalizacja: String
+ status: StatusZespolu
  }

class WpisDziennika {
- id: Integer <<PK>>
--
+ czasAkcji: DateTime
+ opisDzialania: String
  }

class Zalacznik {
- id: Integer <<PK>>
--
+ nazwaPliku: String
+ typPliku: String
+ url: String
  }

class StandardowaProceduraOperacyjna {
- id: Integer <<PK>>
--
+ nazwaProcedury: String
+ opis: String
  }

class KrokProcedury {
- id: Integer <<PK>>
--
+ kolejnosc: Integer
+ opisKroku: String
  }

class Lokalizacja {
- id: Integer <<PK>>
--
+ nazwa: String
+ typ: String
+ wspolrzedne: String
  }

class ZasobLotniskowy {
- id: Integer <<PK>>
--
+ nazwaZasobu: String
+ typZasobu: String
  }

class Raport {
- id: Integer <<PK>>
--
+ dataWygenerowania: DateTime
+ typRaportu: String
+ zakresDat: String
  }

class Certyfikat {
- id: Integer <<PK>>
--
+ nazwaCertyfikatu: String
+ dataWaznosci: DateTime
}

class Zmiana {
- id: Integer <<PK>>
--
+ nazwa: String
+ dataRozpoczecia: DateTime
+ dataZakonczenia: DateTime
}

class PrzypisanieDoZmiany {
- id: Integer <<PK>>
--
+ status: StatusObecnosci
+ czasWejsciaRzeczywisty: DateTime
+ czasWyjsciaRzeczywisty: DateTime
+ uwagi: String
}

Pracownik <|-- Dyspozytor
Pracownik <|-- CzłonekZespoluInterwencyjnego
Pracownik <|-- ManagerBezpieczenstwa


Incydent "1" *-- "1" Lokalizacja : ma miejsce w
Incydent "1" -- "1" Dyspozytor : zarejestrowany przez
Incydent "1" -- "0..1" ZespolInterwencyjny : obsługiwany przez
Incydent "1" o--  "0..*{ordered}" WpisDziennika : ma historię
Incydent "1" -- "0..*" ZasobLotniskowy : wpływa na
Incydent "1" -- "0..1" ManagerBezpieczenstwa : eskalowany do
Incydent "1" -- "1" StandardowaProceduraOperacyjna : podlega pod

WpisDziennika "1" -- "1" Pracownik : wykonany przez
WpisDziennika "1" o-- "0..*" Zalacznik : zawiera

ZespolInterwencyjny "1" *-- "1..*" CzłonekZespoluInterwencyjnego : składa się z

StandardowaProceduraOperacyjna "1" *-- "1..*" KrokProcedury : zawiera
StandardowaProceduraOperacyjna "1" -- "1..*" TypIncydentu : dotyczy

Raport "1" -- "1" ManagerBezpieczenstwa : wygenerowany przez
Raport "1" -- "1..*" Incydent : analizuje

CzłonekZespoluInterwencyjnego "1" -- "0..*" Certyfikat : posiada

Pracownik "1" -- "0..*" PrzypisanieDoZmiany
Zmiana "1" -- "0..*" PrzypisanieDoZmiany

@enduml